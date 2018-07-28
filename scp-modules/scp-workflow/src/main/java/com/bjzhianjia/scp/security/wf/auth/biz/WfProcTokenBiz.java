package com.bjzhianjia.scp.security.wf.auth.biz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.auth.entity.WfProcTokenBean;

@Service
public class WfProcTokenBiz {
	private static Map<String, WfProcTokenBean> hmWfProcTokenBean = new HashMap<String, WfProcTokenBean>();
	
	/**
     * 检查token认证是否合法
     * @param tokenUser
     * @param tokenPass
     * @return
     */
	public boolean isMatched(String tenantId, String tokenUser, String tokenPass) {
		WfProcTokenBean wfProcTokenBean = getWfProcToken(tenantId);
		if (wfProcTokenBean != null && tokenUser != null && tokenPass != null) {
			return tokenUser.equals(wfProcTokenBean.getProcTokenUser())
					&& tokenPass.equals(wfProcTokenBean.getProcTokenPass());
		} else {
			return false;
		}
	}

    
    private synchronized WfProcTokenBean getWfProcToken(String tenantId) {
    	if (hmWfProcTokenBean.containsKey(tenantId)) {
    		return hmWfProcTokenBean.get(tenantId);
    	} else {
    		// 从数据库中取记录，然后缓存到map中，如果数据库也不存在则返回null
    		return null;
    	}
    }    
}
