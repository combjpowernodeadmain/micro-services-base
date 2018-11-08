package com.bjzhianjia.scp.cgp.service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.EnforceCertificateBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.feign.IUserFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private EnforceCertificateBiz enforceCertificateBiz;


    public TableResultResponse<Map<String, Object>> phoneList(String userName, String deptIds, Integer page, Integer limit) {
        TableResultResponse<Map<String, Object>> userList = iUserFeign.phoneList(userName, deptIds, page, limit);
        List<Map<String, Object>> result = userList.getData().getRows();
        if (BeanUtil.isNotEmpty(result)) {
            //缓存用户ids集
            List<String> userIds = new ArrayList<>();
            for (Map<String, Object> map : result) {
                userIds.add(String.valueOf(map.get("userId")));
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
                map.put("gridName", tempAridMap.get(map.get("userId")));
                map.put("certCode", tempEnforce.get(map.get("userId")));
            }
        }
        return new TableResultResponse<>(userList.getData().getTotal(), result);
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
        //基本信息
        resultData.put("name", result.get("name"));
        JSONObject deptInfo = JSONObject.parseObject(String.valueOf(result.get("departId")));
        resultData.put("deptName", deptInfo.getString("name"));
        resultData.put("sex", result.get("sex"));
        //个人图片
        resultData.put("image", result.get("attr1"));
        resultData.put("mobilePhone", result.get("mobilePhone"));
        resultData.put("email", result.get("email"));
        resultData.put("birthday", result.get("birthday"));
        resultData.put("description", result.get("description"));


        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        //执法证编码
        List<EnforceCertificate> enforceList = enforceCertificateBiz.getEnforceByUserIds(userIds);
        resultData.put("certCode", enforceList.get(0).getCertCode());

        //网格信息
        Map<String, String> tempAridMap = this.getAridInfo(areaGridBiz.getByUserIds(userIds));
        resultData.put("gridName", tempAridMap.get(userId));

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
        String gridInfo, userId, gridName, gridRole;
        for (Map<String, Object> map : userAridList) {
            userId = String.valueOf(map.get("userId"));
            gridInfo = tempAridMap.get(userId);
            gridName = String.valueOf(map.get("gridName"));
            gridRole = BeanUtils.isEmpty(dictMap) ? "" : dictMap.get(String.valueOf(map.get("gridRole")));
            //一个用户可能存在多个网格信息
            if (tempAridMap.get(userId) != null) {
                gridInfo += ";" + gridName + "-" + gridRole;
            } else {
                gridInfo = gridName + "-" + gridRole;
            }
            tempAridMap.put(userId, gridInfo);
        }
        return tempAridMap;
    }
}
