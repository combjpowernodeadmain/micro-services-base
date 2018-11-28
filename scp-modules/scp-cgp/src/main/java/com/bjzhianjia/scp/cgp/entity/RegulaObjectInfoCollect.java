package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 监管对象信息采集过程记录
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-11-22 17:30:13
 */
@Table(name = "regula_object_info_collect")
public class RegulaObjectInfoCollect implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //监管对象ID
    @Column(name = "obj_id")
    private Integer objId;
	
	    //信息提交者
    @Column(name = "info_committer")
    private String infoCommitter;
	
	    //信息提交时间
    @Column(name = "info_commit_time")
    private Date infoCommitTime;
	
	    //提交信息
    @Column(name = "commit_msg")
    private String commitMsg;
	
	    //信息审批人
    @Column(name = "info_approver")
    private String infoApprover;
	
	    //信息审批时间
    @Column(name = "info_approve_time")
    private Date infoApproveTime;
	
	    //审批意见
    @Column(name = "info_approve_opinion")
    private String infoApproveOpinion;
	
	    //信息审批结果(0:通过|1:退回:|2:作废)
    @Column(name = "info_approve_status")
    private String infoApproveStatus;
	
	    //信息提交者姓名
    @Column(name = "info_committer_name")
    private String infoCommitterName;
	
	    //用于标记当前是处理审批状态还是提交状态(0:提交|1:审批)
    @Column(name = "info_approver_name")
    private String infoApproverName;
	
	    //节点状态(0:待处理|1:已处理|)
    @Column(name = "info_commit_type")
	private String infoCommitType;
	
	    //是否结束(0:未结束|1:已结束)
    @Column(name = "is_finished")
    private String isFinished;
	

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
	 * 设置：监管对象ID
	 */
	public void setObjId(Integer objId) {
		this.objId = objId;
	}
	/**
	 * 获取：监管对象ID
	 */
	public Integer getObjId() {
		return objId;
	}
	/**
	 * 设置：信息提交者
	 */
	public void setInfoCommitter(String infoCommitter) {
		this.infoCommitter = infoCommitter;
	}
	/**
	 * 获取：信息提交者
	 */
	public String getInfoCommitter() {
		return infoCommitter;
	}
	/**
	 * 设置：信息提交时间
	 */
	public void setInfoCommitTime(Date infoCommitTime) {
		this.infoCommitTime = infoCommitTime;
	}
	/**
	 * 获取：信息提交时间
	 */
	public Date getInfoCommitTime() {
		return infoCommitTime;
	}
	/**
	 * 设置：提交信息
	 */
	public void setCommitMsg(String commitMsg) {
		this.commitMsg = commitMsg;
	}
	/**
	 * 获取：提交信息
	 */
	public String getCommitMsg() {
		return commitMsg;
	}
	/**
	 * 设置：信息审批人
	 */
	public void setInfoApprover(String infoApprover) {
		this.infoApprover = infoApprover;
	}
	/**
	 * 获取：信息审批人
	 */
	public String getInfoApprover() {
		return infoApprover;
	}
	/**
	 * 设置：信息审批时间
	 */
	public void setInfoApproveTime(Date infoApproveTime) {
		this.infoApproveTime = infoApproveTime;
	}
	/**
	 * 获取：信息审批时间
	 */
	public Date getInfoApproveTime() {
		return infoApproveTime;
	}
	/**
	 * 设置：审批意见
	 */
	public void setInfoApproveOpinion(String infoApproveOpinion) {
		this.infoApproveOpinion = infoApproveOpinion;
	}
	/**
	 * 获取：审批意见
	 */
	public String getInfoApproveOpinion() {
		return infoApproveOpinion;
	}
	/**
	 * 设置：信息审批结果(0:通过|1:退回:|2:作废)
	 */
	public void setInfoApproveStatus(String infoApproveStatus) {
		this.infoApproveStatus = infoApproveStatus;
	}
	/**
	 * 获取：信息审批结果(0:通过|1:退回:|2:作废)
	 */
	public String getInfoApproveStatus() {
		return infoApproveStatus;
	}
	/**
	 * 设置：是否结束(0:未结束|1:已结束)
	 */
	public void setIsFinished(String isFinished) {
		this.isFinished = isFinished;
	}
	/**
	 * 获取：是否结束(0:未结束|1:已结束)
	 */
	public String getIsFinished() {
		return isFinished;
	}

	/**
	 * 获取：提交人姓名
	 */
	public String getInfoCommitterName() {
		return infoCommitterName;
	}

	/**
	 * 设置：提交人姓名
	 */
	public void setInfoCommitterName(String infoCommitterName) {
		this.infoCommitterName = infoCommitterName;
	}

	/**
	 * 获取：审批人姓名
	 */
	public String getInfoApproverName() {
		return infoApproverName;
	}

	/**
	 * 设置：审批人姓名
	 */
	public void setInfoApproverName(String infoApproverName) {
		this.infoApproverName = infoApproverName;
	}

	/**
	 * 获取：新增或变更
	 */
	public String getInfoCommitType() {
		return infoCommitType;
	}

	/**
	 * 设置：新增或变更
	 */
	public void setInfoCommitType(String infoCommitType) {
		this.infoCommitType = infoCommitType;
	}
}
