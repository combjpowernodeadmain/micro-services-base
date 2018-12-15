package com.bjzhianjia.scp.cgp.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.constances.WritsConstances;
import com.bjzhianjia.scp.cgp.entity.CaseAttachments;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.mapper.WritsInstancesMapper;
import com.bjzhianjia.scp.cgp.mapper.WritsTemplatesMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DocUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * WritsInstancesBiz 类描述.文书模板实例
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月7日          can      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author can
 *
 */
@Service
public class WritsInstancesBiz extends BusinessBiz<WritsInstancesMapper, WritsInstances> {

    @Autowired
    private WritsTemplatesBiz writsTemplatesBiz;

    @Autowired
    private WritsTemplatesMapper writstTemplateMapper;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Autowired
    private CaseAttachmentsBiz caseAttachmentsBiz;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private CaseRegistrationBiz caseRegistrationBiz;

    /**
     * 分页查询记录列表
     * 
     * @author 尚
     * @param writsInstances
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> getList(JSONObject queryJObj, int page, int limit) {
        Example example = new Example(WritsInstances.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(queryJObj.getString("caseId"))) {
            criteria.andEqualTo("caseId", queryJObj.getString("caseId"));
        }
        if (queryJObj.getInteger("templatedId") != null) {
            criteria.andEqualTo("templateId", queryJObj.getInteger("templatedId"));
        }
        if (StringUtils.isNotBlank(queryJObj.getString("procTaskId"))) {
            criteria.andEqualTo("procTaskId", queryJObj.getString("procTaskId"));
        }
        if (StringUtils.isNotBlank(queryJObj.getString("templateCodes"))) {
            // 按code相出相应的模板ID集合
            List<WritsTemplates> temTemplatesList = writsTemplatesBiz.getByTcodes(queryJObj.getString("templateCodes"));
            List<Integer> templateIdList =
                temTemplatesList.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());
            criteria.andIn("templateId", templateIdList);
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<WritsInstances> list = this.selectByExample(example);
        if (BeanUtil.isEmpty(list)) {
            return new TableResultResponse<JSONObject>(0, new ArrayList<JSONObject>());
        }

        List<JSONObject> jObjList = queryAssist(list);

        return new TableResultResponse<>(pageInfo.getTotal(), jObjList);
    }

    private List<JSONObject> queryAssist(List<WritsInstances> list) {
        List<String> templateIdList =
            list.stream().map(o -> String.valueOf(o.getTemplateId())).distinct().collect(Collectors.toList());

        List<WritsTemplates> templateList = writstTemplateMapper.selectByIds(String.join(",", templateIdList));
        Map<Integer, String> template_ID_NAME_Map = new HashMap<>();
        if (BeanUtil.isNotEmpty(templateList)) {
            template_ID_NAME_Map =
                templateList.stream().collect(Collectors.toMap(WritsTemplates::getId, WritsTemplates::getName));
        }

        List<JSONObject> result = new ArrayList<>();

        int count = 1;
        for (WritsInstances writTmp : list) {
            JSONObject tmpJObj = new JSONObject();
            tmpJObj.put("id", writTmp.getId());

            if (StringUtils.isBlank(writTmp.getRefAbbrev())) {
                tmpJObj.put("refAbbrev", "实例" + count);
                count++;
            } else {
                tmpJObj.put("refAbbrev", writTmp.getRefAbbrev());
            }

            tmpJObj.put("templateName", template_ID_NAME_Map.get(writTmp.getTemplateId()));
            result.add(tmpJObj);
        }
        return result;
    }

    /**
     * =更新或插入对象
     * 
     * @author 尚
     * @param bizData
     * @return
     */
    @Transactional
    public Result<Void> updateOrInsert(JSONObject bizData) {
        Result<Void> result = new Result<>();

        WritsInstances writsInstances = JSON.parseObject(bizData.toJSONString(), WritsInstances.class);
        /*
         * =文书ID用writsId作为变量名传入，以增强可读性<br/>
         * =文书ID用writsId作为变量名，因为不能直接parseObject给WritsInstances中的ID的属性，需要手动指定
         */
        writsInstances.setId(bizData.getInteger("writsId"));

        if (!"-1".equals(bizData.getString("procBizId")) && StringUtils.isBlank(writsInstances.getCaseId())) {
            // procBizId即为案件ID，将该值赋给文书里的案件ID
            writsInstances.setCaseId(bizData.getString("procBizId"));
        }

        // 判断当前处于哪个节点上(中队，法治科，镇局)
        String procNode = bizData.getString("processNode");
        // 前端 传入的文书内容
        String fillContext = writsInstances.getFillContext();

        if (writsInstances.getId() == null) {
            // 还没有插入过对象
            JSONObject jObjInDB = mergeFillContext(procNode, fillContext, null, writsInstances.getCaseId(), null);

            // 把处理后的文书内容(fillContext)放回，进行更新操作
            writsInstances.setRefYear(String.valueOf(new LocalDate().getYear()));
            writsInstances.setFillContext(jObjInDB.toJSONString());

            this.insertSelective(writsInstances);
        } else {
            /*
             * = 处理逻辑<br/> 判断当前是谁在审批——从已存在的记录中将审批记录(fill_context)取出，对json字符串进行处理
             */
            WritsInstances writsInstancesInDB = this.selectById(writsInstances.getId());

            String fillContextInDB = writsInstancesInDB.getFillContext();

            JSONObject jObjInDB = JSONObject.parseObject(fillContextInDB);
            jObjInDB = mergeFillContext(procNode, fillContext, jObjInDB, writsInstances.getCaseId(), null);

            // 把处理后的文书内容(fillContext)放回，进行更新操作
            writsInstances.setFillContext(jObjInDB.toJSONString());

            this.updateSelectiveById(writsInstances);
        }

        result.setIsSuccess(true);
        return result;
    }

