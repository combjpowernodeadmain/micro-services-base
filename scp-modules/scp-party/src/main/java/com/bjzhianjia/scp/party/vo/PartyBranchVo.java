package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.party.entity.PartyBranch;

/**
 * @author By 尚
 * @date 2019/3/31 15:19
 */
public class PartyBranchVo extends PartyBranch {

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
