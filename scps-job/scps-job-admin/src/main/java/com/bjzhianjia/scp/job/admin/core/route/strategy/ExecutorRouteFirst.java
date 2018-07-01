package com.bjzhianjia.scp.job.admin.core.route.strategy;

import com.bjzhianjia.scp.job.admin.core.route.ExecutorRouter;
import com.bjzhianjia.scp.job.admin.core.trigger.XxlJobTrigger;
import com.bjzhianjia.scp.job.core.biz.model.ReturnT;
import com.bjzhianjia.scp.job.core.biz.model.TriggerParam;

import java.util.ArrayList;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    public String route(int jobId, ArrayList<String> addressList) {
        return addressList.get(0);
    }

    @Override
    public ReturnT<String> routeRun(TriggerParam triggerParam, ArrayList<String> addressList) {

        // address
        String address = route(triggerParam.getJobId(), addressList);

        // run executor
        ReturnT<String> runResult = XxlJobTrigger.runExecutor(triggerParam, address);
        runResult.setContent(address);
        return runResult;
    }
}
