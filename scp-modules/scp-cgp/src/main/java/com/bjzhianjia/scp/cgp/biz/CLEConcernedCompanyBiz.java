package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.util.BeanUtil;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.CLEConcernedCompany;
import com.bjzhianjia.scp.cgp.mapper.CLEConcernedCompanyMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;

/**
 * 综合执法 - 当事人（企业）表
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Service
public class CLEConcernedCompanyBiz extends BusinessBiz<CLEConcernedCompanyMapper,CLEConcernedCompany> {
    @Autowired
    private CaseRegistrationBiz caseRegistrationBiz;

    /**
     * 按案件ID获取当事人
     * @param caseId
     * @return
     */
    public CLEConcernedCompany getByCaseId(String caseId) {
        if (StringUtils.isBlank(caseId)) {
            return null;
        }

        // 案件ID在当事人表中，可能会因为数据错误导致出现多条记录，在查询当事人记录时以案件记录中的当事人ID为准
        CaseRegistration caseRegistration = caseRegistrationBiz.selectById(caseId);
        if (BeanUtil.isNotEmpty(caseRegistration)
            && BeanUtil.isNotEmpty(caseRegistration.getConcernedId())) {
            CLEConcernedCompany cleConcernedCompany =
                this.selectById(caseRegistration.getConcernedId());
            return cleConcernedCompany;
        }

        return null;
    }

    public List<CLEConcernedCompany> getList(CLEConcernedCompany company) {
        Example example=new Example(CLEConcernedCompany.class);
        Example.Criteria criteria = example.createCriteria();
        if(BeanUtil.isNotEmpty(company.getRegulaObjectId())){
            criteria.andEqualTo("regulaObjectId",company.getRegulaObjectId());
        }

        return this.selectByExample(example);
    }
}