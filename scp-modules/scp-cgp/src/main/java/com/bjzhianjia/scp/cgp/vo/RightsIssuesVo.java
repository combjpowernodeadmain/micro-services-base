package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.RightsIssues;

/**
 * RightsIssuesVo 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月17日          尚      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author can
 *
 */
public class RightsIssuesVo extends RightsIssues {

    /**
     * RightsIssuesVo.java description
     */
    private static final long serialVersionUID = -7938532139721588200L;
    /**
     * 事件类别名称
     */
    private String eventTypeName;
    /**
     * 业务条线数据字典code
     */
    private String bizTypeCode;

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getBizTypeCode() {
        return bizTypeCode;
    }

    public void setBizTypeCode(String bizTypeCode) {
        this.bizTypeCode = bizTypeCode;
    }
}
