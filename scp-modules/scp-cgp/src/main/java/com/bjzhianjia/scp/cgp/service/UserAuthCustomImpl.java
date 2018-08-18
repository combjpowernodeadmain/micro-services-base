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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bjzhianjia.scp.cgp.feign.IUserFeign;
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
        // TODO Auto-generated method stub
        return null;
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
