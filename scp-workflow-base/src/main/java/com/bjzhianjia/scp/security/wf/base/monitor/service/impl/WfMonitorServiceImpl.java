/*
 * @(#) WfMonitorServiceImpl.java  1.0  August 29, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * IQB("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with IQB.
 */
package com.bjzhianjia.scp.security.wf.base.monitor.service.impl;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.auth.biz.WfProcUserAuthBiz;
import com.bjzhianjia.scp.security.wf.base.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.base.constant.Constants.WfRequestDataTypeAttr;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.monitor.biz.WfMonitorBiz;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfMyProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.service.IWfMonitorService;
import com.bjzhianjia.scp.security.wf.base.utils.DatePattern;
import com.bjzhianjia.scp.security.wf.base.utils.DateTools;
import com.bjzhianjia.scp.security.wf.base.utils.DateUtil;
import com.bjzhianjia.scp.security.wf.base.utils.JSONUtil;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;
import com.bjzhianjia.scp.security.wf.base.vo.WfProcAuthDataBean;
import com.bjzhianjia.scp.security.wf.base.vo.WfProcBizDataBean;
import com.github.pagehelper.PageInfo;

/**
 * Description: 工作流监控服务接口实现类
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.29    mayongming       1.0        1.0 Version
 * </pre>
 */
@Service("wfMonitorService")
public class WfMonitorServiceImpl implements IWfMonitorService {
    @Autowired
    WfMonitorBiz wfMonitorBiz;
    @Autowired
    WfProcUserAuthBiz wfProcUserAuthBiz; 

