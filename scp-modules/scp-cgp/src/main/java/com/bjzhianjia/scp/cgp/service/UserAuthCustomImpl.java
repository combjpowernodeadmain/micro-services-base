/*
 *
 * Copyright 2018 by zxbit.cn
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZXBIT ("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with ZXBIT.
 *
 */

package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.AreaGridMemberBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.feign.IUserFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.wf.base.auth.service.IWfProcUserAuthService;

/**
 * 	实现IWfProcUserAuthService接口，提供获取用户信息的实现。
 * @author chenshuai
 */
@Service
public class UserAuthCustomImpl implements IWfProcUserAuthService {

    
    @Autowired
    private IUserFeign userFeign;
    
    @Autowired
    private AreaGridMemberBiz areaGridMemberBiz;
    
    @Autowired
    private AreaGridBiz areaGridBiz;
    /**
     * 空构造函数
     */
    public UserAuthCustomImpl() {
        super();
    }

    @Override
    public String getUserId() {
        return BaseContextHandler.getUserID();
    }

    @Override
    public String getUserCode() {
        return BaseContextHandler.getUserID();
    }

    @Override
    public String getDeptId() {
        return BaseContextHandler.getDepartID();
    }
    @Override
	public String getUsername() {
    	return BaseContextHandler.getUsername();
	}
    @Override
    public String getDeptId(String userCode) {
        return userFeign.getDepartIdByUserId(userCode);
    }

    @Override
    public String getOrgId() {
        return BaseContextHandler.getDepartID();
    }

    @Override
    public String getTenantId() {
        return BaseContextHandler.getTenantID();
    }

    @Override
    public String getTenantId(String userCode) {
        return userFeign.getTenantIdByUserId(userCode);
    }

    @Override
    public String getOrgId(String userCode) {
        return userFeign.getDepartIdByUserId(userCode);
    }

    @Override
    public String getOrgCode() {
        return BaseContextHandler.getDepartID();
    }

    @Override
    public String getOrgCode(String userCode) {
        return userFeign.getDepartIdByUserId(userCode);
    }

    @Override
    public List<String> getRoleCodes() {
        return userFeign.getGroupCodesByUserId(BaseContextHandler.getUserID());
    }

    @Override
    public List<String> getRoleCodes(String userCode) {
        return userFeign.getGroupCodesByUserId(userCode);
    }

    @Override
    public List<String> getAuthOrgCodes() {
        return userFeign.getGroupCodesByUserId(BaseContextHandler.getUserID());
    }

    @Override
    public List<String> getAuthOrgCodes(String userCode) {
        return userFeign.getGroupCodesByUserId(BaseContextHandler.getUserID());
    }

    @Override
    public String getSelfPermissionData1() {
        /*
         * selfPermissionData1用于网格权限
         */
        // 查询操作人所属网格,一个人可能属于多个网格
        List<AreaGridMember> areaGridList = areaGridMemberBiz.getGridByMemId(BaseContextHandler.getUserID());
        
        List<String> areaGridIdList=new ArrayList<>();
        if(BeanUtil.isNotEmpty(areaGridList)) {
            areaGridIdList=areaGridList.stream().map(o->String.valueOf(o.getGridId())).distinct().collect(Collectors.toList());
            
            /*
             * 合并areaGridIdList里对应网格的父级网格ID
             */
            Set<String> mergeParentAreaGrid = areaGridBiz.mergeParentAreaGrid(areaGridIdList);
            
            String areaGridIdStr = "'" + String.join(",", mergeParentAreaGrid).replaceAll(",", "','")+"'";//拼接sql中替换点位符的字符串
            return areaGridIdStr;
        }
        
        /*
         *  如果当前人员不属于某一网格，则返回“-1”，而不返回空，避免产生sql错误
         *  如果返回为空，则sql语句为"AND task.PROC_SELFDATA1 IN ()",该sql片段会报错
         */
        return "'-1'";
    }

    @Override
    public String getSelfPermissionData1(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelfPermissionData2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelfPermissionData2(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelfPermissionData3() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelfPermissionData3(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getSelfPermissionData4() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelfPermissionData4(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelfPermissionData5() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSelfPermissionData5(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public List<String> getUserFlowPositions(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
