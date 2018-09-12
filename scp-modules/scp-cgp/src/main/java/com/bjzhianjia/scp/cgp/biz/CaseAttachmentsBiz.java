package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.CaseAttachments;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.mapper.CaseAttachmentsMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * CaseAttachmentsBiz 类描述.立案附件
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年8月26日          can      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author can
 *
 */
@Service
public class CaseAttachmentsBiz extends BusinessBiz<CaseAttachmentsMapper, CaseAttachments> {

    /**
     * 分页查询对象
     * 
     * @author 尚
     * @param caseAttachments
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseAttachments> getList(CaseAttachments caseAttachments, int page, int limit) {
        Example example = new Example(CaseAttachments.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");

        if (StringUtils.isNotBlank(caseAttachments.getCaseId())) {
            criteria.andEqualTo("caseId", caseAttachments.getCaseId());
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseAttachments> list = this.selectByExample(example);

        return new TableResultResponse<>(pageInfo.getTotal(), list);
    }

    /**
     * =批量添加对象
     * 
     * @param caseAttachmentsList
     * @return
     */
    public Result<Void> add(List<CaseAttachments> caseAttachmentsList) {
        Result<Void> result = new Result<>();

        for (CaseAttachments caseAttachments : caseAttachmentsList) {
            caseAttachments.setCrtTime(new Date());
            caseAttachments.setCrtUserId(BaseContextHandler.getUserID());
            caseAttachments.setCrtUserName(BaseContextHandler.getUsername());

            caseAttachments.setIsDeleted(
                StringUtils.isBlank(caseAttachments.getIsDeleted()) ? "0" : caseAttachments.getIsDeleted());
        }

        this.mapper.insertCaseAttachmentList(caseAttachmentsList);
        result.setIsSuccess(true);
        return result;
    }
}