    /**
     * 查询用户已办流程任务列表
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public PageInfo<WfMyProcBackBean> getUserDoneTasks(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
    	JSONObject queryObj = parseQueryData(authData, bizData);
    	
        parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procFinishedTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procFinishedTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);

        return new PageInfo<WfMyProcBackBean>(wfMonitorBiz.getUserDoneTasks(queryObj));
    }

    /**
     * 查询用户已办流程任务数量
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public int getUserDoneTaskCount(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
    	JSONObject queryObj = parseQueryData(authData, bizData);

        parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procFinishedTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procFinishedTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);

        return wfMonitorBiz.getUserDoneTaskCount(queryObj);
    }
    
    /**
     * 查询用户待办流程任务列表
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public PageInfo<WfProcBackBean> getUserToDoTasks(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, true);
    	JSONObject queryObj = parseQueryData(authData, bizData);

    	parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        
        List<WfProcBackBean> list = wfMonitorBiz.getUserToDoTasks(queryObj);
        return new PageInfo<WfProcBackBean>(list);
    }
    
    /**
     * 查询用户待办流程任务数量
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public int getUserToDoTaskCount(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, true);
    	JSONObject queryObj = parseQueryData(authData, bizData);

    	parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        
        return wfMonitorBiz.getUserToDoTaskCount(queryObj);
    }
    /**
     * 查询所有待办流程任务列表
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public PageInfo<WfProcBackBean> getAllToDoTasks(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, true);
    	JSONObject queryObj = parseQueryData(authData, bizData);

    	parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        
        List<WfProcBackBean> list = wfMonitorBiz.getAllToDoTasks(queryObj);
        return new PageInfo<WfProcBackBean>(list);
    }
    /**
     * 查询流程任务列表
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public PageInfo<WfProcBackBean> getAllTasks(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, true);
    	JSONObject queryObj = parseQueryData(authData, bizData);

    	parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procTaskCommitTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        
        List<WfProcBackBean> list = wfMonitorBiz.getAllTasks(queryObj);
        return new PageInfo<WfProcBackBean>(list);
    }
    
    /**
     * 流程监控查询
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public PageInfo<WfMyProcBackBean> getActiveProcessList(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
    	JSONObject queryObj = parseQueryData(authData, bizData);

        parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procCommitTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCommitTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);

        return new PageInfo<WfMyProcBackBean>(wfMonitorBiz.getActiveProcessList(queryObj));
    }
    
    /**
     * 查询用户待办流程任务列表
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public PageInfo<WfMyProcBackBean> getOrgProcessList(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
    	JSONObject queryObj = parseQueryData(authData, bizData);

        parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procCommitTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCommitTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);

        return new PageInfo<WfMyProcBackBean>(wfMonitorBiz.getOrgProcessList(queryObj));
    }
    
    /**
     * 流程实例汇总查询
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public PageInfo<WfMyProcBackBean> geyProcessSummary(JSONObject objs) throws WorkflowException {
    	WfProcAuthDataBean authData = parseAuthData(objs);
    	WfProcBizDataBean bizData = parseBizData(objs);
    	wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
    	JSONObject queryObj = parseQueryData(authData, bizData);

        parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procFinishedTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procFinishedTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        
        return new PageInfo<WfMyProcBackBean>(wfMonitorBiz.geyProcessSummary(queryObj));
    }
    
    /**
     * 流程委托查询
     * @param objs
     * @return
     * @throws WorkflowException
     */
	public PageInfo<WfMyProcBackBean> getProcessDelegateList(JSONObject objs)
			throws WorkflowException {
		WfProcAuthDataBean authData = parseAuthData(objs);
		WfProcBizDataBean bizData = parseBizData(objs);
		wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
		JSONObject queryObj = parseQueryData(authData, bizData);

        parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
        parseAndUpdateDate(queryObj, "procFinishedTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
        parseAndUpdateDate(queryObj, "procFinishedTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);

        return new PageInfo<WfMyProcBackBean>(wfMonitorBiz.getProcessDelegateList(queryObj));
    }
    
    /**
     * 获取指定用户的流程委托列表
     * @param objs
     * @return
     * @throws WorkflowException
     */
	public PageInfo<WfMyProcBackBean> getMyProcDelegateList(JSONObject objs)
			throws WorkflowException {
		WfProcAuthDataBean authData = parseAuthData(objs);
		wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
		JSONObject queryObj = parseQueryData(authData, null);

		return new PageInfo<WfMyProcBackBean>(wfMonitorBiz.getMyProcDelegateList(queryObj));
	}
    
    /**
     * 根据指定格式解析时间字符串，并转化成int类型更新对应数据
     * @param objs
     * @param key     
     * @param type
     * @throws WorkflowException
     */
    private void parseAndUpdateDate(JSONObject objs, String key, int type) throws WorkflowException {
        if (objs.containsKey(key) && !StringUtil.isNull(objs.getString(key))) {
            String value = objs.getString(key);
            objs.put(key, DateUtil.getTime(value, DatePattern.DATEPARTTEN_YYYYMMDD_TYPE3, type));
        }
    }
    
	/**
     * 解析流程认证数据
     * @param objs  请求Json数据
     * @return
     * @throws WorkflowException
     */
    private WfProcAuthDataBean parseAuthData(JSONObject objs) throws WorkflowException {
        WfProcAuthDataBean authData = null;
        
        if (objs.containsKey(WfRequestDataTypeAttr.PROC_AUTHDATA)) {
            authData =
                JSONUtil.toJavaObject(objs.getJSONObject(WfRequestDataTypeAttr.PROC_AUTHDATA),
                    WfProcAuthDataBean.class);
        }
        
        if (authData == null || authData.isEmpty()) {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020000);
        }
        
        return authData;
    }
    
    /**
     * 解析流程查询条件数据
     * @param objs  请求Json数据
     * @return
     * @throws WorkflowException
     */
    private WfProcBizDataBean parseBizData(JSONObject objs) throws WorkflowException {
        if (objs.containsKey(WfRequestDataTypeAttr.PROC_BIZDATA)) {
            WfProcBizDataBean bizData = JSONUtil.toJavaObject(objs, WfProcBizDataBean.class);
            return (bizData == null || bizData.isEmpty()) ? new WfProcBizDataBean() : bizData;
        } else {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020000);
        }
    }  
    