    /**
     * 根据当前工作的节点(procNode)，处理案件的文书信息(fillContext)，并将该信息存入jObjInDB中返回
     * 
     * @author 尚
     * @param procNode
     * @param fillContext
     * @param jObjInDB
     * @return
     */
    private JSONObject mergeFillContext(String procNode, String fillContext, JSONObject jObjInDB, String caseId,
        String refNo) {
        if (jObjInDB == null) {
            jObjInDB = new JSONObject();
        }

        if (StringUtils.isNotBlank(fillContext)) {
            JSONObject tmpFillContext = JSONObject.parseObject(fillContext);

            jObjInDB.putAll(tmpFillContext);
        }

        return jObjInDB;
    }

    /**
     * 获取文书<br/>
     * 该方法生成一个以.docx为后缀名的word文档，将该文档保存至某一地址，并将该地址返回<br/>
     * @param id
     * @return
     */
    public ObjectRestResponse<String> getWritsInstance(Integer id) {
        ObjectRestResponse<String> restResult = new ObjectRestResponse<>();

        WritsInstances writsInstances = this.selectById(id);
        if (writsInstances == null) {
            restResult.setMessage("文书ID" + id + "下不存在文书实例");
            restResult.setStatus(400);
            return restResult;
        }

        WritsTemplates writsTemplate = writsTemplatesBiz.selectById(writsInstances.getTemplateId());

        String writsPath = "";

        String fillContext = writsInstances.getFillContext();
        try {
            writsPath = _getWritsInstancesAssist(writsInstances, writsTemplate, fillContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        restResult.setData(writsPath);
        return restResult;
    }

    public TableResultResponse<JSONObject> getWithWritsInstance(String templateCodes, String procTaskId,
        String caseId) {
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();

        Example example = new Example(WritsInstances.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isBlank(caseId)) {
            restResult.setStatus(400);
            restResult.setMessage("请指定任务ID");
            return restResult;
        }
        if (StringUtils.isBlank(templateCodes)) {
            restResult.setStatus(400);
            restResult.setMessage("请指定模板tcode");
            return restResult;
        }

        criteria.andEqualTo("caseId", caseId);
        // criteria.andEqualTo("procTaskId", procTaskId);
        // 按code相出相应的模板ID集合
        List<WritsTemplates> temTemplatesList = writsTemplatesBiz.getByTcodes(templateCodes);
        List<Integer> templateIdList =
            temTemplatesList.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());
        if (BeanUtil.isNotEmpty(templateIdList)) {
            criteria.andIn("templateId", templateIdList);
        }

        List<WritsInstances> writsInstancesLis = this.selectByExample(example);

        Map<Integer, String> templateIdNameMap = temTemplatesList.stream().collect(Collectors.toMap(WritsTemplates::getId, WritsTemplates::getName));

        // 模板与模板下的文书结合
        List<JSONObject> templateJObjList = new ArrayList<>();
        int count = 1;
        for (WritsTemplates tmpT : temTemplatesList) {
            JSONObject templateJObj = new JSONObject();
            templateJObj.put("writsTemplatesId", tmpT.getId());
            templateJObj.put("writsTemplates", tmpT.getName());
            List<JSONObject> innerWritsInstancesList = new ArrayList<>();// 用于封装回传前端的文书信息集合
            for (WritsInstances tmpW : writsInstancesLis) {
                // 搜索与temT对应的文书
                if (tmpT.getId().equals(tmpW.getTemplateId())) {
                    JSONObject writsInstancesJObj = new JSONObject();// 用于封装回传前端的文书信息，减少回传数据量
                    writsInstancesJObj.put("writsInstancesId", tmpW.getId());

                    if (StringUtils.isBlank(tmpW.getRefAbbrev())) {
                        writsInstancesJObj.put("writsInstancesName",
                            templateIdNameMap.get(tmpW.getTemplateId()) == null ? "实例"
                                : templateIdNameMap.get(tmpW.getTemplateId()) + count);
                        count++;
                    } else {
                        writsInstancesJObj.put("writsInstancesName", tmpW.getRefAbbrev());
                    }
                    innerWritsInstancesList.add(writsInstancesJObj);
                }
                templateJObj.put("writsInstances", innerWritsInstancesList);
            }
            templateJObjList.add(templateJObj);

        }
        return new TableResultResponse<>(0, templateJObjList);
    }

    /**
     * =查询目前某一refEnforceType下，某一年中refYear最大的记录
     * 
     * @param caseId
     * @param templateId
     * @param refEnforceType
     * @return
     */
    public WritsInstances theMaxWenHao(String caseId, Integer templateId, String refEnforceType) {
        Example example = new Example(WritsInstances.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("refEnforceType", refEnforceType);
        criteria.andEqualTo("refYear", new LocalDate().getYear());

        example.setOrderByClause("ref_no desc");
        PageHelper.startPage(1, 1);

        List<WritsInstances> maxRefNoWritsInstanceList = this.selectByExample(example);
        if (BeanUtil.isNotEmpty(maxRefNoWritsInstanceList)) {
            // 对某一refEnforceType下的文书按refYear进行倒序查找，查询数量为1，如果集合不为空，则结果集只有一个
            return maxRefNoWritsInstanceList.get(0);
        }

        return null;
    }

    /**
     * =查询目前某一refEnforceType下，某一年中refYear最大的文号序号，并将文号序号加1后返回
     * 
     * @param caseId
     * @param templateId
     * @param refEnforceType
     * @return
     */
    public WritsInstances theNextWenHao(String caseId, Integer templateId, String refEnforceType) {
        WritsInstances maxWenHao = theMaxWenHao(caseId, templateId, refEnforceType);

        WritsInstances resultInstance = new WritsInstances();
        if (BeanUtil.isNotEmpty(maxWenHao)) {
            resultInstance.setRefNo(String.valueOf(Integer.valueOf(maxWenHao.getRefNo()) + 1));
        } else {
            resultInstance.setRefNo("1");
        }

        return resultInstance;
    }

    /**
     * =添加文书暂存
     * 
     * @param writsInstances
     * @return
     */
    public ObjectRestResponse<WritsInstances> addCache(JSONObject writsJObj) {
        WritsInstances writsInstances = JSONObject.toJavaObject(writsJObj, WritsInstances.class);

        if (BeanUtil.isEmpty(writsInstances.getTemplateId())) {
            // 尝试通过tcode来获取模板ID
            // 获取模板ID
            String tCode = writsJObj.getString("tCode");
            List<WritsTemplates> writsTemplateList = writsTemplatesBiz.getByTcodes(tCode);
            WritsTemplates writsTemplates = new WritsTemplates();
            if (BeanUtil.isNotEmpty(writsTemplateList)) {
                writsTemplates = writsTemplateList.get(0);
            }

            writsInstances.setTemplateId(writsTemplates.getId());
        }

        ObjectRestResponse<WritsInstances> restResult = new ObjectRestResponse<>();

        this.insertSelective(writsInstances);
        // 将自动生成的ID传回前端
        writsInstances.setId(writsInstances.getId());

        restResult.setData(writsInstances);
        restResult.setStatus(200);
        return restResult;
    }

    /**
     * =查询某一案件下的文书及历史
     * 
     * @param caseId
     * @return
     */
    public ObjectRestResponse<JSONArray> writsAndAttachmentHistory(String caseId) {
        ObjectRestResponse<JSONArray> restResult = new ObjectRestResponse<>();
        JSONArray resultJArray = new JSONArray();

        if (StringUtils.isBlank(caseId)) {
            restResult.setStatus(400);
            restResult.setMessage("请指定任务ID");
            return restResult;
        }

        List<WritsInstances> writsInstanceList = getWrtisInstances(caseId);
        List<CaseAttachments> attachmentsList = getAttachments(caseId);
        List<WritsTemplates> templateList= _getTemplates(writsInstanceList);
        Map<Integer, String> templateIdNameMap=new HashMap<>();
        if(BeanUtil.isNotEmpty(templateList)){
            templateIdNameMap = templateList.stream().collect(Collectors.toMap(WritsTemplates::getId, WritsTemplates::getName));
        }

        // 收集需要查询的人
        Set<String> userIdList = new HashSet<>();
        List<String> writsCrtUserIdList =
            writsInstanceList.stream().map(o -> o.getCrtUserId()).distinct().collect(Collectors.toList());
        List<String> attaCrtUserIdList =
            attachmentsList.stream().map(o -> o.getCrtUserId()).collect(Collectors.toList());

        userIdList.addAll(writsCrtUserIdList);
        userIdList.addAll(attaCrtUserIdList);

        Map<String, String> userMap = new HashMap<>();
        if (BeanUtil.isNotEmpty(userIdList)) {
            userMap = adminFeign.getUser(String.join(",", userIdList));
        }

        int count = 1;
        for (WritsInstances writsInstances : writsInstanceList) {
            JSONObject tmp = new JSONObject();
            tmp.put("id", writsInstances.getId());
            tmp.put("refNo", writsInstances.getRefNo());// 文号
            tmp.put("crtUserId", writsInstances.getCrtUserId());// 上传人ID
            tmp.put("crtUserName", CommonUtil.getValueFromJObjStr(userMap.get(writsInstances.getCrtUserId()), "name"));// 上传人姓名
            tmp.put("crtTime", writsInstances.getCrtTime());// 上传时间
            tmp.put("type", "writs");

            if (StringUtils.isBlank(writsInstances.getRefAbbrev())) {
                tmp.put("templateName",
                        templateIdNameMap.get(writsInstances.getTemplateId()) == null ? "实例"
                                : templateIdNameMap.get(writsInstances.getTemplateId()) + count);
                count++;
            } else {
                tmp.put("templateName", writsInstances.getRefAbbrev());
            }
            resultJArray.add(tmp);
        }
        for (CaseAttachments caseAttachments : attachmentsList) {
            JSONObject tmp = new JSONObject();
            tmp.put("uploadPhraseTitle", caseAttachments.getUploadPhraseTitle());// 文号
            tmp.put("crtUserId", caseAttachments.getCrtUserId());// 上传人ID
            tmp.put("crtUserName", CommonUtil.getValueFromJObjStr(userMap.get(caseAttachments.getCrtUserId()), "name"));// 上传人姓名
            tmp.put("crtTime", caseAttachments.getCrtTime());// 上传时间
            tmp.put("type", "attachment");
            tmp.put("docUrl", caseAttachments.getDocUrl());
            tmp.put("attachmentsName", caseAttachments.getAttachmentsName());//附件名称(文书名称)
            resultJArray.add(tmp);
        }

        restResult.setStatus(200);
        restResult.setData(resultJArray);
        return restResult;
    }

    /**
     * 查询与 writsInstanceList文书对应的模板
     * @param writsInstanceList
     * @return
     */
    private List<WritsTemplates> _getTemplates(List<WritsInstances> writsInstanceList) {
        if (BeanUtil.isEmpty(writsInstanceList)) {
            return new ArrayList<>();
        }

        List<String> templatesIdList =
            writsInstanceList.stream().map(o -> String.valueOf(o.getTemplateId())).distinct()
                .collect(Collectors.toList());
        if (BeanUtil.isNotEmpty(templatesIdList)) {
            Example example = new Example(WritsTemplates.class).selectProperties("id", "name");
            example.createCriteria().andIn("id", templatesIdList);
            List<WritsTemplates> writsTemplates = writsTemplatesBiz.selectByExample(example);
            return writsTemplates;
        }

        return new ArrayList<>();
    }

    private List<CaseAttachments> getAttachments(String caseId) {
        Example example = new Example(CaseAttachments.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("caseId", caseId);

        List<CaseAttachments> attachmentList = caseAttachmentsBiz.selectByExample(example);
        if (BeanUtil.isEmpty(attachmentList)) {
            return new ArrayList<CaseAttachments>();
        }
        return attachmentList;
    }

    private List<WritsInstances> getWrtisInstances(String caseId) {
        Example example = new Example(WritsInstances.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("caseId", caseId);

        List<WritsInstances> writsList = this.selectByExample(example);
        if (BeanUtil.isEmpty(writsList)) {
            return new ArrayList<WritsInstances>();
        }
        return writsList;
    }

    /**
     * 批量添加记录
     * 
     * @param writsInstancesJArray
     * @return
     */
    @Deprecated
    public ObjectRestResponse<WritsInstances> addList(JSONArray writsInstancesJArray) {
        List<WritsInstances> writsInstanceList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(writsInstancesJArray)) {
            writsInstanceList = writsInstancesJArray.toJavaList(WritsInstances.class);
        }

        ObjectRestResponse<WritsInstances> restResult = new ObjectRestResponse<>();
        for (WritsInstances writsInstances : writsInstanceList) {
            writsInstances.setCrtUserId(BaseContextHandler.getUserID());
            writsInstances.setCrtUserName(BaseContextHandler.getUsername());
            writsInstances.setCrtTime(new Date());
            writsInstances.setTenantId(BaseContextHandler.getTenantID());
            writsInstances.setIsDeleted("0");
        }

        this.mapper.insertWritsInstancesList(writsInstanceList);

        return restResult;
    }

    /**
     * 将请求信息中文书信息进行保存
     * 
     * @param caseRegJObj
     */
    public void addWritsInstances(JSONObject caseRegJObj) {
        String caseId = caseRegJObj.getString("procBizId");

        JSONArray writsInstancesJArray = caseRegJObj.getJSONArray("writsInstances");

        if(BeanUtil.isEmpty(writsInstancesJArray)){
            // 如果文书实例数组为空，则直接返回，不进行以后的操作
            return;
        }

        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            /*
             * =文书ID用writsId作为变量名传入，以增强可读性<br/>
             * =文书ID用writsId作为变量名，因为不能直接parseObject给WritsInstances中的ID的属性，需要手动指定
             */
            JSONObject writsJObj = writsInstancesJArray.getJSONObject(i);
            writsJObj.put("id", writsJObj.getString("writsId"));
        }

        // 收集前端传入的模板tcode值，可能没有传入，如果没传，则该操作为更新操作
        List<String> tcodeList = new ArrayList<>();
        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            String tcode = writsInstancesJArray.getJSONObject(i).getString("tcode");
            if (StringUtils.isNotBlank(tcode)) {
                tcodeList.add(tcode);
            }
        }

        Map<String, Integer> template_TCODE_ID_Map = new HashMap<>();
        if (BeanUtil.isNotEmpty(tcodeList)) {
            // 如果tcodeList集合为空，说明目前上审批状态，没有要添加新文书的操作，以下将进行更新操作
            List<WritsTemplates> templateList = writsTemplatesBiz.getByTcodes(String.join(",", tcodeList));
            if (BeanUtil.isNotEmpty(templateList)) {
                template_TCODE_ID_Map =
                    templateList.stream().collect(Collectors.toMap(WritsTemplates::getTcode, WritsTemplates::getId));
            }
        }

        // 关联该文书相关的案件
        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            JSONObject writsJObj = writsInstancesJArray.getJSONObject(i);
            WritsInstances writsInstances = writsJObj.toJavaObject(WritsInstances.class);
            writsInstances.setCaseId(caseId);

            /*
             * 判断文书是否进行过暂存(如果请求参数中有文书ID表明暂存过)<br/>
             * 或是一个更新操作（如果请求参数中有文书ID表明是更新操作）
             */
            if (BeanUtil.isEmpty(writsInstances.getId())) {
                /*
                 * 没有暂存过，进行一次添加操作<br/>
                 * 前端并没有办法获取文书ID，然后入的是文书的code值，
                 */
                if (BeanUtil.isEmpty(template_TCODE_ID_Map)) {
                    /*
                     * 如果程序执行到这里，总该有一条记录里tcode不为空，如果tcode为空，
                     * 说明没有进行暂存的那条记录也没有传tcode进来
                     */
                    throw new BizException("请指定模板ID或模板tcode");
                }
                writsInstances.setTemplateId(template_TCODE_ID_Map.get(writsJObj.getString("tcode")));
                this.insertSelective(writsInstances);
            } else {
                // 暂存过，进行更新操作
                WritsInstances writsInstanceInDB = this.selectById(writsInstances.getId());
                // writsInstances.setTemplateId(writsTemplates.getId());
                JSONObject mergeFillContext =
                    mergeFillContext(null, writsInstances.getFillContext(),
                        JSONObject.parseObject(writsInstanceInDB.getFillContext()), null, null);
                writsInstances.setFillContext(mergeFillContext.toJSONString());
                this.updateSelectiveById(writsInstances);
            }
        }
    }

