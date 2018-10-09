package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;

/**
 * 网格
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-27 09:55:01
 */
@Table(name = "area_grid")
public class AreaGrid implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	@Id
	private Integer id;

	// 网格编号
	@Column(name = "grid_code")
	private String gridCode;

	// 网格名称
	@Column(name = "grid_name")
	private String gridName;

	// 网格等级
	//原数据库中为tinyint类型
	@Column(name = "grid_level")
	@MergeField(key = "root_biz_grid_level", feign = DictFeign.class, method = "getByCode")
	private String gridLevel;

	// 上级网格
	@Column(name = "grid_parent")
	private Integer gridParent;

	// 执法队伍
	@Column(name = "grid_team")
	private Integer gridTeam;

	// 网格员数量
	@Column(name = "grid_numbers")
	// 自动生成代码为Boolean类型
	private Integer gridNumbers;

	// 网格户数
	@Column(name = "grid_household")
	// 自动生成代码为Boolean类型
	private Integer gridHousehold;

	// 网格人数
	@Column(name = "grid_persons")
	private Integer gridPersons;

	// 网格面积(平方米)
	@Column(name = "grid_areas")
	private Float gridAreas;

	// 网格范围
	@Column(name = "grid_range")
	private String gridRange;

	// 管理部门
	@Column(name = "mgr_dept")
	private String mgrDept;

	// 地图信息
	@Column(name = "map_info")
	private String mapInfo;

	// 是否删除；1：是；0: 否
	@Column(name = "is_deleted")
	private String isDeleted;

	// 是否禁用；1：是；0：否
	@Column(name = "is_disabled")
	private String isDisabled;

	// 创建人
	@Column(name = "crt_user_name")
	private String crtUserName;

	// 创建人ID
	@Column(name = "crt_user_id")
	private String crtUserId;

	// 创建时间
	@Column(name = "crt_time")
	private Date crtTime;

	// 最后更新人
	@Column(name = "upd_user_name")
	private String updUserName;

	// 最后更新人ID
	@Column(name = "upd_user_id")
	private String updUserId;

	// 最后更新时间
	@Column(name = "upd_time")
	private Date updTime;

	// 部门ID
	@Column(name = "depart_id")
	private String departId;

	// 租户ID
	@Column(name = "tenant_id")
	private String tenantId;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：网格编号
	 */
	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	/**
	 * 获取：网格编号
	 */
	public String getGridCode() {
		return gridCode;
	}

	/**
	 * 设置：网格名称
	 */
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	/**
	 * 获取：网格名称
	 */
	public String getGridName() {
		return gridName;
	}
	/**
	 * 设置：网格等级
	 */
	public void setGridLevel(String gridLevel) {
		this.gridLevel = gridLevel;
	}
	/**
	 * 获取：网格等级
	 */
	public String getGridLevel() {
		return gridLevel;
	}

//	/**
//	 * 设置：网格等级
//	 */
//	public void setGridLevel(Boolean gridLevel) {
//		this.gridLevel = gridLevel;
//	}
//
//	/**
//	 * 获取：网格等级
//	 */
//	public Boolean getGridLevel() {
//		return gridLevel;
//	}

	/**
	 * 设置：上级网格
	 */
	public void setGridParent(Integer gridParent) {
		this.gridParent = gridParent;
	}

	/**
	 * 获取：上级网格
	 */
	public Integer getGridParent() {
		return gridParent;
	}

	/**
	 * 设置：执法队伍
	 */
	public void setGridTeam(Integer gridTeam) {
		this.gridTeam = gridTeam;
	}

	/**
	 * 获取：执法队伍
	 */
	public Integer getGridTeam() {
		return gridTeam;
	}

	/**
	 * 获取：网格员数量
	 */
	public Integer getGridNumbers() {
		return gridNumbers;
	}

	/**
	 * 设置：网格员数量
	 */
	public void setGridNumbers(Integer gridNumbers) {
		this.gridNumbers = gridNumbers;
	}

	/**
	 * 设置：网格户数
	 */
	public Integer getGridHousehold() {
		return gridHousehold;
	}

	/**
	 * 设置：网格户数
	 */
	public void setGridHousehold(Integer gridHousehold) {
		this.gridHousehold = gridHousehold;
	}

	// /**
//	 * 设置：网格员数量
//	 */
//	public void setGridNumbers(Boolean gridNumbers) {
//		this.gridNumbers = gridNumbers;
//	}
//	/**
//	 * 获取：网格员数量
//	 */
//	public Boolean getGridNumbers() {
//		return gridNumbers;
//	}
//	/**
//	 * 设置：网格户数
//	 */
//	public void setGridHousehold(Boolean gridHousehold) {
//		this.gridHousehold = gridHousehold;
//	}
//	/**
//	 * 获取：网格户数
//	 */
//	public Boolean getGridHousehold() {
//		return gridHousehold;
//	}
	/**
	 * 设置：网格人数
	 */
	public void setGridPersons(Integer gridPersons) {
		this.gridPersons = gridPersons;
	}

	/**
	 * 获取：网格人数
	 */
	public Integer getGridPersons() {
		return gridPersons;
	}

	/**
	 * 设置：网格面积(平方米)
	 */
	public void setGridAreas(Float gridAreas) {
		this.gridAreas = gridAreas;
	}

	/**
	 * 获取：网格面积(平方米)
	 */
	public Float getGridAreas() {
		return gridAreas;
	}

	/**
	 * 设置：网格范围
	 */
	public void setGridRange(String gridRange) {
		this.gridRange = gridRange;
	}

	/**
	 * 获取：网格范围
	 */
	public String getGridRange() {
		return gridRange;
	}

	/**
	 * 设置：管理部门
	 */
	public void setMgrDept(String mgrDept) {
		this.mgrDept = mgrDept;
	}

	/**
	 * 获取：管理部门
	 */
	public String getMgrDept() {
		return mgrDept;
	}

	/**
	 * 设置：地图信息
	 */
	public void setMapInfo(String mapInfo) {
		this.mapInfo = mapInfo;
	}

	/**
	 * 获取：地图信息
	 */
	public String getMapInfo() {
		return mapInfo;
	}

	/**
	 * 设置：是否删除；1：是；0: 否
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * 获取：是否删除；1：是；0: 否
	 */
	public String getIsDeleted() {
		return isDeleted;
	}

	/**
	 * 设置：是否禁用；1：是；0：否
	 */
	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}

	/**
	 * 获取：是否禁用；1：是；0：否
	 */
	public String getIsDisabled() {
		return isDisabled;
	}

	/**
	 * 设置：创建人
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}

	/**
	 * 获取：创建人
	 */
	public String getCrtUserName() {
		return crtUserName;
	}

	/**
	 * 设置：创建人ID
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}

	/**
	 * 获取：创建人ID
	 */
	public String getCrtUserId() {
		return crtUserId;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}

	/**
	 * 设置：最后更新人
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}

	/**
	 * 获取：最后更新人
	 */
	public String getUpdUserName() {
		return updUserName;
	}

	/**
	 * 设置：最后更新人ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}

	/**
	 * 获取：最后更新人ID
	 */
	public String getUpdUserId() {
		return updUserId;
	}

	/**
	 * 设置：最后更新时间
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	/**
	 * 获取：最后更新时间
	 */
	public Date getUpdTime() {
		return updTime;
	}

	/**
	 * 设置：部门ID
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}

	/**
	 * 获取：部门ID
	 */
	public String getDepartId() {
		return departId;
	}

	/**
	 * 设置：租户ID
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * 获取：租户ID
	 */
	public String getTenantId() {
		return tenantId;
	}
}
