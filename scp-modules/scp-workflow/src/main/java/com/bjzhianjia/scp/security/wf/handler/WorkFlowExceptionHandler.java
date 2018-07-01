/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.bjzhianjia.scp.security.wf.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjzhianjia.scp.security.common.msg.BaseResponse;
import com.bjzhianjia.scp.security.wf.constant.EnumResultTemplate;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;

/**
 * 全局异常拦截处理器
 * @author scp
 * @version 1.0
 */
@ControllerAdvice("com.bjzhianjia.scp.security.wf")
@Order(1)
@ResponseBody
public class WorkFlowExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(WorkFlowExceptionHandler.class);

    
    /**
     * 工作流异常处理
     * 
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(WorkflowException.class)
    public BaseResponse businessExceptionHandler(HttpServletResponse response, WorkflowException ex) {
    	response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

    	BaseResponse resInfo = new BaseResponse();
		resInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	
		logger.error("SCP ERROR MESSAGE：{}", ex);
		
    	EnumResultTemplate returnInfo = ex.getRetInfo();
    	if (returnInfo != null) {
    		logger.error("SCP错误信息：{} ;* {} ", returnInfo.getRetFactInfo(), returnInfo);
    		
    		Map<String, Object> result = new HashMap<>();	
    		result.put("errorType", 2);// 发生业务异常 - 有提示信息
    		result.put("errorCode", returnInfo.getRetCode());// 响应码
    		result.put("errorMessage", returnInfo.getRetFactInfo());// 实际异常信息
    		resInfo.setMessage(returnInfo.getRetUserInfo()); // 用户响应信息
    		resInfo.setMetadata(result);
    		
    	}
		
    	
    	return resInfo;
    }
    

    @ExceptionHandler(Exception.class)
    public BaseResponse otherExceptionHandler(HttpServletResponse response, Exception ex) {
    	response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	
        logger.error("SCP错误信息：{} ;* {}", WorkflowEnumResults.WF_COMM_02000001.getRetFactInfo(), ex);


    	BaseResponse resInfo = new BaseResponse();
		resInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	
	    Map<String, Object> result = new HashMap<>();	
		result.put("errorType", 3);// 发生业务异常 - 有提示信息
		result.put("errorCode", WorkflowEnumResults.WF_COMM_02000001.getRetCode());// 响应码
		result.put("errorMessage", WorkflowEnumResults.WF_COMM_02000001.getRetFactInfo());// 实际异常信息
		resInfo.setMessage(WorkflowEnumResults.WF_COMM_02000001.getRetUserInfo()); // 用户响应信息
		
		resInfo.setMetadata(result);
    	
    	return resInfo;
    }

}
