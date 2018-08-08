package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 领导人物分配
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 00:06:35
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
}
