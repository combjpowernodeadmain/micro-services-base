package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 预立案信息
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-08 16:14:17
 */
@Table(name = "case_info")
public class CaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //事件来源类型
    @Column(name = "source_type")
    private String sourceType;
	
	    //事件来源编号
    @Column(name = "source_code")
    private String sourceCode;
	
	    //事件编号
    @Column(name = "case_code")
    private String caseCode;
	
	    //事件标题
    @Column(name = "case_title")
    private String caseTitle;
	
	    //事件描述
    @Column(name = "case_desc")
    private String caseDesc;
    	
    	//事件级别（后加字段）
    @Column(name="case_level")
    private String caseLevel;
	
	    //涉及监管对象（ID集合，逗号隔开）
    @Column(name = "regula_obj_list")
    private String regulaObjList;
	
	    //当事人
    @Column(name = "concerned_person")
    private String concernedPerson;
	
	    //业务条线集
    @Column(name = "biz_list")
    private String bizList;
	
	    //事件类别集
    @Column(name = "event_type_list")
    private String eventTypeList;
	
	    //所属网格
    @Column(name = "grid")
    private Integer grid;
	
	    //事件发生地点
    @Column(name = "occur_addr")
    private String occurAddr;
    
    	//事件发生时间(后加字段)
    @Column(name="occur_time")
    private Date occurTime;
	
	    //地理标识
    @Column(name = "map_info")
    private String mapInfo;
	
	    //问题是否存在
    @Column(name = "check_is_exist")
    private String checkIsExist;
	
	    //检查时间
    @Column(name = "check_time")
    private Date checkTime;
	
	    //检查人
    @Column(name = "check_person")
    private String checkPerson;
	
	    //检查意见
    @Column(name = "check_opinion")
    private String checkOpinion;
	
	    //检查照片
    @Column(name = "check_pic")
    private String checkPic;
	
	    //视频证据链接
    @Column(name = "check_video")
    private String checkVideo;
	
	    //办理期限
    @Column(name = "dead_line")
    private Date deadLine;
	
	    //事件办理部门
    @Column(name = "execute_dept")
    private String executeDept;
	
	    //事件要求
    @Column(name = "requirements")
    private String requirements;
	
	    //审批信息
    @Column(name = "approve_info")
    private String approveInfo;
	
	    //结案检查是否存在
    @Column(name = "finish_check_is_exist")
    private String finishCheckIsExist;
	
	    //结案检查时间
    @Column(name = "finish_check_time")
    private Date finishCheckTime;
	
	    //结案检查人
    @Column(name = "finish_check_person")
    private String finishCheckPerson;
	
	    //结案意见
    @Column(name = "finish_check_opinion")
    private String finishCheckOpinion;
	
	    //结案检查照片
    @Column(name = "finish_check_pic")
    private String finishCheckPic;
	
	    //结案检查视频
    @Column(name = "finish_check_video")
    private String finishCheckVideo;
	
	    //结案人
    @Column(name = "finish_person")
    private String finishPerson;
	
	    //结案时间
    @Column(name = "finish_time")
    private Date finishTime;
	
	    //结案说明
    @Column(name = "finish_desc")
    private String finishDesc;
	
	    //反馈人
    @Column(name = "reply_person")
    private String replyPerson;
	
	    //反馈时间
    @Column(name = "reply_time")
    private Date replyTime;
	
	    //反馈说明
    @Column(name = "reply_desc")
    private String replyDesc;
	
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
    
  //该案件是否重复
    @Column(name="is_duplicate")
    private String isDuplicate;
    
    	//与哪个案件重复
    @Column(name="duplicate_with")
    private Integer duplicateWith;
    
    
    	// 涉及监管对象类型
	@Column(name = "regula_object_type_id")
	private String regulaObjectTypeId;
    
    
    	// 当事人类型（个人，单位）
	@Column(name = "concerned_type")
	private String concernedType;
    
	public String getIsDuplicate() {
		return isDuplicate;
	}
	public void setIsDuplicate(String isDuplicate) {
		this.isDuplicate = isDuplicate;
	}
	public Integer getDuplicateWith() {
		return duplicateWith;
	}
	public void setDuplicateWith(Integer duplicateWith) {
		this.duplicateWith = duplicateWith;
	}
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
	 * 设置：事件来源类型
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * 获取：事件来源类型
	 */
	public String getSourceType() {
		return sourceType;
	}
	/**
	 * 设置：事件来源编号
	 */
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	/**
	 * 获取：事件来源编号
	 */
	public String getSourceCode() {
		return sourceCode;
	}
	/**
	 * 设置：事件编号
	 */
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	/**
	 * 获取：事件编号
	 */
	public String getCaseCode() {
		return caseCode;
	}
	/**
	 * 设置：事件标题
	 */
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	/**
	 * 获取：事件标题
	 */
	public String getCaseTitle() {
		return caseTitle;
	}
	/**
	 * 设置：事件描述
	 */
	public void setCaseDesc(String caseDesc) {
		this.caseDesc = caseDesc;
	}
	/**
	 * 获取：事件描述
	 */
	public String getCaseDesc() {
		return caseDesc;
	}
	/**
	 * 设置：涉及监管对象（ID集合，逗号隔开）
	 */
	public void setRegulaObjList(String regulaObjList) {
		this.regulaObjList = regulaObjList;
	}
	/**
	 * 获取：涉及监管对象（ID集合，逗号隔开）
	 */
	public String getRegulaObjList() {
		return regulaObjList;
	}
	/**
	 * 设置：当事人
	 */
	public void setConcernedPerson(String concernedPerson) {
		this.concernedPerson = concernedPerson;
	}
	/**
	 * 获取：当事人
	 */
	public String getConcernedPerson() {
		return concernedPerson;
	}
	/**
	 * 设置：业务条线集
	 */
	public void setBizList(String bizList) {
		this.bizList = bizList;
	}
	/**
	 * 获取：业务条线集
	 */
	public String getBizList() {
		return bizList;
	}
	/**
	 * 设置：事件类别集
	 */
	public void setEventTypeList(String eventTypeList) {
		this.eventTypeList = eventTypeList;
	}
	/**
	 * 获取：事件类别集
	 */
	public String getEventTypeList() {
		return eventTypeList;
	}
	/**
	 * 设置：所属网格
	 */
	public void setGrid(Integer grid) {
		this.grid = grid;
	}
	/**
	 * 获取：所属网格
	 */
	public Integer getGrid() {
		return grid;
	}
	/**
	 * 设置：事件发生地点
	 */
	public void setOccurAddr(String occurAddr) {
		this.occurAddr = occurAddr;
	}
	/**
	 * 获取：事件发生地点
	 */
	public String getOccurAddr() {
		return occurAddr;
	}
	/**
	 * 设置：地理标识
	 */
	public void setMapInfo(String mapInfo) {
		this.mapInfo = mapInfo;
	}
	/**
	 * 获取：地理标识
	 */
	public String getMapInfo() {
		return mapInfo;
	}
	/**
	 * 设置：问题是否存在
	 */
	public void setCheckIsExist(String checkIsExist) {
		this.checkIsExist = checkIsExist;
	}
	/**
	 * 获取：问题是否存在
	 */
	public String getCheckIsExist() {
		return checkIsExist;
	}
	/**
	 * 设置：检查时间
	 */
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	/**
	 * 获取：检查时间
	 */
	public Date getCheckTime() {
		return checkTime;
	}
	/**
	 * 设置：检查人
	 */
	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	/**
	 * 获取：检查人
	 */
	public String getCheckPerson() {
		return checkPerson;
	}
	/**
	 * 设置：检查意见
	 */
	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}
	/**
	 * 获取：检查意见
	 */
	public String getCheckOpinion() {
		return checkOpinion;
	}
	/**
	 * 设置：检查照片
	 */
	public void setCheckPic(String checkPic) {
		this.checkPic = checkPic;
	}
	/**
	 * 获取：检查照片
	 */
	public String getCheckPic() {
		return checkPic;
	}
	/**
	 * 设置：视频证据链接
	 */
	public void setCheckVideo(String checkVideo) {
		this.checkVideo = checkVideo;
	}
	/**
	 * 获取：视频证据链接
	 */
	public String getCheckVideo() {
		return checkVideo;
	}
	/**
	 * 设置：办理期限
	 */
	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}
	/**
	 * 获取：办理期限
	 */
	public Date getDeadLine() {
		return deadLine;
	}
	/**
	 * 设置：事件办理部门
	 */
	public void setExecuteDept(String executeDept) {
		this.executeDept = executeDept;
	}
	/**
	 * 获取：事件办理部门
	 */
	public String getExecuteDept() {
		return executeDept;
	}
	/**
	 * 设置：事件要求
	 */
	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}
	/**
	 * 获取：事件要求
	 */
	public String getRequirements() {
		return requirements;
	}
	/**
	 * 设置：审批信息
	 */
	public void setApproveInfo(String approveInfo) {
		this.approveInfo = approveInfo;
	}
	/**
	 * 获取：审批信息
	 */
	public String getApproveInfo() {
		return approveInfo;
	}
	/**
	 * 设置：结案检查是否存在
	 */
	public void setFinishCheckIsExist(String finishCheckIsExist) {
		this.finishCheckIsExist = finishCheckIsExist;
	}
	/**
	 * 获取：结案检查是否存在
	 */
	public String getFinishCheckIsExist() {
		return finishCheckIsExist;
	}
	/**
	 * 设置：结案检查时间
	 */
	public void setFinishCheckTime(Date finishCheckTime) {
		this.finishCheckTime = finishCheckTime;
	}
	/**
	 * 获取：结案检查时间
	 */
	public Date getFinishCheckTime() {
		return finishCheckTime;
	}
	/**
	 * 设置：结案检查人
	 */
	public void setFinishCheckPerson(String finishCheckPerson) {
		this.finishCheckPerson = finishCheckPerson;
	}
	/**
	 * 获取：结案检查人
	 */
	public String getFinishCheckPerson() {
		return finishCheckPerson;
	}
	/**
	 * 设置：结案意见
	 */
	public void setFinishCheckOpinion(String finishCheckOpinion) {
		this.finishCheckOpinion = finishCheckOpinion;
	}
	/**
	 * 获取：结案意见
	 */
	public String getFinishCheckOpinion() {
		return finishCheckOpinion;
	}
	/**
	 * 设置：结案检查照片
	 */
	public void setFinishCheckPic(String finishCheckPic) {
		this.finishCheckPic = finishCheckPic;
	}
	/**
	 * 获取：结案检查照片
	 */
	public String getFinishCheckPic() {
		return finishCheckPic;
	}
	/**
	 * 设置：结案检查视频
	 */
	public void setFinishCheckVideo(String finishCheckVideo) {
		this.finishCheckVideo = finishCheckVideo;
	}
	/**
	 * 获取：结案检查视频
	 */
	public String getFinishCheckVideo() {
		return finishCheckVideo;
	}
	/**
	 * 设置：结案人
	 */
	public void setFinishPerson(String finishPerson) {
		this.finishPerson = finishPerson;
	}
	/**
	 * 获取：结案人
	 */
	public String getFinishPerson() {
		return finishPerson;
	}
	/**
	 * 设置：结案时间
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	/**
	 * 获取：结案时间
	 */
	public Date getFinishTime() {
		return finishTime;
	}
	/**
	 * 设置：结案说明
	 */
	public void setFinishDesc(String finishDesc) {
		this.finishDesc = finishDesc;
	}
	/**
	 * 获取：结案说明
	 */
	public String getFinishDesc() {
		return finishDesc;
	}
	/**
	 * 设置：反馈人
	 */
	public void setReplyPerson(String replyPerson) {
		this.replyPerson = replyPerson;
	}
	/**
	 * 获取：反馈人
	 */
	public String getReplyPerson() {
		return replyPerson;
	}
	/**
	 * 设置：反馈时间
	 */
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	/**
	 * 获取：反馈时间
	 */
	public Date getReplyTime() {
		return replyTime;
	}
	/**
	 * 设置：反馈说明
	 */
	public void setReplyDesc(String replyDesc) {
		this.replyDesc = replyDesc;
	}
	/**
	 * 获取：反馈说明
	 */
	public String getReplyDesc() {
		return replyDesc;
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
	public Date getOccurTime() {
		return occurTime;
	}
	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}
	public String getCaseLevel() {
		return caseLevel;
	}
	public void setCaseLevel(String caseLevel) {
		this.caseLevel = caseLevel;
	}
		/**
	 * 获取：涉及监管对象类型id
	 */
	public String getRegulaObjTypeId() {
		return regulaObjectTypeId;
	}

	/**
	 * 设置：涉及监管对象类型id
	 */
	public void setRegulaObjTypeId(String regulaObjectTypeId) {
		this.regulaObjectTypeId = regulaObjectTypeId;
	}

	/**
	 * 获取：当事人类型
	 */
	public String getConcernedType() {
		return concernedType;
	}

	/**
	 * 设置获取：当事人类型
	 */
	public void setConcernedType(String concernedType) {
		this.concernedType = concernedType;
	}
}