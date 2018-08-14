package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 09:11:11
 */
@Table(name = "leadership_assign")
public class LeadershipAssign implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //交办领导
    @Column(name = "task_leader")
    private String taskLeader;
    
    	//交办事项编号
    @Column(name="task_code")
    private String taskCode;
	
    	//地点
    @Column(name="task_addr")
    private String taskAddr;
    
	    //任务标题
    @Column(name = "task_title")
    private String taskTitle;
	
	    //任务内容
    @Column(name = "task_desc")
    private String taskDesc;
	
	    //交办时间
    @Column(name = "task_time")
    private Date taskTime;
	
	    //完成期限
    @Column(name = "dead_line")
    private Date deadLine;
	
	    //涉及监管对象
    @Column(name = "regula_obj_list")
    private String regulaObjList;
	
	    //事件处理状态
    @Column(name = "exe_status")
    private String exeStatus;
	
	    //是否删除(1:删除|0:未删除)
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //更新人姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //租房
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //部门ID
    @Column(name = "dept_id")
    private String deptId;
	

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
	 * 设置：交办领导
	 */
	public void setTaskLeader(String taskLeader) {
		this.taskLeader = taskLeader;
	}
	/**
	 * 获取：交办领导
	 */
	public String getTaskLeader() {
		return taskLeader;
	}
	/**
	 * 获取：交办事项编号
	 */
	public String getTaskCode() {
		return taskCode;
	}
	/**
	 * 设置：交办事项编号
	 */
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	/**
	 * 获取：地点
	 */
	public String getTaskAddr() {
		return taskAddr;
	}
	/**
	 * 设置：地点
	 */
	public void setTaskAddr(String taskAddr) {
		this.taskAddr = taskAddr;
	}
	/**
	 * 设置：任务标题
	 */
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	/**
	 * 获取：任务标题
	 */
	public String getTaskTitle() {
		return taskTitle;
	}
	/**
	 * 设置：任务内容
	 */
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	/**
	 * 获取：任务内容
	 */
	public String getTaskDesc() {
		return taskDesc;
	}
	/**
	 * 设置：交办时间
	 */
	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}
	/**
	 * 获取：交办时间
	 */
	public Date getTaskTime() {
		return taskTime;
	}
	/**
	 * 设置：完成期限
	 */
	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}
	/**
	 * 获取：完成期限
	 */
	public Date getDeadLine() {
		return deadLine;
	}
	/**
	 * 设置：涉及监管对象
	 */
	public void setRegulaObjList(String regulaObjList) {
		this.regulaObjList = regulaObjList;
	}
	/**
	 * 获取：涉及监管对象
	 */
	public String getRegulaObjList() {
		return regulaObjList;
	}
	/**
	 * 设置：事件处理状态
	 */
	public void setExeStatus(String exeStatus) {
		this.exeStatus = exeStatus;
	}
	/**
	 * 获取：事件处理状态
	 */
	public String getExeStatus() {
		return exeStatus;
	}
	/**
	 * 设置：是否删除(1:删除|0:未删除)
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：是否删除(1:删除|0:未删除)
	 */
	public String getIsDeleted() {
		return isDeleted;
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
	 * 设置：创建人姓名
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：创建人姓名
	 */
	public String getCrtUserName() {
		return crtUserName;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdTime() {
		return updTime;
	}
	/**
	 * 设置：更新人ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：更新人ID
	 */
	public String getUpdUserId() {
		return updUserId;
	}
	/**
	 * 设置：更新人姓名
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：更新人姓名
	 */
	public String getUpdUserName() {
		return updUserName;
	}
	/**
	 * 设置：租房
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租房
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：部门ID
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门ID
	 */
	public String getDeptId() {
		return deptId;
	}
}