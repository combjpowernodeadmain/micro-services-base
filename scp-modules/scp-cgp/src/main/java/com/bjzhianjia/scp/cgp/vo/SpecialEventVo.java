package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.SpecialEvent;

/**
 * 专项管理VO
 */
public class SpecialEventVo extends SpecialEvent {

    private static final long serialVersionUID = -1242256664482529546L;
    private String bizListName;
    private String eventTypeName;
    private String speStatusId;
    private String regObjTypeName;
    // 专项状态
    private String speStatusName;

    public String getBizListName() {
        return bizListName;
    }

    public void setBizListName(String bizListName) {
        this.bizListName = bizListName;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getSpeStatusId() {
        return speStatusId;
    }

    public void setSpeStatusId(String speStatusId) {
        this.speStatusId = speStatusId;
    }

    public String getRegObjTypeName() {
        return regObjTypeName;
    }

    public void setRegObjTypeName(String regObjTypeName) {
        this.regObjTypeName = regObjTypeName;
    }

    /**
     * 获取：专项任务状态名称
     *
     * @return
     */
    public String getSpeStatusName() {
        return speStatusName;
    }

    /**
     * 设置：专项任务状态名称
     *
     * @return
     */
    public void setSpeStatusName(String speStatusName) {
        this.speStatusName = speStatusName;
    }
}
