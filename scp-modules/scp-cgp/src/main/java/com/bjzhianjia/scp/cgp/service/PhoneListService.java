package com.bjzhianjia.scp.cgp.service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.EnforceCertificateBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.feign.IUserFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * PhoneListService 通讯录.
 * <p>
 * ${tags}
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/11/5          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@Service
@Transactional
public class PhoneListService {
    @Autowired
    private IUserFeign iUserFeign;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private EnforceCertificateBiz enforceCertificateBiz;


    public TableResultResponse<Map<String, Object>> phoneList(String userName, String deptIds, Integer page, Integer limit) {
        // 查询所有部门记录，作为递归数据源
        List<JSONObject> allDeptList = adminFeign.deptListAll();
        if(allDeptList==null){
            allDeptList=new ArrayList<>();
        }

        // 处理部门ID，如果deptIds中含有子部门，由进行整合
        deptIds=bindChildrenDeptId(deptIds,allDeptList);

        // 处理所有部门信息至"id":"JSON对象"形式
        JSONObject deptIdInstanceJObj=new JSONObject();
        for(JSONObject tmp:allDeptList){
            deptIdInstanceJObj.put(tmp.getString("id"), tmp);
        }

        TableResultResponse<Map<String, Object>> userList = iUserFeign.phoneList(userName, deptIds, page, limit);
        List<Map<String, Object>> result = userList.getData().getRows();
        if (BeanUtil.isNotEmpty(result)) {
            //缓存用户ids集
            List<String> userIds = new ArrayList<>();
            for (Map<String, Object> map : result) {
                userIds.add(String.valueOf(map.get("userId")));

                // 将部门名称整合为==》"当前部门(父部门)"形式
                if (map.get("deptId") != null) {
                    // 说明该条记录存在部门信息,从deptIdInstanceJObj集中将该部门信息取出来
                    JSONObject deptJObj =
                        deptIdInstanceJObj.getJSONObject(String.valueOf(map.get("deptId")));
                    String deptName = "";
                    if (StringUtils.isNotBlank(deptJObj.getString("name"))) {
                        deptName = deptJObj.getString("name");
                    }
                    if (!StringUtils.equals(deptJObj.getString("parentId"), "-1")
                        && !StringUtils.equals(deptJObj.getString("parentId"), "root")) {
                        // 说明该部门有父部门且父部门不是要节点
                        String parentDeptName = "";
                        // 从deptIdInstanceJObj集中将该部门父部门信息取出来
                        JSONObject parentDept =
                            deptIdInstanceJObj.getJSONObject(deptJObj.getString("parentId"));
                        if (parentDept != null) {
                            parentDeptName = "(" + parentDept.getString("name") + ")";
                            deptName += parentDeptName;
                        }
                    }
                    map.put("deptName", deptName);
                }
            }
            //缓存网格信息
            Map<String, String> tempAridMap = this.getAridInfo(areaGridBiz.getByUserIds(userIds));
            //缓存执法证
            Map<String, String> tempEnforce = new HashMap<>();
            List<EnforceCertificate> enforceList = enforceCertificateBiz.getEnforceByUserIds(userIds);
            for (EnforceCertificate enforceCertificate : enforceList) {
                tempEnforce.put(enforceCertificate.getUsrId(), enforceCertificate.getCertCode());
            }

            //封装前台结果集
            for (Map<String, Object> map : result) {
                //网格信息
                map.put("gridName", tempAridMap.get(String.valueOf(map.get("userId"))));
                map.put("certCode", tempEnforce.get(String.valueOf(map.get("userId"))));
            }
        }
        return new TableResultResponse<>(userList.getData().getTotal(), result);
    }

    /**
     * 绑定部门deptIds下的子部门
     * @param deptIds
     * @return
     */
    private String bindChildrenDeptId(String deptIds,List<JSONObject> allDeptList) {
        if(StringUtils.isBlank(deptIds)){
            return null;
        }

        Set<String> result=new HashSet<>();

        String[] split = deptIds.split(",");
        for(String deptId:split){
            // 进行递归查询，将符合条件的部门ID放入result结果集中
            bindChildrenDeptIdAssist(result,deptId,allDeptList);
        }

        if(BeanUtil.isNotEmpty(result)){
            return String.join(",", result);
        }
        return null;
    }

    private void bindChildrenDeptIdAssist(Set<String> result, String deptId, List<JSONObject> allDeptList) {
        // 先把当前部门加入到结果集
        result.add(deptId);
        for(JSONObject tmp:allDeptList){
            if(StringUtils.equals(tmp.getString("parentId"), deptId)){
                // 说明deptId是tmp的子部门
                bindChildrenDeptIdAssist(result, tmp.getString("id"), allDeptList);
            }
        }
    }

