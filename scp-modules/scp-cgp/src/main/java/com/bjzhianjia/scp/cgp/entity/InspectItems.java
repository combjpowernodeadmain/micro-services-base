package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;


/**
 * 巡查事项
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-16 16:08:44
 */
@Table(name = "inspect_items")
public class InspectItems implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //巡查事项编号
    @Column(name = "code")
    @NotEmpty(message="巡查事项编号不能为空")
    @Length(max=32,message="巡查事项编号长度不能超过32")
    private String code;
	
	    //巡查事项名称
    @Column(name = "name")
    @NotEmpty(message="巡查事项编名称不能为空")
    @Length(max=127,message="巡查事项名称长度不能超过127")
    private String name;
	
	    //事件类别
    @Column(name = "type")
    @NotEmpty(message="事件类别不能为空")
    private String type;
	
	    //业务条线
    @Column(name = "biz_type")
    @NotEmpty(message="业务条线不能为空")
    @MergeField(key = "root_biz_type", feign = DictFeign.class, method = "getByCode")
    private String bizType;
	
	    //巡查方式
    @Column(name = "inspect_way")
    private String inspectWay;
	
	    //巡查频次
    @Column(name = "inspect_frenquency")
    private Integer inspectFrenquency;
	
	    //专项活动
    @Column(name = "special_campaign")
    private String specialCampaign;
	
	    //权力事项编号
    @Column(name = "item_num")
    @NotEmpty(message="权利事项编号不能为空")
    @Length(max=32,message="权利事项编号长度不能超过32")
    private String itemNum;
	
	    //是否可用
    @Column(name = "is_enable")
    @NotEmpty(message="是否可用不能为空")
    @MergeField(key = "root_biz_enabled", feign = DictFeign.class, method = "getByCode")
    private String isEnable;
	
	    //是否删除；1：是；0: 否
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //创建人
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //最后更新人
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //最后更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //部门ID
    @Column(name = "depart_id")
    private String departId;
	
	    //租户ID
    @Column(name = "tenant_id")
    private String tenantId;
	

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：巡查事项编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：巡查事项编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：巡查事项名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：巡查事项名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：事件类别
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：事件类别
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：业务条线
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	/**
	 * 获取：业务条线
	 */
	public String getBizType() {
		return bizType;
	}
	/**
	 * 设置：巡查方式
	 */
	public void setInspectWay(String inspectWay) {
		this.inspectWay = inspectWay;
	}
	/**
	 * 获取：巡查方式
	 */
	public String getInspectWay() {
		return inspectWay;
	}
	/**
	 * 设置：巡查频次
	 */
	public void setInspectFrenquency(Integer inspectFrenquency) {
		this.inspectFrenquency = inspectFrenquency;
	}
	/**
	 * 获取：巡查频次
	 */
	public Integer getInspectFrenquency() {
		return inspectFrenquency;
	}
	/**
	 * 设置：专项活动
	 */
	public void setSpecialCampaign(String specialCampaign) {
		this.specialCampaign = specialCampaign;
	}
	/**
	 * 获取：专项活动
	 */
	public String getSpecialCampaign() {
		return specialCampaign;
	}
	/**
	 * 设置：权力事项编号
	 */
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	/**
	 * 获取：权力事项编号
	 */
	public String getItemNum() {
		return itemNum;
	}
	/**
	 * 设置：是否可用
	 */
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	/**
	 * 获取：是否可用
	 */
	public String getIsEnable() {
		return isEnable;
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
