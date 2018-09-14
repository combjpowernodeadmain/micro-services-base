package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;

/**
 * 
 * @author 尚
 *
 */
public class RegulaObjectVo extends RegulaObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6624378212384321901L;
	private String objTypeName;
	
	private EnterpriseInfo enterpriseInfo;
	public String getObjTypeName() {
		return objTypeName;
	}
	public void setObjTypeName(String objTypeName) {
		this.objTypeName = objTypeName;
	}
    
    public EnterpriseInfo getEnterpriseInfo() {
        return enterpriseInfo;
    }
    
    public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        this.enterpriseInfo = enterpriseInfo;
    }

}
