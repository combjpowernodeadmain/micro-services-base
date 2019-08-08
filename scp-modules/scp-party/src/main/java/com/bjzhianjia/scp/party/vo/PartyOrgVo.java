package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.party.entity.PartyOrg;

/**
 * @author By 尚
 * @date 2019/4/1 18:38
 */
public class PartyOrgVo extends PartyOrg {

    private String parentName;

    private String orgTypeName;

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
