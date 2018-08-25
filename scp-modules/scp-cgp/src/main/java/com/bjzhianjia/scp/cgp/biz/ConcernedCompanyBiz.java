package com.bjzhianjia.scp.cgp.biz;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.mapper.ConcernedCompanyMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

/**
 * 当事人（企业）表
 *
 * @author bo
 */
@Service
public class ConcernedCompanyBiz extends BusinessBiz<ConcernedCompanyMapper,ConcernedCompany> {
}