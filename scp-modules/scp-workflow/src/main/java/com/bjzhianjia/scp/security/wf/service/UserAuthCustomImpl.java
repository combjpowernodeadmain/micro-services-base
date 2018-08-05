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
package com.bjzhianjia.scp.security.wf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService;
import com.bjzhianjia.scp.security.wf.feign.IDepartFeign;
import com.bjzhianjia.scp.security.wf.feign.IUserFeign;


/**
 * UserAuthCustomImpl 类描述.
 * 
 * 实现IWfProcUserAuthService接口，提供获取用户信息的实现。
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * Jul 29, 2018          ric_w      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @date Jul 29, 2018
 * @author ric_w
 *
 */
@Service
public class UserAuthCustomImpl implements IWfProcUserAuthService {

    @Autowired
    private IDepartFeign departFeign;
    
    @Autowired
    private IUserFeign userFeign;
    /**
     * 空构造函数
     */
    public UserAuthCustomImpl() {
        super();
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getUserId()
     */
    @Override
    public String getUserId() {
        return BaseContextHandler.getUserID();
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getUserCode()
     */
    @Override
    public String getUserCode() {
        return BaseContextHandler.getUserID();
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getDeptId()
     */
    @Override
    public String getDeptId() {
        return BaseContextHandler.getDepartID();
    }

    /* userCode = username
     * 
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getDeptId(java.lang.String)
     */
    @Override
    public String getDeptId(String userCode) {
        return userFeign.getDepartIdByUserId(userCode);
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getOrgId()
     */
    @Override
    public String getOrgId() {
        return BaseContextHandler.getDepartID();
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getTenantId()
     */
    @Override
    public String getTenantId() {
        return BaseContextHandler.getTenantID();
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getTenantId(java.lang.String)
     */
    @Override
    public String getTenantId(String userCode) {
        return userFeign.getTenantIdByUserId(userCode);
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getOrgId(java.lang.String)
     */
    @Override
    public String getOrgId(String userCode) {
        return userFeign.getDepartIdByUserId(userCode);
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getOrgCode()
     */
    @Override
    public String getOrgCode() {
        return BaseContextHandler.getDepartID();
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getOrgCode(java.lang.String)
     */
    @Override
    public String getOrgCode(String userCode) {
        return userFeign.getDepartIdByUserId(userCode);
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getRoleCodes()
     */
    @Override
    public List<String> getRoleCodes() {
        return userFeign.getGroupCodesByUserId(BaseContextHandler.getUserID());
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getRoleCodes(java.lang.String)
     */
    @Override
    public List<String> getRoleCodes(String userCode) {
        return userFeign.getGroupCodesByUserId(userCode);
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getAuthOrgCodes()
     */
    @Override
    public List<String> getAuthOrgCodes() {
        return userFeign.getGroupCodesByUserId(BaseContextHandler.getUserID());
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getAuthOrgCodes(java.lang.String)
     */
    @Override
    public List<String> getAuthOrgCodes(String userCode) {
        return userFeign.getGroupCodesByUserId(BaseContextHandler.getUserID());
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData1()
     */
    @Override
    public String getSelfPermissionData1() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData1(java.lang.String)
     */
    @Override
    public String getSelfPermissionData1(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData2()
     */
    @Override
    public String getSelfPermissionData2() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData2(java.lang.String)
     */
    @Override
    public String getSelfPermissionData2(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData3()
     */
    @Override
    public String getSelfPermissionData3() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData3(java.lang.String)
     */
    @Override
    public String getSelfPermissionData3(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData4()
     */
    @Override
    public String getSelfPermissionData4() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData4(java.lang.String)
     */
    @Override
    public String getSelfPermissionData4(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData5()
     */
    @Override
    public String getSelfPermissionData5() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService#getSelfPermissionData5(java.lang.String)
     */
    @Override
    public String getSelfPermissionData5(String userCode) {
        // TODO Auto-generated method stub
        return null;
    }

}
