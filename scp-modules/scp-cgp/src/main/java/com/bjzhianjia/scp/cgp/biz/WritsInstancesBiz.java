package com.bjzhianjia.scp.cgp.biz;

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
    private WritsTemplatesMapper writsTemplatesMapper;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Autowired
    private CaseAttachmentsBiz caseAttachmentsBiz;

    @Autowired
    private AdminFeign adminFeign;

    /**
     * 分页查询记录列表
     * 
     * @author 尚
     * @param writsInstances
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<WritsInstances> getList(JSONObject queryJObj, int page, int limit) {
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

        return new TableResultResponse<>(pageInfo.getTotal(), list);
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

            // WritsInstances theNextWenHao =
            // theNextWenHao(writsInstances.getCaseId(),
            // writsInstances.getTemplateId(),
            // writsInstances.getRefEnforceType());

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
            // tmpFillContext.put("ZiHao",
            // tmpFillContext.getString("ZiHao") == null ? "" :
            // tmpFillContext.getString("ZiHao") + refNo);
            jObjInDB.putAll(tmpFillContext);
        }

        return jObjInDB;
    }

    /**
     * =获取文书<br/>
     * =该方法生成一个以.docx为后缀名的word文档，将该文档保存至某一地址，并将该地址返回<br/>
     * 
     * 
     * @param tcode
     * @param caseId
     * @param procTaskId
     * @return
     */
    public ObjectRestResponse<String> getWritsInstance(Integer id) {
        ObjectRestResponse<String> restResult = new ObjectRestResponse<>();

        WritsInstances writsInstances = this.selectById(id);
        WritsTemplates writsTemplate = writsTemplatesBiz.selectById(writsInstances.getTemplateId());

        String writsPath = "";

        if (writsInstances != null) {
            String fillContext = writsInstances.getFillContext();

            // 生成与fillContext相对应的文件名
            StringBuffer destFileNameBuffer = new StringBuffer();
            destFileNameBuffer.append(WritsConstances.WRITS_PREFFIX).append(writsInstances.getCaseId()).append("_")
                .append(fillContext.hashCode()).append(".docx");

            String destPath = propertiesConfig.getDestFilePath() + destFileNameBuffer.toString();

            if (DocUtil.exists(destPath)) {
                writsPath = destFileNameBuffer.toString();
            } else {
                // 说明可能已存在过旧文件，将所有旧文件进行删除，将生成的新文件进行生成并返回路径
                List<String> ignoreFileNameList = new ArrayList<>();
                ignoreFileNameList.add(destFileNameBuffer.toString());

                DocUtil.deletePrefix(WritsConstances.WRITS_PREFFIX + writsInstances.getCaseId() + "_",
                    WritsConstances.WRITS_SUFFIX, propertiesConfig.getDestFilePath(), ignoreFileNameList);

                // 将fillContext内的内容添加到文书模板上
                JSONObject fillJObj = JSONObject.parseObject(fillContext);

                StringBuffer ziHao = new StringBuffer();
                ziHao.append(writsInstances.getRefEnforceType()).append("[").append(writsInstances.getRefYear())
                    .append("]").append(writsInstances.getRefNo());
                fillJObj.put("ZiHao", ziHao.toString());

                @SuppressWarnings({ "unchecked", "rawtypes" })
                Map<String, String> map = (Map) fillJObj;

                try {
                    writsPath =
                        DocUtil.getDestUrlAfterReplaceWord(writsTemplate.getDocUrl(), destFileNameBuffer.toString(),
                            propertiesConfig.getDestFilePath(), map);
                } catch (Exception e) {
                    e.printStackTrace();
                    restResult.setStatus(400);
                    restResult.setMessage(e.getMessage());
                    return restResult;
                }
            }
        }

        restResult.setData(writsPath);
        // restResult.setData("http://www.xdocin.com/demo/demo.docx");
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
        // if (StringUtils.isBlank(procTaskId)) {
        // restResult.setStatus(400);
        // restResult.setMessage("请指定流程任务ID");
        // return restResult;
        // }
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
        if(BeanUtil.isNotEmpty(templateIdList)) {
            criteria.andIn("templateId", templateIdList);
        }

        List<WritsInstances> writsInstancesLis = this.selectByExample(example);

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
                    writsInstancesJObj.put("writsInstancesName",
                        StringUtils.isEmpty(tmpW.getRefAbbrev()) ? "实例" + count : tmpW.getRefAbbrev());
                    innerWritsInstancesList.add(writsInstancesJObj);
                }
                templateJObj.put("writsInstances", innerWritsInstancesList);
            }
            templateJObjList.add(templateJObj);

            count++;
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
        // criteria.andEqualTo("isDeleted", "0");
        // criteria.andEqualTo("caseId", caseId);
        // criteria.andEqualTo("templateId", caseId);
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

        // 收集需要查询的人
        Set<String> userIdList = new HashSet<>();
        List<String> writsCrtUserIdList =
            writsInstanceList.stream().map(o -> o.getCrtUserId()).distinct().collect(Collectors.toList());
        // List<String> writstemplateIdList =
        // writsInstanceList.stream().map(o ->
        // String.valueOf(o.getTemplateId())).distinct()
        // .collect(Collectors.toList());
        List<String> attaCrtUserIdList =
            attachmentsList.stream().map(o -> o.getCrtUserId()).collect(Collectors.toList());

        userIdList.addAll(writsCrtUserIdList);
        userIdList.addAll(attaCrtUserIdList);

        Map<String, String> userMap = new HashMap<>();
        if (BeanUtil.isNotEmpty(userIdList)) {
            userMap = adminFeign.getUser(String.join(",", userIdList));
        }

        // 查询文书对应的模板
        // List<WritsTemplates> templateList = new ArrayList<>();
        // Map<Integer, String> template_ID_NAME_Map = new HashMap<>();
        // if (BeanUtil.isNotEmpty(writstemplateIdList)) {
        // templateList = writsTemplatesMapper.selectByIds(String.join(",",
        // writstemplateIdList));
        // template_ID_NAME_Map =
        // templateList.stream().collect(Collectors.toMap(WritsTemplates::getId,
        // WritsTemplates::getName));
        // }

        for (WritsInstances writsInstances : writsInstanceList) {
            JSONObject tmp = new JSONObject();
            tmp.put("id", writsInstances.getId());
            tmp.put("refNo", writsInstances.getRefNo());// 文号
            tmp.put("crtUserId", writsInstances.getCrtUserId());// 上传人ID
            tmp.put("crtUserName", CommonUtil.getValueFromJObjStr(userMap.get(writsInstances.getCrtUserId()), "name"));// 上传人姓名
            tmp.put("crtTime", writsInstances.getCrtTime());// 上传时间
            tmp.put("type", "writs");
            tmp.put("templateName", writsInstances.getRefAbbrev());
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
            resultJArray.add(tmp);
        }

        restResult.setStatus(200);
        restResult.setData(resultJArray);
        return restResult;
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
     * @param writsInstancesList
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
     * @param caseId
     */
    public void addWritsInstances(JSONObject caseRegJObj) {
        String caseId = caseRegJObj.getString("procBizId");

        JSONArray writsInstancesJArray = caseRegJObj.getJSONArray("writsInstances");
        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            /*
             * =文书ID用writsId作为变量名传入，以增强可读性<br/>
             * =文书ID用writsId作为变量名，因为不能直接parseObject给WritsInstances中的ID的属性，需要手动指定
             */
            JSONObject writsJObj = writsInstancesJArray.getJSONObject(i);
            writsJObj.put("id", writsJObj.getString("writsId"));
        }

        String tcode = "";
        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            tcode = writsInstancesJArray.getJSONObject(i).getString("tcode");
        }

        WritsTemplates writsTemplates = new WritsTemplates();
        List<WritsTemplates> templateList = writsTemplatesBiz.getByTcodes(tcode);
        if (BeanUtil.isNotEmpty(templateList)) {
            // 传入一个tcode值，则得到的结果也一定是一个
            writsTemplates = templateList.get(0);
        }

        List<WritsInstances> writsInstanceList =
            JSONArray.parseArray(writsInstancesJArray.toJSONString(), WritsInstances.class);

        if (writsInstanceList == null) {
            writsInstanceList = new ArrayList<>();
        }

        // 关联该文书相关的案件
        for (WritsInstances writsInstances : writsInstanceList) {
            // 无法判断传入的文书是否进行暂存过，需要进行逐条验证

            writsInstances.setCaseId(caseId);
            // 判断文书是否进行过暂存(如果请求参数中有文书ID表明暂存过)
            if (BeanUtil.isEmpty(writsInstances.getId())) {
                /*
                 * 没有暂存过，进行一次添加操作<br/>
                 * 前端并没有办法获取文书ID，然后入的是文书的code值，
                 */
                if (StringUtils.isBlank(tcode)) {
                    /*
                     * 如果程序执行到这里，总该有一条记录里tcode不为空，如果tcode为空，
                     * 说明没有进行暂存的那条记录也没有传tcode进来
                     */
                    throw new BizException("请指定模板ID或模板tcode");
                }
                writsInstances.setTemplateId(writsTemplates.getId());
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
}