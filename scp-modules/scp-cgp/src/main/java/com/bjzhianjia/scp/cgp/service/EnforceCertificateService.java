package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.bjzhianjia.scp.cgp.entity.LawEnforcePath;
import com.bjzhianjia.scp.cgp.mapper.LawEnforcePathMapper;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.AreaGridMemberBiz;
import com.bjzhianjia.scp.cgp.biz.EnforceCertificateBiz;
import com.bjzhianjia.scp.cgp.biz.LawEnforcePathBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 执法证服务类
 * 
 * @author zzh
 *
 */
@Service
@Slf4j
public class EnforceCertificateService {

    @Autowired
    private EnforceCertificateBiz enforceCertificateBiz;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private MergeCore mergeCore;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private AreaGridMemberBiz areaGridMemberBiz;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private LawEnforcePathBiz lawEnforcePathBiz;

    @Autowired
    private Environment environment;

    @Autowired
    private LawEnforcePathMapper lawEnforcePathMapper;

    /**
     * 分页查询
     * 
     * @param page
     *            当前页
     * @param limit
     *            页大小
     * @param rightsIssues
     *            查询条件
     * @return
     */
    public TableResultResponse<JSONObject> getList(int page, int limit, EnforceCertificate rightsIssues,String deptId) {

        TableResultResponse<EnforceCertificate> tableResult =
            enforceCertificateBiz.getList(page, limit, rightsIssues, deptId);
        List<EnforceCertificate> list = tableResult.getData().getRows();

        if (list.size() == 0) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        List<String> bizTypes = list.stream().map((o) -> o.getBizLists()).distinct().collect(Collectors.toList());

        /*
         * 业务条线有可能是null，在聚和数据的时候要去除
         */
        if (bizTypes != null && bizTypes.size() > 0) {
            /*
             * -------------------By尚------------------开始----------------
             */
            /*
             * 数据库表中biz_lists字段存放的是业务条线ID集合，用“，”隔开 在此通过Feign联查到业务条线内容
             */
            Map<String, String> bizTypeMap = dictFeign.getByCodeIn(String.join(",", bizTypes));
            // 在rightsIssues对象中，有可能bizLists有可能是两个业务条线ID
            if (bizTypeMap != null && bizTypeMap.size() > 0) {
                for (EnforceCertificate tmp : list) {
                    String bizLists = tmp.getBizLists();
                    if (StringUtils.isBlank(bizLists)) {
                        continue;
                    }
                    String[] split = bizLists.split(",");
                    StringBuilder dictList = new StringBuilder();
                    for (int i = 0; split != null && i < split.length; i++) {
                        if (i == split.length - 1) {
                            dictList.append(bizTypeMap.get(split[i]));
                        } else {
                            dictList.append(bizTypeMap.get(split[i])).append(",");
                        }
                    }
                    tmp.setBizLists(dictList.toString());
                }
            }

            /*
             * -------------------By尚------------------结束----------------
             */
        }

        // 获取执证人联系电话
        List<String> userIds = list.stream().map((o) -> o.getUsrId()).distinct().collect(Collectors.toList());
        if (userIds != null && userIds.size() > 0) {
            Map<String, String> userMap = adminFeign.getUser(String.join(",", userIds));
            if (userMap != null && userMap.size() > 0) {
                for (EnforceCertificate enforceCertificate : list) {
                    if (StringUtils.isBlank(enforceCertificate.getUsrId())) {
                        continue;
                    }
                    String string = userMap.get(enforceCertificate.getUsrId());
                    JSONObject userJObj = JSONObject.parseObject(string);

                    JSONObject userJObjForRetrn = new JSONObject();
                    userJObjForRetrn.put("id", enforceCertificate.getUsrId());
                    userJObjForRetrn.put("telPhone", userJObj != null ? userJObj.getString("mobilePhone") : "");
                    enforceCertificate.setUsrId(userJObjForRetrn.toJSONString());
                }
            }
        }

        try {
            mergeCore.mergeResult(EnforceCertificate.class, list);
        } catch (Exception ex) {
            log.error("merge data exception", ex);
        }

        /*
         * =原本返回对象为TableResultResponse<EnforceCertificate>，在指挥中心页面需要执法者定位信息，
         * =将返回值 改为TableResultResponse<JSONObject>，用于封装与定位信息相关的数据
         */
        JSONArray resultJArray = JSONArray.parseArray(JSON.toJSONString(list));

        /*
         * 执法人员所在部门
         */
        for (int i = 0; i < resultJArray.size(); i++) {
            JSONObject jsonObject = resultJArray.getJSONObject(i);
            JSONArray userDetail = adminFeign.getUserDetail(jsonObject.getJSONObject("usrId").getString("id"));
            List<String> deptNameList = new ArrayList<>();
            if (BeanUtil.isNotEmpty(userDetail)) {
                JSONObject deptJObj;
                for (int j = 0; j < userDetail.size(); j++) {
                    deptJObj = userDetail.getJSONObject(j);
                    deptNameList.add(deptJObj.getString("deptName") == null ? ""
                        : deptJObj.getString("deptName"));
                }
                // 是否党员
                deptJObj = userDetail.getJSONObject(0);
                jsonObject.put("isPartyMember", deptJObj.getString("attr4"));
            }
            jsonObject.put("deptName", String.join(",", deptNameList));
        }

        /*
         * =关联执法人员定位
         */
        Map<String, JSONObject> lawMap = new HashMap<>();
        if (userIds != null && userIds.size() > 0) {
            lawMap = lawEnforcePathBiz.getByUserIds(String.join(",", userIds));
        }

        List<JSONObject> resultJObjList = new ArrayList<>();
        for (int i = 0; i < resultJArray.size(); i++) {
            JSONObject jsonObject = resultJArray.getJSONObject(i);
            if (lawMap != null && !lawMap.isEmpty()) {
                jsonObject.put("mapInfo", lawMap.get(jsonObject.getJSONObject("usrId").getString("id")));
                // jsonObject.put("mapInfo",
                // lawMap.get(jsonObject.getString("usrId")));
            }
            resultJObjList.add(jsonObject);
        }

        return new TableResultResponse<>(tableResult.getData().getTotal(), resultJObjList);
    }

