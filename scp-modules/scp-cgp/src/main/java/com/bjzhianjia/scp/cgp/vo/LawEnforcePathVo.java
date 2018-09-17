package com.bjzhianjia.scp.cgp.vo;

import org.hibernate.validator.constraints.NotBlank;

import com.bjzhianjia.scp.cgp.entity.LawEnforcePath;

/**
 * LawEnforcePathVo 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月4日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
public class LawEnforcePathVo extends LawEnforcePath{
    //{"lng":"xx","lat":"xxx"}
    private String mapInfo;
    
    /**
     * 获取：{"lng":"xx","lat":"xxx"}
     * @return
     */
    public String getMapInfo() {
        return mapInfo;
    }
    
    /**
     * 设置: {"lng":"xx","lat":"xxx"}
     * 
     */
    public void setMapInfo(String mapInfo) {
        this.mapInfo = mapInfo;
    }
}
