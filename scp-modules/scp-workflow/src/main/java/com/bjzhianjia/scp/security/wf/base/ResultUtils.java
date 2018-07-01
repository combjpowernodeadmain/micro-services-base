package com.bjzhianjia.scp.security.wf.base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.util.Pagination;
import com.bjzhianjia.scp.security.wf.constant.BaseEnumResults;
import com.bjzhianjia.scp.security.wf.constant.EnumResultTemplate;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 服务基类包括统一返回信息处理等
 *
 */
public class ResultUtils<T> {
	
	private static int mapInitialCapacity = 6;
	
	/**
	 * 业务错误编码
	 */
	private static final int ERROR_CODE_FOR_BUSINESS = 2;
	
	/**
	 * 系统错误代码
	 */
	private static final int ERROR_CODE_FOR_SYSTEM = 3;
	
	/**
	 * 
	 * @param returnInfo-响应信息
	 * @param args
	 * @return 统一返回成功信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectRestResponse<Map> success(EnumResultTemplate returnInfo, String... args) {
		ObjectRestResponse<Map> result = new ObjectRestResponse<>();
		result.setStatus(HttpStatus.OK.value());
		result.setMessage(returnInfo.getRetUserInfo());
		
		Map successInfo = new LinkedHashMap(mapInitialCapacity);
		successInfo.put("code", returnInfo.getRetCode());// 响应码
		successInfo.put("detailInfo", replaceStr(returnInfo.getRetFactInfo(), args));// 实际异常信息
		
		return result; 
	}
	
	/**
	 * 
	 * Description: 文字置换
	 * @param
	 * @return String
	 * @throws
	 * @Author scp
	 * Create Date: 2016年8月15日 下午4:34:09
	 */
	private static String replaceStr(String s, String... args){
	    if(args.length == 0 || s == null){
	        return s;
	    }
	    for(int i = 0 ; i < args.length ; i++){
	        s = s.replaceFirst("#$%", args[i]);
	    }
	    return s;
	    
	}
	
	/**
	 * 
	 * @param linkedHashMap-响应信息
	 * @return 统一返回成功信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectRestResponse<Map> success() {
		
		ObjectRestResponse<Map> result = new ObjectRestResponse<>();
		result.setStatus(HttpStatus.OK.value());
		result.setMessage(BaseEnumResults.BASE00000000.getRetUserInfo());
		
		Map successInfo = new LinkedHashMap(mapInitialCapacity);
		successInfo.put("code", BaseEnumResults.BASE00000000.getRetCode());// 响应码
		successInfo.put("detailInfo", BaseEnumResults.BASE00000000.getRetFactInfo());// 实际异常信息
		
		return result;
	}
	
	/**
	 * 
	 * @param linkedHashMap-响应信息
	 * @return 统一返回成功信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectRestResponse success(Object resultInfo) {
		
		ObjectRestResponse<Object> result = new ObjectRestResponse<>();
		result.setStatus(HttpStatus.OK.value());
		result.setMessage(BaseEnumResults.BASE00000000.getRetUserInfo());
		
		Map successInfo = new LinkedHashMap(mapInitialCapacity);
		successInfo.put("code", BaseEnumResults.BASE00000000.getRetCode());// 响应码
		successInfo.put("detailInfo", BaseEnumResults.BASE00000000.getRetFactInfo());// 实际异常信息
		result.setData(resultInfo);
		
		return result;
	}

	/**
	 * 
	 * @param linkedHashMap-响应信息
	 * @return 统一返回成功信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectRestResponse<Map> success(Map resultInfo) {
		
		ObjectRestResponse<Map> result = new ObjectRestResponse<>();
		result.setStatus(HttpStatus.OK.value());
		result.setMessage(BaseEnumResults.BASE00000000.getRetUserInfo());
		
		Map successInfo = new LinkedHashMap(mapInitialCapacity);
		successInfo.put("code", BaseEnumResults.BASE00000000.getRetCode());// 响应码
		successInfo.put("detailInfo", BaseEnumResults.BASE00000000.getRetFactInfo());// 实际异常信息
		result.setData(resultInfo);
		
		return result;
	}
 
	public static ObjectRestResponse<Pagination> success(Pagination pagination) {
		ObjectRestResponse<Pagination> result = new ObjectRestResponse<>();
		result.setStatus(HttpStatus.OK.value());
		result.setMessage(BaseEnumResults.BASE00000000.getRetUserInfo());
		result.data(pagination);

		return result;
	}
	 
	@SuppressWarnings("rawtypes")
	public static ObjectRestResponse<PageInfo> success(PageInfo pagination) {
		ObjectRestResponse<PageInfo> result = new ObjectRestResponse<>();
		result.setStatus(HttpStatus.OK.value());
		result.setMessage(BaseEnumResults.BASE00000000.getRetUserInfo());
		result.data(pagination);

		return result;
	}
	
	/**
	 * 
	 * @param linkedHashMap-响应信息
	 * @return 统一处理失败信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectRestResponse<Map> fail(Exception e) {
		Map result = new LinkedHashMap(mapInitialCapacity);
		ObjectRestResponse<Map> response = new ObjectRestResponse<>();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		// 如果是自定义异常
		if (e instanceof WorkflowException) {
		    EnumResultTemplate returnInfo = ((WorkflowException) e).getRetInfo();
		    	
			result.put("errorType", ERROR_CODE_FOR_BUSINESS);// 发生业务异常 - 有提示信息
			result.put("errorCode", returnInfo.getRetCode());// 响应码
			result.put("errorMessage", returnInfo.getRetFactInfo());// 实际异常信息
			
			response.setMessage(returnInfo.getRetUserInfo()); // 用户响应信息
			response.setMetadata(result);
		} else {
			result.put("errorType", ERROR_CODE_FOR_SYSTEM);// 发生业务异常 - 有提示信息
			result.put("errorCode", BaseEnumResults.BASE00000001.getRetCode());// 响应码
			result.put("errorMessage", BaseEnumResults.BASE00000001.getRetFactInfo());// 实际异常信息
			
			response.setMessage(BaseEnumResults.BASE00000001.getRetUserInfo()); // 用户响应信息
			response.setMetadata(result);
		}


		return response;
	}
    
	/**
	 * 处理验证信息
	 * @param br
	 * @throws Exception 
	 */
//	public void validateIPInfo(BindingResult br) throws IqbException {
//		if (br.hasFieldErrors()) { // 判断验证是否出错
//			List<FieldError> fes = br.getFieldErrors();
//			for (FieldError fe : fes) {
//				// 记录日志
//				String erroInfo = new StringBuffer("IQB异常信息---对象：")
//						.append(fe.getObjectName()).append("属性 : ")
//						.append(fe.getField()).append(" :")
//						.append(fe.getDefaultMessage()).toString();
//				logger.error(erroInfo);
//				throw new IqbException(erroInfo);
//			}
//		}
//	}
	
	/**
	 * 获取 UUID 32位
	 * @return
	 */
    public String getUUID(){
		
		UUID uid =  UUID.randomUUID();
		return uid.toString().replace("-", "");		
	}
}