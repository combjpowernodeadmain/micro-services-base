/*
 * @(#) WfProcDesinerRest.java  1.0  August 29, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * SCP("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with SCP.
 */
package com.bjzhianjia.scp.security.wf.base.rest;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.wf.base.biz.ResultUtils;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.base.design.service.IWfProcDesignService;
import com.bjzhianjia.scp.security.wf.base.msg.ObjectRestResponse;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 工作流流程设计Rest服务接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.29    mayongming       1.0        1.0 Version
 * 2016.09.01    scp       1.0        新增流程部署功能
 * </pre>
 */
@Controller
@CheckUserToken
@SuppressWarnings("rawtypes")
@RequestMapping("/design")
@Api(value = "工作流流程设计Rest服务接口", tags = "流程设计")
@Slf4j
public class WfProcDesignRest {

	@Autowired
	IWfProcDesignService wfProcDesignService;

	/**
	 * 功能：部署流程
	 * 
	 * @param objs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deployementProcess")
	public ObjectRestResponse<Map> deploymentProcess(
			@RequestBody JSONObject objs) {
		log.debug("SCP信息---部署工作流...开始");
		wfProcDesignService.deploymentProcess(objs);
		log.debug("SCP信息---部署工作流...结束");
		return ResultUtils.success();
	}

	/**
	 * 根据Model部署流程
	 */
	@ResponseBody
	@RequestMapping(value = "modeldeploy")
	public ObjectRestResponse<Map> deploy(@RequestBody JSONObject objs) {
		log.debug("SCP信息---部署工作流...开始");
		wfProcDesignService.deploymentModel(objs);
		log.debug("SCP信息---部署工作流...结束");
		return ResultUtils.success();
	}

	/**
	 * 模型列表
	 */
	@RequestMapping(value = "modellist")
	@ResponseBody
	public ObjectRestResponse<PageInfo> modelList(@RequestBody JSONObject objs) {
		log.debug("SCP信息---开始分页查询数据...");
		PageInfo<WfProcActReModelBean> pageInfo = wfProcDesignService
				.selectModelList(objs);
		log.info("SCP信息---分页查询数据完成.");

		return ResultUtils.success(pageInfo);
	}

	/**
	 * 功能：流程模型删除
	 * 
	 * @author scp
	 */
	@ResponseBody
	@RequestMapping(value = "/modeldelete")
	public ObjectRestResponse<Map> modeldelete(@RequestBody JSONObject objs) {
		log.debug("SCP信息---删除模型...开始");
		wfProcDesignService.modeldelete(objs);
		log.debug("SCP信息---删除模型...结束");
		return ResultUtils.success();
	}

	/**
	 * 功能：部署流程删除
	 * 
	 * @author scp
	 */
	@ResponseBody
	@RequestMapping(value = "/wfdel")
	public ObjectRestResponse<Map> wfdel(@RequestBody JSONObject objs) {
		log.debug("SCP信息---删除流程...开始");
		wfProcDesignService.wfdel(objs);
		log.debug("SCP信息---删除流程...结束");
		return ResultUtils.success();
	}

	/**
	 * 功能：部署流程挂起
	 * 
	 * @author scp
	 */
	@ResponseBody
	@RequestMapping(value = "/wfsuspend")
	public ObjectRestResponse<Map> wfsuspend(@RequestBody JSONObject objs) {
		log.debug("SCP信息---挂起流程...开始");
		wfProcDesignService.wfsuspend(objs);
		log.debug("SCP信息---挂起流程...结束");
		return ResultUtils.success();
	}

	/**
	 * 功能：部署流程恢复
	 * 
	 * @author scp
	 */
	@ResponseBody
	@RequestMapping(value = "/wfactive")
	public ObjectRestResponse<Map> wfactive(@RequestBody JSONObject objs) {
		log.debug("SCP信息---恢复流程...开始");
		wfProcDesignService.wfactive(objs);
		log.debug("SCP信息---恢复流程...结束");
		return ResultUtils.success();
	}

	/**
	 * 流程定义列表
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/processlist")
	public ObjectRestResponse processList(@RequestBody JSONObject objs) {
		log.debug("SCP信息---开始分页查询数据...");
		PageInfo<WfProcActReProcdefBean> pageInfo = wfProcDesignService
				.processList(objs);
		log.info("SCP信息---分页查询数据完成.");
		LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
		linkedHashMap.put("result", pageInfo);
		return ResultUtils.success(pageInfo);
	}

	/**
	 * 功能：获取流程图片
	 * 
	 * @author scp
	 */
	@RequestMapping(value = "/openimage")
	public void loadByDeployment(@RequestParam("procDefId") String procDefId,
			HttpServletResponse response) throws Exception {
		InputStream imageStream = null;
		byte[] b = new byte[1024];
		int len = -1;

		try {
			imageStream = wfProcDesignService.openImageByProcessDefinition(procDefId);

			while ((len = imageStream.read(b, 0, 1024)) != -1) {
				response.setContentType("image/png");
				response.getOutputStream().write(b, 0, len);
			}

			response.flushBuffer();
		} catch (Exception e) {
			log.error("获取流程图片失败", e);
		} finally {
			close(imageStream);
		}
	}

	/**
	 * 功能：获取流程高亮显示当前节点图片
	 * @param executionId 流程实例ID
	 * 
	 * @author scp
	 */
	@RequestMapping(value = "/openimage2")
	public void readResource(@RequestParam("executionId") String executionId,
			HttpServletResponse response) throws Exception {
		InputStream imageStream = null;
		byte[] b = new byte[1024];
		int len;
		
		try {
			imageStream = wfProcDesignService.openImageByProcessInstance(executionId);
			
			// 输出资源内容到相应对象
			while ((len = imageStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
			response.flushBuffer();
		} catch (Exception e) {
			log.error("获取流程图片失败", e);
		} finally {
			close(imageStream);
		}
	}

	private void close(InputStream stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (Exception e) {
			log.error("关闭文件输出流错误", e);
		} finally {
			stream = null;
		}
	}
	
	/**
	 * 功能：导出xml
	 * 
	 * @author scp
	 */
	@ResponseBody
	@RequestMapping(value = "/export")
	public ObjectRestResponse<Map> export(@RequestBody JSONObject objs) {
		log.debug("SCP信息---导出模型...开始");
		wfProcDesignService.export(objs);
		log.debug("SCP信息---导出模型...结束");
		return ResultUtils.success();
	}
}