    /**
     * 创建执法证
     * 
     * @param enforceCertificate
     * @return
     */
    public Result<Void> createEnforceCertificate(EnforceCertificate enforceCertificate) {
        Result<Void> result = new Result<>();
        result.setIsSuccess(false);

        EnforceCertificate tempEnforceCertificate =
            enforceCertificateBiz.getByCertCode(enforceCertificate.getCertCode());
        if (tempEnforceCertificate != null) {
            result.setMessage("证件编号已存在");
            return result;
        }

        tempEnforceCertificate = enforceCertificateBiz.getByHolderName(enforceCertificate.getHolderName());
        if (tempEnforceCertificate != null) {
            result.setMessage("持证人已存在");
            return result;
        }

        if (!StringUtil.isEmpty(enforceCertificate.getBizLists())) {
            // 字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
            Map<String, String> bizType = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
            if (bizType == null || bizType.size() == 0) {
                result.setMessage("业务线条不存在");
                return result;
            }
            String[] bizCodes = enforceCertificate.getBizLists().split(",");

            for (String bizCode : bizCodes) {

                if (!bizType.containsKey(bizCode)) {
                    result.setMessage("业务线条不存在");
                    return result;
                }
            }
        }

        enforceCertificateBiz.insertSelective(enforceCertificate);

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }

    /**
     * 修改执法证
     * 
     * @param enforceCertificate
     * @return
     */
    public Result<Void> updateEnforceCertificate(EnforceCertificate enforceCertificate) {
        Result<Void> result = new Result<>();
        result.setIsSuccess(false);

        EnforceCertificate tempEnforceCertificate =
            enforceCertificateBiz.getByCertCode(enforceCertificate.getCertCode());
        if (tempEnforceCertificate != null && !tempEnforceCertificate.getId().equals(enforceCertificate.getId())) {
            result.setMessage("证件编号已存在");
            return result;
        }

        tempEnforceCertificate = enforceCertificateBiz.getByHolderName(enforceCertificate.getHolderName());
        /*
         * 持证人唯一性验证<br/>
         */
        if (tempEnforceCertificate != null) {
            boolean flag = tempEnforceCertificate.getId().equals(enforceCertificate.getId());
            if (!flag) {// 说明在数据库中已经有一条记录，但与enforceCertificate对应的记录不相同
                result.setMessage("持证人已存在");
                return result;
            }
        }

        if (!StringUtil.isEmpty(enforceCertificate.getBizLists())) {
            // 字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
            Map<String, String> bizType = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
            if (bizType == null || bizType.size() == 0) {
                result.setMessage("业务条线不存在");
                return result;
            }
            String[] bizCodes = enforceCertificate.getBizLists().split(",");

            for (String bizCode : bizCodes) {

                if (!bizType.containsKey(bizCode)) {
                    result.setMessage("业务条线不存在");
                    return result;
                }
            }
        }

        enforceCertificateBiz.updateSelectiveById(enforceCertificate);

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }

