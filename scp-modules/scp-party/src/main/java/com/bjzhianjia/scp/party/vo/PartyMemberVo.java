package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.party.entity.PartyMember;

/**
 * @author By 尚
 * @date 2019/3/28 21:13
 */
public class PartyMemberVo extends PartyMember {

    // 性别显示
    private String sexName;
    // 全日制教育学历名称
    private String ftEduName;
    // 全日制教育学位名称
    private String ftDegreeName;
    // 在职教育学历名称
    private String ptEduName;
    // 在职教育学位名称
    private String ptDegreeName;

    private String preJoinOrgName;

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getFtEduName() {
        return ftEduName;
    }

    public void setFtEduName(String ftEduName) {
        this.ftEduName = ftEduName;
    }

    public String getFtDegreeName() {
        return ftDegreeName;
    }

    public void setFtDegreeName(String ftDegreeName) {
        this.ftDegreeName = ftDegreeName;
    }

    public String getPtEduName() {
        return ptEduName;
    }

    public void setPtEduName(String ptEduName) {
        this.ptEduName = ptEduName;
    }

    public String getPtDegreeName() {
        return ptDegreeName;
    }

    public void setPtDegreeName(String ptDegreeName) {
        this.ptDegreeName = ptDegreeName;
    }

    public String getPreJoinOrgName() {
        return preJoinOrgName;
    }

    public void setPreJoinOrgName(String preJoinOrgName) {
        this.preJoinOrgName = preJoinOrgName;
    }
}