    /**
     * 通过用户id获取用户信息
     *
     * @param userId
     * @return
     */
    public Map<String, Object> userInfo(String userId) {

        Map<String, Object> resultData = new HashMap<>();
        Map<String, Object> result = iUserFeign.getUserByUserId(userId);
        if(BeanUtils.isEmpty(result)){
            return resultData;
        }
        //获取用户岗位信息
        ObjectRestResponse<List<Map<String, Object>>> positionResult = adminFeign.getPositionByUserId(userId);
        List<Map<String, Object>> positionList = positionResult.getData();
        if (BeanUtil.isNotEmpty(positionList)) {
            StringBuilder positions = new StringBuilder();
            Map<String, Object> map;
            for (int i = 0; i < positionList.size(); i++) {
                map = positionList.get(i);
                if(BeanUtils.isEmpty(map)){
                    continue;
                }
                positions.append(map.get("name")==null?"":map.get("name"));
                if(i < positionList.size()-1){
                    positions.append(",");
                }
            }
            resultData.put("positions", positions.toString());
        }


        /*
         * 为返回数据添加是否为NULL的判断，避免在页面上出现null字样
         */
        //基本信息
        resultData.put("name", result.get("name") == null ? "" : result.get("name"));

        //  部门名称
        String deptName="";
        JSONObject deptInfo=new JSONObject();

        try {
            deptInfo = JSONObject.parseObject(String.valueOf(result.get("departId")));
            deptName=deptInfo.getString("name") == null ? "" : deptInfo.getString("name");
        } catch (Exception e) {
            e.getMessage();
        }

        // 查询父级部门信息
        if (!StringUtils.equals(deptInfo.getString("parentId"),"-1")
                && !StringUtils.equals(deptInfo.getString("parentId"), "root")
        && StringUtils.isNotBlank(deptInfo.getString("parentId"))) {
            // 说明该部门有父级部门且不为根部门
            Map<String, String> parentDeptMap =
                    adminFeign.getDepart(deptInfo.getString("parentId"));
            if (BeanUtil.isNotEmpty(parentDeptMap)) {
                List<String> parentDepartJSONStr = new ArrayList<>(parentDeptMap.values());
                JSONObject parentDeptJObj = JSONObject.parseObject(parentDepartJSONStr.get(0));
                if (StringUtils.isNotBlank(parentDeptJObj.getString("name"))) {
                    deptName += "(" + parentDeptJObj.getString("name") + ")";
                }
            }
        }
        resultData.put("deptName", deptName);

        resultData.put("sex", result.get("sex") == null ? "" : result.get("sex"));
        // 个人图片
        resultData.put("image", result.get("attr1") == null ? "" : result.get("attr1"));
        resultData.put("mobilePhone",
            result.get("mobilePhone") == null ? "" : result.get("mobilePhone"));
        resultData.put("email", result.get("email") == null ? "" : result.get("email"));
        resultData.put("birthday", result.get("birthday") == null ? "" : result.get("birthday"));
        resultData.put("description",
            result.get("description") == null ? "" : result.get("description"));


        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        //执法证编码
        List<EnforceCertificate> enforceList = enforceCertificateBiz.getEnforceByUserIds(userIds);
        if(BeanUtil.isEmpty(enforceList)){
            // 如果userIds里不包含执法人员，则enforceList有可能是空或NULL---By尚
            resultData.put("certCode", "");
        }else{
            resultData.put("certCode", enforceList.get(0).getCertCode()==null?"":enforceList.get(0).getCertCode());
        }

        //网格信息
        Map<String, String> tempAridMap = this.getAridInfo(areaGridBiz.getByUserIds(userIds));
        resultData.put("gridName", tempAridMap.get(userId) == null ? "" : tempAridMap.get(userId));

        return resultData;
    }

    /**
     * 筛选用户所属网格（多个网格）
     *
     * @param userAridList 网格信息列表
     * @return userId：name-role;name-role;name-role
     */
    private Map<String, String> getAridInfo(List<Map<String, Object>> userAridList) {
        Map<String, String> dictMap = dictFeign.getByCode(Constances.ROOT_BIZ_GRID_ROLE);
        //缓存网格信息
        Map<String, String> tempAridMap = new HashMap<>();
        String gridInfo, userId, gridName, gridRole,parentGridName;
        for (Map<String, Object> map : userAridList) {
            userId = String.valueOf(map.get("userId"));
            gridInfo = tempAridMap.get(userId);
            gridName = String.valueOf(map.get("gridName"));
            gridRole =
                BeanUtils.isEmpty(dictMap) ? ""
                    : dictMap.get(String.valueOf(map.get("gridRole"))) == null ? ""
                        : dictMap.get(String.valueOf(map.get("gridRole")));
            parentGridName =
                map.get("parentGridName") == null ? "" : String.valueOf(map.get("parentGridName"));
            //一个用户可能存在多个网格信息
            if (tempAridMap.get(userId) != null) {
                if(StringUtils.isEmpty(parentGridName)){
                    gridInfo +=
                        StringUtils.isBlank(gridRole) ? ";" + gridName
                            : ";" + gridName + "_" + gridRole;
                }else{
                    gridInfo +=
                        StringUtils.isBlank(gridRole) ? ";" + gridName + "（" + parentGridName + "）"
                            : ";" + gridName + "（" + parentGridName + "）_" + gridRole;
                }
            } else {
                if(StringUtils.isEmpty(parentGridName)){
                    gridInfo = StringUtils.isBlank(gridRole) ? gridName : gridName + "_" + gridRole;
                }else{
                    gridInfo =
                        StringUtils.isBlank(gridRole) ? gridName + "（" + parentGridName + "）"
                            : gridName + "（" + parentGridName + "）_" + gridRole;
                }
            }
            tempAridMap.put(userId, gridInfo);
        }
        return tempAridMap;
    }
}