    /**
     * 获取执法人员详情
     * 
     * @return
     */
    public ObjectRestResponse<JSONObject> getDetailOfCertificater(String userId) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();
        if (StringUtils.isBlank(userId)) {
            restResult.setStatus(400);
            restResult.setMessage("请指定用户ID");
            return restResult;
        }

        JSONObject jsonObject = new JSONObject();

        // 查询执法人员

        /*
         * userDetail内信息
         * SELECT bu.`name` userName,bu.`id` userId,bu.`sex`,bu.`mobile_phone`
         * mobilePhone,
         * bd.`name` deptName,bd.`id` deptId,
         * bp.`name` positionName,bp.`id` positionId
         */
        JSONArray userDetail = adminFeign.getUserDetail(userId);
        if (userDetail != null && !userDetail.isEmpty()) {
            // 按ID进行查询，如果有返回值 ，则返回值必定唯一
            jsonObject = userDetail.getJSONObject(0);
        }

        /*
         * 人员所属网格及角色===============开始======================
         */
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("gridMember", userId);
        List<AreaGridMember> gridMemList = areaGridMemberBiz.getByMap(conditions);
        List<Integer> gridIdList = new ArrayList<>();
        List<String> roleList = new ArrayList<>();

        if (gridMemList != null && !gridMemList.isEmpty()) {
            gridIdList = gridMemList.stream().map(o -> o.getGridId()).distinct().collect(Collectors.toList());
            roleList = gridMemList.stream().map(o -> o.getGridRole()).distinct().collect(Collectors.toList());
        }

        // 所属网格
        List<String> gridNameList = new ArrayList<>();
        if (gridIdList != null && !gridIdList.isEmpty()) {
            List<AreaGrid> gridList = areaGridBiz.getByIds(gridIdList);
            if (gridList != null && !gridList.isEmpty()) {
                gridNameList = gridList.stream().map(o -> o.getGridName()).distinct().collect(Collectors.toList());
            }
        }

        // 所属角色
        List<String> roleNameList = new ArrayList<>();
        if (roleList != null && !roleList.isEmpty()) {
            Map<String, String> roleMap = dictFeign.getByCodeIn(String.join(",", roleList));
            roleNameList = new ArrayList<>(roleMap.values());
        }
        /*
         * 人员所属网格及角色===============结束======================
         */

        // 执法人员定位
        JSONObject mapInfoAndTime = lawEnforcePathBiz.getMapInfoByUserId(userId);

        jsonObject.put("gridName", String.join(",", gridNameList));
        jsonObject.put("gridRoleName", String.join(",", roleNameList));
        jsonObject.put("mapInfo", mapInfoAndTime.isEmpty() ? null : mapInfoAndTime);

