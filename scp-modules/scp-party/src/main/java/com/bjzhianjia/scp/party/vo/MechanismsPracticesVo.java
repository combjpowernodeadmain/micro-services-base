package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.party.entity.MechanismsPractices;

public class MechanismsPracticesVo extends MechanismsPractices {

    private String brandFile;

    private String partyOrgName;

    public String getBrandFile() {
        return brandFile;
    }

    public void setBrandFile(String brandFile) {
        this.brandFile = brandFile;
    }

    public String getPartyOrgName() {
        return partyOrgName;
    }

    public void setPartyOrgName(String partyOrgName) {
        this.partyOrgName = partyOrgName;
    }
}
