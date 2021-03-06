package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.cgp.mapper.RightsIssuesMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 权利事项业务类
 * 
 * @author zzh
 *
 */
@Service
public class RightsIssuesBiz extends BusinessBiz<RightsIssuesMapper, RightsIssues> {

    @Autowired
    private RightsIssuesMapper rightsIssuesMapper;

    /**
     * 根据编号查询
     * 
     * @param code
     *            编号
     * @return
     */
    public RightsIssues getByCode(String code) {

        Example example = new Example(RightsIssues.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("code", code);

        List<RightsIssues> list = rightsIssuesMapper.selectByExample(example);

        if (list == null || list.size() == 0) {
            return null;
        }

        RightsIssues rightsIssues = list.get(0);

        if (rightsIssues.getIsDeleted().equals("1")) {
            return null;
        }

        return rightsIssues;
    }

    /**
     * 根据法则获取
     * 
     * @param unlawfulAct
     *            法则
     * @return
     */
    public RightsIssues getByUnlawfulAct(String unlawfulAct) {
        Example example = new Example(RightsIssues.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("unlawfulAct", unlawfulAct);

        List<RightsIssues> list = rightsIssuesMapper.selectByExample(example);

        if (list == null || list.size() == 0) {
            return null;
        }

        RightsIssues rightsIssues = list.get(0);

        if (rightsIssues.getIsDeleted().equals("1")) {
            return null;
        }

        return rightsIssues;
    }

    /**
     * 根据查询条件搜索
     * 
     * @param rightsIssues
     * @return
     */
    public TableResultResponse<RightsIssues> getList(int page, int limit, List<Integer> bizTypes,
        RightsIssues rightsIssues) {
        Example example = new Example(RightsIssues.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(rightsIssues.getCode())) {
            criteria.andLike("code", "%" + rightsIssues.getCode() + "%");
        }
        if (StringUtils.isNotBlank(rightsIssues.getUnlawfulAct())) {
            criteria.andLike("unlawfulAct", "%" + rightsIssues.getUnlawfulAct() + "%");
        }
        if (bizTypes != null && bizTypes.size() > 0) {
            criteria.andIn("type", bizTypes);
        }
        if (StringUtils.isNotBlank(rightsIssues.getIsEnable())) {
            criteria.andEqualTo("isEnable", rightsIssues.getIsEnable());
        }
        if (BeanUtil.isNotEmpty(rightsIssues.getType())) {
            criteria.andEqualTo("type", rightsIssues.getType());
        }
        if (StringUtils.isNotBlank(rightsIssues.getBizType())) {
            criteria.andEqualTo("bizType", rightsIssues.getBizType());
        }

        example.setOrderByClause("id desc");

        Page<Object> result = PageHelper.startPage(page, limit);
        List<RightsIssues> list = rightsIssuesMapper.selectByExample(example);
        return new TableResultResponse<RightsIssues>(result.getTotal(), list);
    }

    /**
     * 根据ID获取单个对象
     * @param id 待查询对象ID
     * @return
     */
    public RightsIssues getById(Integer id) {

        RightsIssues rightsIssues = rightsIssuesMapper.selectByPrimaryKey(id);

        if (rightsIssues != null && rightsIssues.getIsDeleted().equals("1")) {
            return null;
        }

        return rightsIssues;
    }

    public void deleteByIds(Integer[] ids) {

        rightsIssuesMapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getName(), new Date());
    }
}