        restResult.setStatus(200);
        restResult.setData(jsonObject);
        return restResult;
    }

    /**
     * 查询辅助执行队员
     *
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> fuzhuList(int page, int limit,String name) {
        TableResultResponse<JSONObject> restResponse = new TableResultResponse<>();

        /*
         * 0-> 获取辅助人员岗位ID
         * 1-> 获取辅助人员
         * 2-> 获取轨迹
         * 3-> 有轨迹的排在前面
         */

        JSONObject feignUserResult =
                adminFeign.selectUserByPositionCode(environment.getProperty("fuzhuPositionCode"), 1, Integer.MAX_VALUE,name);
        List<JSONObject> list = new ArrayList<>();
        if (BeanUtil.isNotEmpty(feignUserResult)) {
            JSONArray userJArray = feignUserResult.getJSONObject("data").getJSONArray("rows");
            restResponse.getData().setTotal(feignUserResult.getJSONObject("data").getInteger("total"));

            // 2->
            // 收集人员ID
            Set<String> userIdSet = new HashSet<>();
            for (int i = 0; i < userJArray.size(); i++) {
                JSONObject userJObj = userJArray.getJSONObject(i);
                userIdSet.add(userJObj.getString("id"));
            }
            // ***************************************
            // 处理查询轨迹的时间处理
            // ***************************************

            List<LawEnforcePath> listExcludeRole = null;
            List<EnforceCertificate> enforceByUserIds=null;
            if (BeanUtil.isNotEmpty(userIdSet)) {
                listExcludeRole = lawEnforcePathMapper.getListExcludeRole(userIdSet, null);
                List<String> userIdList = new ArrayList<>(userIdSet);
                enforceByUserIds = enforceCertificateBiz.getEnforceByUserIds(userIdList);
            }
            Map<String, LawEnforcePath> userIdLawPathMap = new HashMap<>();
            if (BeanUtil.isNotEmpty(listExcludeRole)) {
                userIdLawPathMap = listExcludeRole.stream()
                        .collect(Collectors.toMap(LawEnforcePath::getCrtUserId, instance -> instance));
            }
            Map<String, EnforceCertificate> enforcerIdInstanceMap =new HashMap<>();
            if(BeanUtil.isNotEmpty(enforceByUserIds)){
                enforcerIdInstanceMap = enforceByUserIds.stream()
                        .collect(Collectors.toMap(EnforceCertificate::getUsrId, instance -> instance));
            }

            // 查询执法证类型字典
            Map<String, String> dictValueMap = dictFeign.getByCode("root_biz_zfzType");
            if(BeanUtil.isEmpty(dictValueMap)){
                dictValueMap=new HashMap<>();
            }

            for (int i = 0; i < userJArray.size(); i++) {
                JSONObject userJObj = userJArray.getJSONObject(i);

                JSONObject mapInfoJObj = new JSONObject();
                LawEnforcePath lawEnforcePath = userIdLawPathMap.get(userJObj.getString("id"));
                EnforceCertificate enforcer = enforcerIdInstanceMap.get(userJObj.getString("id"));
                if (BeanUtil.isNotEmpty(lawEnforcePath)) {
                    String mapInfoBuilder =
                            "{\"lng\":\"" + lawEnforcePath.getLng() + "\",\"lat\":\"" + lawEnforcePath.getLat() + "\"}";
                    mapInfoJObj.put("mapInfo", mapInfoBuilder);
                    mapInfoJObj.put("time", lawEnforcePath.getCrtTime());
                } else {
                    mapInfoJObj = null;
                }

                if (BeanUtil.isNotEmpty(enforcer)) {
                    userJObj.put("certType", dictValueMap.get(enforcer.getCertType()));
                } else {
                    userJObj.put("certType", null);
                }

                userJObj.put("mapInfo", mapInfoJObj);
                list.add(userJObj);
            }
        }

        // 3-> 有轨迹的排在前面
        list.sort((o1, o2) -> {
            if (BeanUtil.isEmpty(o1.getJSONObject("mapInfo")) && BeanUtil.isEmpty(o2.getJSONObject("mapInfo"))) {
                return 0;
            } else if (BeanUtil.isEmpty(o1.getJSONObject("mapInfo")) && BeanUtil
                    .isNotEmpty(o2.getJSONObject("mapInfo"))) {
                return 1;
            } else if (BeanUtil.isNotEmpty(o1.getJSONObject("mapInfo")) && BeanUtil
                    .isEmpty(o2.getJSONObject("mapInfo"))) {
                return -1;
            } else {
                return o1.getJSONObject("mapInfo").toJSONString().compareTo(o2.getJSONObject("mapInfo").toJSONString());
            }
        });

        // 分页
        List<JSONObject> result = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(page * limit, list.size()); i++) {
            result.add(list.get(i));
        }

        restResponse.getData().setRows(result);
        return restResponse;
    }
}
