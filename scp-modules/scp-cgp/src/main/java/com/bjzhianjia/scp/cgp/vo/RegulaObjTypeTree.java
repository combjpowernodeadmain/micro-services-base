package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.security.common.vo.TreeNodeVO;

public class RegulaObjTypeTree extends TreeNodeVO<RegulaObjTypeTree> {
	private String id;
	private String parentId;
	private String objectTypeName;
	private String objectTypeCode;
	private String templetType;
	private Object regulaObjectCountPerType;
	
	public String getObjectTypeName() {
		return objectTypeName;
	}
	public void setObjectTypeName(String objectTypeName) {
		this.objectTypeName = objectTypeName;
	}
	public String getObjectTypeCode() {
		return objectTypeCode;
	}
	public void setObjectTypeCode(String objectTypeCode) {
		this.objectTypeCode = objectTypeCode;
	}
	public RegulaObjTypeTree(String id,String parentId,String objectTypeName, String objectTypeCode,String templetType,Object regulaObjectCountPerType) {
		this.objectTypeName = objectTypeName;
		this.objectTypeCode = objectTypeCode;
		this.id=id;
		this.parentId=parentId;
		this.templetType=templetType;
		this.regulaObjectCountPerType=regulaObjectCountPerType;
	}
	public RegulaObjTypeTree() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getTempletType() {
		return templetType;
	}
	public void setTempletType(String templetType) {
		this.templetType = templetType;
	}
	public Object getRegulaObjectCountPerType() {
		return regulaObjectCountPerType;
	}
	public void setRegulaObjectCountPerType(Object regulaObjectCountPerType) {
		this.regulaObjectCountPerType = regulaObjectCountPerType;
	}
	
}
