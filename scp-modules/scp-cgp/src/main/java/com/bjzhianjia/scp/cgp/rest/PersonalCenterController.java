package com.bjzhianjia.scp.cgp.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.wf.base.monitor.biz.WfMonitorBiz;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * PersonalCenterController 个人中心控制器.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月17日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("personalCenter")
@CheckClientToken
@CheckUserToken
@Api(tags = "个人中心管理")
public class PersonalCenterController {

    @Autowired
    private WfMonitorBiz wfMonitorBiz;

    /**
     * 获取当前用户任务统计
     * 
     * @return
     */
    @ApiOperation("获取当前用户任务统计")
    @GetMapping("/statis/userTask")
    @ResponseBody
    public ObjectRestResponse<List<Map<String, Object>>> getUserStatisTask() {
        ObjectRestResponse<List<Map<String, Object>>> result = new ObjectRestResponse<>();
        // 流程定义代码
        String lawEnforcementProcess = "LawEnforcementProcess"; // 案件流程
        String comprehensiveManage = "comprehensiveManage";// 事件流程

        JSONObject objs = new JSONObject();
        objs.put("procTaskAssignee", BaseContextHandler.getUserID());
        List<Map<String, Object>> data = wfMonitorBiz.getUserStatisTask(objs);

        List<Map<String, Object>> resultData = null;

        if (BeanUtil.isNotEmpty(data)) {
            resultData = new ArrayList<>();
            
            Map<String, Map<String, Object>> maps = new HashMap<>();
            for(Map<String , Object> mapData : data) {
                maps.put(String.valueOf(mapData.get("cmonth")), mapData);
            }
            
            Map<String, Object> tempMap = null;
            //月份
            String month = "";
            //当月有数据则设置
            for (int i = 0; i < 12; i++) {
                month = String.valueOf(i+1);
                tempMap = maps.get(month);
                if(tempMap != null) {
                    Integer lawCount = Integer.valueOf(String.valueOf(tempMap.get(lawEnforcementProcess)));
                    Integer comprehensiveCount = Integer.valueOf(String.valueOf(tempMap.get(comprehensiveManage)));
                    tempMap.put("total", lawCount + comprehensiveCount);
                }else {
                    tempMap = new HashMap<>();
                    tempMap.put("cmonth", month);
                    tempMap.put(comprehensiveManage, 0);
                    tempMap.put(lawEnforcementProcess, 0);
                    tempMap.put("total", 0);
                }
                resultData.add(tempMap);
            }
            
            result.setData(resultData);
        }
        return result;
    }
}
