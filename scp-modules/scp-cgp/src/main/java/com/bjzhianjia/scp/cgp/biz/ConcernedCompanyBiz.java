package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.mapper.ConcernedCompanyMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 当事人（企业）表
 *
 * @author bo
 */
@Service
public class ConcernedCompanyBiz extends BusinessBiz<ConcernedCompanyMapper,ConcernedCompany> {
    
    /**
     * 按ID查询对象
     * 
     * @param id
     * @return
     */
    public ObjectRestResponse<ConcernedCompany> getOne(Integer id) {
        Example example =
            new Example(ConcernedCompany.class)
                .selectProperties(BeanUtil.skipFields(ConcernedCompany.class, "crtUserId",
                    "crtUserName", "crtTime", "updTime", "updUserId", "updUserName"));
        example.createCriteria().andEqualTo("id", id);

        List<ConcernedCompany> concernedCompanies = this.selectByExample(example);
        if (BeanUtil.isNotEmpty(concernedCompanies)) {
            // 当按ID进行查询时，如果结果集不为空，则长度必为1
            return new ObjectRestResponse<ConcernedCompany>().data(concernedCompanies.get(0));
        }

        return null;
    }
}