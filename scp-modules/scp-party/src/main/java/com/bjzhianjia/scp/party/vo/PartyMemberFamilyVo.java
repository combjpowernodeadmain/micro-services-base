package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.party.entity.PartyMemberFamily;

/**
 * @author By 尚
 * @date 2019/3/29 10:19
 */
public class PartyMemberFamilyVo extends PartyMemberFamily {
    private String partyMemName;

    private String politicalName;

    public String getPartyMemName() {
        return partyMemName;
    }

    public void setPartyMemName(String partyMemName) {
        this.partyMemName = partyMemName;
    }

    public String getPoliticalName() {
        return politicalName;
    }

    public void setPoliticalName(String politicalName) {
        this.politicalName = politicalName;
    }
}
