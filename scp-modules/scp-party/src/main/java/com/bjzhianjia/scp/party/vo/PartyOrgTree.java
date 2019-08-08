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

    public PartyOrgTree(Integer id, Integer parentId, String orgFullName, String orgShortName, String mapInfo) {
        this.id = id;
        this.parentId = parentId;
        this.orgFullName = orgFullName;
        this.orgShortName = orgShortName;
        this.mapInfo = mapInfo;
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
}
