package com.bjzhianjia.scp.cgp.entity;

/**
 * 业务操作结果
 * @author zzh
 *
 * @param <T>
 */
public class Result<T> {

	private boolean isSuccess;
	
	private String message;
	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
