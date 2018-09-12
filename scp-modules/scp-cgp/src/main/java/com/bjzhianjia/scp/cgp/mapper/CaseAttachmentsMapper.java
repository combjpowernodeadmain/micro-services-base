package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.CaseAttachments;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 立案附件
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
public interface CaseAttachmentsMapper extends CommonMapper<CaseAttachments> {
	void insertCaseAttachmentList(@Param("attaList")List<CaseAttachments> attaList);
}
