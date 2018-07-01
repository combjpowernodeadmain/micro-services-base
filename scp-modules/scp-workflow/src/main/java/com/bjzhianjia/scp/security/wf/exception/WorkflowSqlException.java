package com.bjzhianjia.scp.security.wf.exception;

import com.bjzhianjia.scp.security.wf.constant.EnumResultTemplate;

public class WorkflowSqlException extends WorkflowException {
	private static final long serialVersionUID = -6251451850593932226L;
	
	public WorkflowSqlException(EnumResultTemplate retInfo) {
		super(retInfo.getReturnCodeInfoByCode(retInfo));
		retInfo = retInfo.getReturnCodeInfoByCode(retInfo);
	}

	public WorkflowSqlException(EnumResultTemplate retInfo, Throwable throwable) {
		super(retInfo.getReturnCodeInfoByCode(retInfo));
		this.throwable = throwable;
		retInfo = retInfo.getReturnCodeInfoByCode(retInfo);
	}
}