package com.bjzhianjia.scp.cgp.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 
 * 
 * @author By Shang
 * @email ${email}
 * @version 2019-03-01 11:14:02
 */
@Table(name = "coordinate_point")
public class CoordinatePoint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private Integer id;
	
	    //
    @Column(name = "fid")
    private Integer fid;
	
	    //
    @Column(name = "c_name")
    private String cName;
	
	    //
    @Column(name = "point_x")
    private String pointX;
	
	    //
    @Column(name = "point_y")
    private String pointY;
	
	    //与ArcGis-code对应,ArcGis中BGCODE或BMCODE名称不一致，需要处理后再导入
    @Column(name = "current_code")
    private String currentCode;
	
	    //确认好ArcGis中哪个字段代表父级code
    @Column(name = "parent_code")
    private String parentCode;
	
	    //当前网格等级
    @Column(name = "g_level")
    private String gLevel;
	

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
	 * 设置：
	 */
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	/**
	 * 获取：
	 */
	public Integer getFid() {
		return fid;
	}
	/**
	 * 设置：
	 */
	public void setCName(String cName) {
		this.cName = cName;
	}
	/**
	 * 获取：
	 */
	public String getCName() {
		return cName;
	}
	/**
	 * 设置：
	 */
	public void setPointX(String pointX) {
		this.pointX = pointX;
	}
	/**
	 * 获取：
	 */
	public String getPointX() {
		return pointX;
	}
	/**
	 * 设置：
	 */
	public void setPointY(String pointY) {
		this.pointY = pointY;
	}
	/**
	 * 获取：
	 */
	public String getPointY() {
		return pointY;
	}
	/**
	 * 设置：与ArcGis-code对应,ArcGis中BGCODE或BMCODE名称不一致，需要处理后再导入
	 */
	public void setCurrentCode(String currentCode) {
		this.currentCode = currentCode;
	}
	/**
	 * 获取：与ArcGis-code对应,ArcGis中BGCODE或BMCODE名称不一致，需要处理后再导入
	 */
	public String getCurrentCode() {
		return currentCode;
	}
	/**
	 * 设置：确认好ArcGis中哪个字段代表父级code
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	/**
	 * 获取：确认好ArcGis中哪个字段代表父级code
	 */
	public String getParentCode() {
		return parentCode;
	}
	/**
	 * 设置：当前网格等级
	 */
	public void setGLevel(String gLevel) {
		this.gLevel = gLevel;
	}
	/**
	 * 获取：当前网格等级
	 */
	public String getGLevel() {
		return gLevel;
	}
}
