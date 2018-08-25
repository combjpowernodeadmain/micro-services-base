package com.bjzhianjia.scp.security.wf.base.exception;

import com.bjzhianjia.scp.security.wf.base.constant.EnumResultTemplate;

/**
 * 异常类处理基类
 */
public class WorkflowException extends RuntimeException {
	/** default sid **/
	private static final long serialVersionUID = -3316241439076307461L;

	/** 返回代码枚举类型 **/
	protected EnumResultTemplate retInfo;

	/** 异常堆栈 */
	protected Throwable throwable;

	public WorkflowException(String message) {
		super(message);
	}

	public EnumResultTemplate getRetInfo() {
		return retInfo;
	}

	public void setRetInfo(EnumResultTemplate retInfo) {
		this.retInfo = retInfo;
	}

	public WorkflowException(EnumResultTemplate returnInfo) {
		super(returnInfo.getReturnCodeInfoByCode(returnInfo).toString());
		retInfo = returnInfo.getReturnCodeInfoByCode(returnInfo);
	}

	public WorkflowException(EnumResultTemplate returnInfo, Throwable throwable) {
		super(returnInfo.getReturnCodeInfoByCode(returnInfo).toString());
		this.throwable = throwable;
		retInfo = returnInfo.getReturnCodeInfoByCode(returnInfo);
	}
	
	
	
	
}