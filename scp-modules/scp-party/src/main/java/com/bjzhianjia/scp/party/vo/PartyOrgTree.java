package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.security.common.vo.TreeNodeVO;

/**
 * @author By 尚
 * @date 2019/3/28 20:32
 */
public class PartyOrgTree extends TreeNodeVO<PartyOrgTree> {

    private Integer id;
    private Integer parentId;
    private String orgFullName;
    private String orgShortName;
    private String mapInfo;
    private String contactsUser;
    private String mobilePhone;
    private String address;

    public PartyOrgTree(Integer id, Integer parentId, String orgFullName, String orgShortName, String mapInfo, String contactsUser, String mobilePhone, String address) {
        this.id = id;
        this.parentId = parentId;
        this.orgFullName = orgFullName;
        this.orgShortName = orgShortName;
        this.mapInfo = mapInfo;
        this.contactsUser = contactsUser;
        this.mobilePhone = mobilePhone;
        this.address = address;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getOrgFullName() {
        return orgFullName;
    }

    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }

    public String getMapInfo() {
        return mapInfo;
    }

    public void setMapInfo(String mapInfo) {
        this.mapInfo = mapInfo;
    }

    public String getContactsUser() {
        return contactsUser;
    }

    public void setContactsUser(String contactsUser) {
        this.contactsUser = contactsUser;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
