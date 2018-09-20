package com.bjzhianjia.scp.cgp.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.base.task.service.impl.WfProcTaskServiceImpl;
import com.github.pagehelper.PageInfo;

/**
 * @author 尚
 */
@Service
public class CaseRegistrationService {

    @Autowired
    private WfProcTaskServiceImpl wfProcTaskService;

    @Autowired
    private AdminFeign adminFeign;

    /**
     * 查询个人待办任务(工作流)
     * 在com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz.getUserToDoTasks(JSONObject)进行了处理
     * 
     * @author 尚
     * @param objs
     *            {"bizData":{}, "procData":{}, "authData":{"procAuthType":"2"},
     *            "variableData":{}, "queryData":{ "isQuery":"false" } }<br/>
     *            如果存在查询条件，则将"isQuery"设为true,并在"queryData"大项内添加查询条件
     * @return
     */
    @Deprecated
    public TableResultResponse<JSONObject> getUserToDoTasks(JSONObject objs) {

        return null;
    }

    /**
     * 查询待办详情
     * 
     * @author 尚
     * @param objs
     * @return
     */
    public JSONArray getProcApproveHistory(JSONObject objs) {
        /*
         * ==========================查询历史============================
         */
        // 查询流程历史记录
        JSONObject procDataJObj = objs.getJSONObject("procData");
        if (procDataJObj == null) {
            throw new BizException("请指定待查询流程组件");
        }
        PageInfo<WfProcTaskHistoryBean> procApprovedHistory = wfProcTaskService.getProcApprovedHistory(objs);
        List<WfProcTaskHistoryBean> historyList = procApprovedHistory.getList();
        if (BeanUtil.isEmpty(historyList)) {
            return new JSONArray();
        }

        Set<String> userIdInHistory = new HashSet<>();
        for (WfProcTaskHistoryBean wfProcTaskHistoryBean : historyList) {
            userIdInHistory.add(wfProcTaskHistoryBean.getProcTaskCommitter());
            userIdInHistory.add(wfProcTaskHistoryBean.getProcTaskAssignee());
        }

        Map<String, String> userMap = new HashMap<>();
        if (BeanUtil.isNotEmpty(userIdInHistory)) {
            userMap = adminFeign.getUser(String.join(",", userIdInHistory));
        }

        JSONArray resultJArray = JSONArray.parseArray(JSON.toJSONString(historyList));
        if (BeanUtil.isNotEmpty(userMap)) {
            for (int i = 0; i < resultJArray.size(); i++) {
                JSONObject tmpJObj = resultJArray.getJSONObject(i);
                tmpJObj.put("procTaskCommitterName", userMap.get(tmpJObj.getString("procTaskCommitter")));
                tmpJObj.put("procTaskAssigneeName", userMap.get(tmpJObj.getString("procTaskAssignee")));
            }
        }

        return resultJArray;
    }
}