    /**
     * 生成PDF文书，并返回文书名称<br/>
     * 如果案件对象的文书已经存在，则直接返回该文书名称<br/>
     * 如果文书内容发生改变，则删除旧文书，并生成新文书
     * 
     * @param writsId
     * @return
     */
    public String getTruePDFWritsInstancesById(Integer writsId) {
        // 生成文书实例
        ObjectRestResponse<String> _fileNameRest = this.getWritsInstance(writsId);

        String fullDocFileName = _fileNameRest.getData();
        String fullPDFFileName =
            fullDocFileName.substring(0, fullDocFileName.lastIndexOf(".")) + WritsConstances.WRITS_SUFFIX_PDF;

        if (DocUtil.exists(propertiesConfig.getDestFilePath() + fullPDFFileName)) {
            return fullPDFFileName;
        }

        // PDF文书实例不存在
        List<String> ignoreFileNameList = new ArrayList<>();
        ignoreFileNameList.add(fullPDFFileName);
        DocUtil.deletePrefix(WritsConstances.WRITS_PREFFIX, WritsConstances.WRITS_SUFFIX_PDF,
            propertiesConfig.getDestFilePath(), ignoreFileNameList);

        try {
            // 将openOffice地址及端口通过配置文件指定
            DocUtil.WordToPDF(propertiesConfig.getDestFilePath() + fullDocFileName,
                propertiesConfig.getDestFilePath() + fullPDFFileName,
                propertiesConfig.getOpenOfficeHost(),
                Integer.valueOf(propertiesConfig.getOpenOfficePort()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullPDFFileName;
    }

    /**
     * 添加手机端文书实例
     * @param bizData
     */
    public void insertInstanceClient(JSONObject bizData) {
        caseRegistrationBiz.insertInstanceClient(bizData);
    }

    /**
     * 根据案件ID获取文书记录
     * 
     * @param caseId
     * @return
     */
    public TableResultResponse<JSONObject> getByCaseId(String caseId) {
        Example example = new Example(WritsInstances.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("caseId", caseId);

        List<WritsInstances> writsInstances = this.selectByExample(example);
        if (BeanUtil.isEmpty(writsInstances)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        /*
         * 收集文书模板ID，进行一次性查询模板，查询模板目的用于在前端页面显示文书名称
         * 如：《立案核查1》
         */
        List<String> templateIdList =
            writsInstances.stream().map(o -> String.valueOf(o.getTemplateId())).distinct()
                .collect(Collectors.toList());

        List<WritsTemplates> writsTemplates = null;
        if (BeanUtil.isNotEmpty(templateIdList)) {
            writsTemplates = writstTemplateMapper.selectByIds(String.join(",", templateIdList));
        }

        Map<Integer, String> templateIdNameMap = new HashMap<>();// 用于封装文书模板ID与name的Map集合
        if (BeanUtil.isNotEmpty(writsTemplates)) {
            for (WritsTemplates writsTemplatesTmp : writsTemplates) {
                templateIdNameMap.put(writsTemplatesTmp.getId(), writsTemplatesTmp.getName());
            }
        }

        // 整合需要显示的文书名称，同一个文书模板下可能有多个文书，用序号进行区分
        int index = 1;
        List<JSONObject> resultList = new ArrayList<>();
        for (WritsInstances writsInstancesTmp : writsInstances) {
            JSONObject resultJObj = new JSONObject();
            resultJObj.put("id", writsInstancesTmp.getId());
            String writsInstanceName =
                templateIdNameMap.get(writsInstancesTmp.getTemplateId()) + index;
            resultJObj.put(writsInstanceName, writsInstanceName);
            index++;

            resultList.add(resultJObj);
        }
        return new TableResultResponse<>(resultList.size(), resultList);
    }

    /**
     * 获取文书<br/>
     * 该方法生成一个以.docx为后缀名的word文档，将该文档保存至某一地址，并将该地址返回<br/>
     * @param writsInstancesJObj
     * @return
     */
    public ObjectRestResponse<String> getTemporaryWritsInstance(JSONObject writsInstancesJObj) {
        ObjectRestResponse<String> restResult = new ObjectRestResponse<>();

        WritsInstances writsInstances = JSONObject.parseObject(writsInstancesJObj.toJSONString(), WritsInstances.class);

        if (writsInstances == null) {
            restResult.setMessage("生成临时文书失败");
            restResult.setStatus(400);
            return restResult;
        }

        List<WritsTemplates> templatesList = writsTemplatesBiz.getByTcodes(writsInstancesJObj.getString("tcode"));

        if(BeanUtil.isEmpty(templatesList)){
            restResult.setMessage("请指定文书模板");
            restResult.setStatus(400);
            return restResult;
        }

        WritsTemplates writsTemplate = templatesList.get(0);

        String writsPath = "";

        String fillContext = writsInstances.getFillContext();
        try {
            writsPath = _getWritsInstancesAssist(writsInstances, writsTemplate, fillContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        restResult.setData(writsPath);
        return restResult;
    }

    /**
     * 生成文书实例(word文档)帮助方法
     * @param writsInstances
     * @param writsTemplate
     * @param fillContext
     * @return
     * @throws Exception
     */
    private String _getWritsInstancesAssist(WritsInstances writsInstances, WritsTemplates writsTemplate, String fillContext) throws Exception {
        String writsPath;// 生成与fillContext相对应的文件名
        StringBuffer destFileNameBuffer = new StringBuffer();
        destFileNameBuffer.append(WritsConstances.WRITS_PREFFIX).append(writsInstances.getCaseId()).append("_")
                .append(fillContext.hashCode()).append(WritsConstances.WRITS_SUFFIX_DOCX);

        String destPath = propertiesConfig.getDestFilePath() + destFileNameBuffer.toString();

        if (DocUtil.exists(destPath)) {
            writsPath = destFileNameBuffer.toString();
        } else {
            // 说明可能已存在过旧文件，将所有旧文件进行删除，将生成的新文件进行生成并返回路径
            List<String> ignoreFileNameList = new ArrayList<>();
            ignoreFileNameList.add(destFileNameBuffer.toString());

            DocUtil.deletePrefix(WritsConstances.WRITS_PREFFIX + writsInstances.getCaseId() + "_",
                    WritsConstances.WRITS_SUFFIX_DOCX, propertiesConfig.getDestFilePath(), ignoreFileNameList);

            // 将fillContext内的内容添加到文书模板上
            JSONObject fillJObj = JSONObject.parseObject(fillContext);

            @SuppressWarnings({ "unchecked", "rawtypes" })
            Map<String, String> map = (Map) fillJObj;
            // 文书模板“执法任务”“中队信息”去除，不再使用fillContext字段中的“zHiHao”作案件文号
            // 如果文书内容中refYear与refNo为null，请置为空，避免前端因没传该参数造成文书模板填充错误
            map.put("refYear",
                writsInstances.getRefYear() == null ? "" : writsInstances.getRefYear());
            map.put("refNo", writsInstances.getRefNo() == null ? "" : writsInstances.getRefNo());

            /*
             * 获取模板中的占位符，如果map中还未包含某占位符，则将其添加到map中
             */
            DocUtil.getSpecialcharacters(writsTemplate.getDocUrl(), map);

            writsPath =
                    DocUtil.getDestUrlAfterReplaceWord(writsTemplate.getDocUrl(),
                            destFileNameBuffer.toString(), propertiesConfig.getDestFilePath(), map);
        }
        return writsPath;
    }
}