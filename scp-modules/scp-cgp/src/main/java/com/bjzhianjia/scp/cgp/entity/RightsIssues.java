package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;

/**
 * 权利事项实体
 * @author zzh
 *
 */
@Table(name = "rights_issues")
public class RightsIssues implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//主键
	@Id
    private Integer id; 
	
	//权利事项编号
	@Column(name = "code")
	@NotEmpty(message="权利事项编号不能为空")
    @Length(max=32,message="权利事项编号长度不能超过32")
	private String code;
	
	//事件类别
	@Column(name = "event_type")
	@NotNull(message="事件类别不能为空")
	private Integer type;
	
	//违法行为
	@Column(name = "unlawful_act")
	@NotEmpty(message="违法行为不能为空")
    @Length(max=127,message="执法行为长度不能超过127")
	private String unlawfulAct;
	
	//违则
	@Column(name = "break_rule")
	@NotEmpty(message="违则不能为空")
	private String breakRule;
	
	//违则详情
	@Column(name = "break_rule_detail")
	private String breakRuleDetail;
	
	//罚则
	@Column(name = "penalty")
	@NotEmpty(message="罚则不能为空")
	private String penalty;
	
	//罚则明细
	@Column(name = "penalty_detail")
	private String penaltyDetail;
	
	//自由裁量范围
	@Column(name = "discretionary_range")
	private String discretionaryRange;

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
	
	    //租户ID
    @Column(name = "tenant_id")
    private String tenantId;
    @MergeField(key = "root_biz_type", feign = DictFeign.class, method = "getByCode")
    private String bizType;
	
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
	 * 获取：权利事项编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 获取：权利事项编号
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 获取:事件类型
	 */
	public Integer getType() {
		return type;
	}
	
	/**
	 * 设置: 类型名称
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	
	/**
	 * 获取: 违法行为
	 */
	public String getUnlawfulAct() {
		return unlawfulAct;
	}
	
	/**
	 * 设置: 违法行为
	 */
	public void setUnlawfulAct(String unlawfulAct) {
		this.unlawfulAct = unlawfulAct;
	}
	
	/**
	 * 获取: 违则
	 */
	public String getBreakRule() {
		return breakRule;
	}
	
	/**
	 * 设置: 违则
	 */
	public void setBreakRule(String breakRule) {
		this.breakRule = breakRule;
	}
	
	/**
	 * 获取: 违则详情
	 */
	public String getBreakRuleDetail() {
		return breakRuleDetail;
	}
	
	/**
	 * 设置: 违则详情
	 */
	public void setBreakRuleDetail(String breakRuleDetail) {
		this.breakRuleDetail = breakRuleDetail;
	}
	
	/**
	 * 获取: 罚则
	 */
	public String getPenalty() {
		return penalty;
	}
	
	/**
	 * 设置: 罚则
	 */
	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}
	
	/**
	 * 获取: 罚则详情
	 */
	public String getPenaltyDetail() {
		return penaltyDetail;
	}
	
	/**
	 * 设置: 罚则详情
	 */
	public void setPenaltyDetail(String penaltyDetail) {
		this.penaltyDetail = penaltyDetail;
	}
	
	/**
	 * 获取: 自由裁量范围
	 */
	public String getDiscretionaryRange() {
		return discretionaryRange;
	}
	
	/**
	 * 设置: 自由裁量范围
	 */
	public void setDiscretionaryRange(String discretionaryRange) {
		this.discretionaryRange = discretionaryRange;
	}
	
	/**
	 * 设置：是否可用；1：是；0: 否
	 */
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	/**
	 * 获取：是否可用；1：是；0: 否
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
	
	/**
	 * 获取:业务线条
	 */
	public String getBizType() {
		return bizType;
	}
	
	/**
	 * 设置:业务线条
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	
}
