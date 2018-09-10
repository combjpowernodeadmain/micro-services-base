package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
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
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.mapper.WritsInstancesMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DocUtil;
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
            WritsInstances theNextWenHao =
                theNextWenHao(writsInstances.getCaseId(), writsInstances.getTemplateId(),
                    writsInstances.getRefEnforceType());

            // 还没有插入过对象
            JSONObject jObjInDB =
                mergeFillContext(procNode, fillContext, null, writsInstances.getCaseId(), theNextWenHao.getRefNo());

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
            jObjInDB =
                mergeFillContext(procNode, fillContext, jObjInDB, writsInstances.getCaseId(),
                    writsInstancesInDB.getRefNo());

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
            destFileNameBuffer.append(WritsConstances.WRITS_PREFFIX).append(fillContext.hashCode()).append(".docx");

            String destPath = propertiesConfig.getDestFilePath() + destFileNameBuffer.toString();

            if (DocUtil.exists(destPath)) {
                writsPath = destFileNameBuffer.toString();
            } else {
                // 说明可能已存在过旧文件，将所有旧文件进行删除，将生成的新文件进行生成并返回路径
                // 该删除操作已放到定时任务中进行com.bjzhianjia.scp.cgp.service.DocService.deletedDocFiles()
                // DocUtil.deletePrefix(WritsConstances.WRITS_PREFFIX,
                // WritsConstances.WRITS_SUFFIX,
                // propertiesConfig.getDestFilePath());

                // 将fillContext内的内容添加到文书模板上
                JSONObject fillJObj = JSONObject.parseObject(fillContext);

                StringBuffer ziHao = new StringBuffer();
                ziHao.append("[").append(writsInstances.getRefYear()).append("]").append(writsInstances.getRefNo());
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

        // restResult.setData(writsPath);
        restResult.setData("http://www.xdocin.com/demo/demo.docx");
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
        criteria.andIn("templateId", templateIdList);

        List<WritsInstances> writsInstancesLis = this.selectByExample(example);

        // 模板与模板下的文书结合
        List<JSONObject> templateJObjList = new ArrayList<>();
        for (WritsTemplates tmpT : temTemplatesList) {
            JSONObject templateJObj = new JSONObject();
            templateJObj.put("writsTemplatesId", tmpT.getId());
            templateJObj.put("writsTemplates", tmpT.getName());
            List<JSONObject> innerWritsInstancesList = new ArrayList<>();// 用于封装回传前端的文书信息集合
            int count = 1;
            for (WritsInstances tmpW : writsInstancesLis) {
                // 搜索与temT对应的文书
                if (tmpT.getId().equals(tmpW.getTemplateId())) {
                    JSONObject writsInstancesJObj = new JSONObject();// 用于封装回传前端的文书信息，减少回传数据量
                    writsInstancesJObj.put("writsInstancesId", tmpW.getId());
                    writsInstancesJObj.put("writsInstancesName", "实例" + count);
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
    public ObjectRestResponse<WritsInstances> addCache(WritsInstances writsInstances) {
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
        List<String> attaCrtUserIdList =
            attachmentsList.stream().map(o -> o.getCrtUserId()).collect(Collectors.toList());

        userIdList.addAll(writsCrtUserIdList);
        userIdList.addAll(attaCrtUserIdList);

        Map<String, String> userMap = adminFeign.getUser(String.join(",", userIdList));

        for (WritsInstances writsInstances : writsInstanceList) {
            JSONObject tmp = new JSONObject();
            tmp.put("refNo", writsInstances.getRefNo());// 文号
            tmp.put("crtUserId", writsInstances.getCrtUserId());// 上传人ID
            tmp.put("crtUserName", CommonUtil.getValueFromJObjStr(userMap.get(writsInstances.getCrtUserId()), "name"));// 上传人姓名
            tmp.put("crtTime", writsInstances.getCrtTime());// 上传时间
            
            resultJArray.add(tmp);
        }
        for (CaseAttachments caseAttachments : attachmentsList) {
            JSONObject tmp = new JSONObject();
            tmp.put("uploadPhraseTitle", caseAttachments.getUploadPhraseTitle());// 文号
            tmp.put("crtUserId", caseAttachments.getCrtUserId());// 上传人ID
            tmp.put("crtUserName", CommonUtil.getValueFromJObjStr(userMap.get(caseAttachments.getCrtUserId()), "name"));// 上传人姓名
            tmp.put("crtTime", caseAttachments.getCrtTime());// 上传时间
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
}