package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党建品牌故事附件表
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
@Table(name = "brand_story_file")
public class BrandStoryFile implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    //党建品牌故事id
    @Column(name = "story_id")
    private Integer storyId;

    //品牌故事附件文件名
    @Column(name = "file_name")
    private String fileName;

    //品牌故事附件路径
    @Column(name = "file_url")
    private String fileUrl;

    //品牌故事附件说明
    @Column(name = "file_comment")
    private String fileComment;

    //故事附件上传日期
    @Column(name = "file_upload_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fileUploadDate;

    //故事附件状态code（字典表code）
    @Column(name = "file_state")
    private String fileState;

    @Column(name = "is_deleted")
    private String isDeleted;


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
     * 设置：党建品牌故事id
     */
    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    /**
     * 获取：党建品牌故事id
     */
    public Integer getStoryId() {
        return storyId;
    }

    /**
     * 设置：品牌故事附件文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取：品牌故事附件文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置：品牌故事附件路径
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取：品牌故事附件路径
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置：品牌故事附件说明
     */
    public void setFileComment(String fileComment) {
        this.fileComment = fileComment;
    }

    /**
     * 获取：品牌故事附件说明
     */
    public String getFileComment() {
        return fileComment;
    }

    /**
     * 设置：故事附件上传日期
     */
    public void setFileUploadDate(Date fileUploadDate) {
        this.fileUploadDate = fileUploadDate;
    }

    /**
     * 获取：故事附件上传日期
     */
    public Date getFileUploadDate() {
        return fileUploadDate;
    }

    /**
     * 设置：故事附件状态code（字典表code）
     */
    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    /**
     * 获取：故事附件状态code（字典表code）
     */
    public String getFileState() {
        return fileState;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