    /**
     * 解析流程业务数据
     * @param authData  用户认证数据
     * @param bizData	用户查询数据
     * @return
     * @throws WorkflowException
     */
	private JSONObject parseQueryData(WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException {
		JSONObject queryObj = new JSONObject();
		
		if (bizData != null) {
			Iterator<String> iterBiz = bizData.getBizData().keySet().iterator();
			while (iterBiz.hasNext()) {
				String key  = iterBiz.next();
				queryObj.put(key, bizData.getBizData(key));
			}
		}
		
		if (authData != null) {
			Iterator<String> iterAuth = authData.getAuthData().keySet().iterator();
			while (iterAuth.hasNext()) {
				String key  = iterAuth.next();
				queryObj.put(key, authData.getAuthData(key));
			}
		}
		
		return queryObj;
	}
    
    /**
     * 导出流程汇总查询数据
     * @param params
     * @return  
     * @throws Exception 
     * Create Date: 2017年11月22日
     */
    @Override
    public String exportProcessSummary(JSONObject objs, HttpServletResponse response) {
        try {
        	WfProcAuthDataBean authData = parseAuthData(objs);
        	WfProcBizDataBean bizData = parseBizData(objs);
        	wfProcUserAuthBiz.userAuthenticate(authData, false, false, false);
        	JSONObject queryObj = parseQueryData(authData, bizData);

            parseAndUpdateDate(queryObj, "procCreateTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
            parseAndUpdateDate(queryObj, "procCreateTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);
            parseAndUpdateDate(queryObj, "procFinishedTimeStart", DateUtil.DATECONVERTYPE_DATESTART);
            parseAndUpdateDate(queryObj, "procFinishedTimeEnd", DateUtil.DATECONVERTYPE_DATEEND);

            // 获取流程实例汇总列表
            List<WfMyProcBackBean> procList = wfMonitorBiz.getProcessSummary(queryObj);
              
            // 2.导出excel表格
            HSSFWorkbook workbook = this.exportShouldDebtDetailList(procList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=ProcessSummary-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010104);
        }
        
        return "success";
    }
    
    // 导出
    private HSSFWorkbook exportShouldDebtDetailList(List<WfMyProcBackBean> list) {
        String[] excelHeader =
        {"序号", "流程名称", "业务ID", "流程创建时间", "流程完成时间", "流程摘要", "任务名称", "提交人", "处理人/角色",
                "受托人", "任务到达时间", "任务受理时间", "流程状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("流程汇总查询页");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            WfMyProcBackBean sdd = list.get(i);
            row.createCell(0).setCellValue(i + 1);// 序号
            row.createCell(1).setCellValue(sdd.getProcName());// 流程名称
            row.createCell(2).setCellValue(sdd.getProcBizId());// 业务id
            row.createCell(3).setCellValue(sdd.getProcCreatetime());// 手流程创建时间
            row.createCell(4).setCellValue(sdd.getProcEndtime()!=null ? sdd.getProcEndtime():"");// 流程完成时间
            row.createCell(5).setCellValue(sdd.getProcMemo()!=null ? sdd.getProcMemo():"");// 流程摘要
            row.createCell(6).setCellValue(sdd.getProcTaskname()!=null ? sdd.getProcTaskname():"");// 任务名称
            row.createCell(7).setCellValue(sdd.getProcTaskCommitter()!=null ? sdd.getProcTaskCommitter():"");// 提交人
            row.createCell(8).setCellValue(sdd.getProcTaskAssignee()!=null ? sdd.getProcTaskAssignee():"");// 处理人/角色
            row.createCell(9).setCellValue(sdd.getProcLicensor()!=null ? sdd.getProcLicensor():"");// 受托人
            row.createCell(10).setCellValue(sdd.getProcTaskCommittime()!=null ? sdd.getProcTaskCommittime():"");// 任务到达时间
            row.createCell(11).setCellValue(sdd.getProcTaskAssigntime() !=null ?sdd.getProcTaskAssigntime():""); // 任务受理时间
            row.createCell(12).setCellValue(sdd.getProcStatusName() !=null ? sdd.getProcStatusName():"");// 流程状态
        }

        // 设置列宽
        for (int j = 0; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }

        return wb;
    }
}
