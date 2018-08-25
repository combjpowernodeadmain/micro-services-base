package com.bjzhianjia.scp.security.wf.base.auth.biz;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.base.auth.entity.WfProcTokenBean;
import com.bjzhianjia.scp.security.wf.base.auth.mapper.WfProcTokenMapper;

@Service
public class WfProcTokenBiz {
	private static Map<String, WfProcTokenBean> hmWfProcTokenBean = new HashMap<String, WfProcTokenBean>();
	
	@Autowired
	private WfProcTokenMapper mapper;
	
	/**
     * 检查token认证是否合法
     * @param tokenUser
     * @param tokenPass
     * @return
     */
	public boolean isMatched(String tenantId, String tokenUser, String tokenPass) {
		WfProcTokenBean wfProcTokenBean = getWfProcToken(tenantId);
		if (wfProcTokenBean != null && StringUtils.isNotEmpty(tokenUser) && StringUtils.isNotEmpty(tokenPass)) {
			return tokenUser.equals(wfProcTokenBean.getProcTokenUser())
					&& tokenPass.equals(wfProcTokenBean.getProcTokenPass());
		} else {
			return false;
		}
	}

    
    private synchronized WfProcTokenBean getWfProcToken(String tenantId) {
    	if (hmWfProcTokenBean.containsKey(tenantId)) {
    		return hmWfProcTokenBean.get(tenantId);
    	}
		// 从数据库中取记录，然后缓存到map中，如果数据库也不存在则返回null
	    WfProcTokenBean procToken = mapper.selectByTenantId(tenantId);
	    if(procToken == null) {
	        return null;
	    }
	    hmWfProcTokenBean.put(tenantId, procToken);
	    return procToken;
	
    }    
}
