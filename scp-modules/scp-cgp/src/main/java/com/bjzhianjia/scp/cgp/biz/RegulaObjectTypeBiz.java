package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * RegulaObjectTypeBiz 监管对象.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年7月7日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author bo
 *
 */
@Service
public class RegulaObjectTypeBiz extends BusinessBiz<RegulaObjectTypeMapper, RegulaObjectType> {

    /**
     * 查询id最大的那条记录
     * 
     * @author 尚
     * @return
     */
    public RegulaObjectType getTheMaxOne() {
        Example example = new Example(RegulaObjectType.class);
        example.setOrderByClause("id desc");
        PageHelper.startPage(0, 1);
        List<RegulaObjectType> RegulaObjectType = this.mapper.selectByExample(example);
        if (RegulaObjectType != null && !RegulaObjectType.isEmpty()) {
            return RegulaObjectType.get(0);
        }
        return null;
    }

    /**
     * 按条件查询
     * 
     * @author 尚
     * @param condition
     *            封装条件的MAP集合，key:条件名，value:条件值
     * @param isContainDelete
     *            是否包含被删除对象
     * @return
     */
    public List<RegulaObjectType> getByMap(Map<String, Object> conditions, boolean isContainDelete) {
        Example example = new Example(RegulaObjectType.class);
        Example.Criteria criteria = example.createCriteria();

        if (!isContainDelete) {
            criteria.andEqualTo("isDeleted", "0");
        }

        Set<String> keySet = conditions.keySet();
        for (String string : keySet) {
            criteria.andEqualTo(string, conditions.get(string));
        }

        List<RegulaObjectType> list = this.mapper.selectByExample(example);
        return list;
    }

    /**
     * 分页查询记录
     * 
     * @author 尚
     * @param page
     * @param limit
     * @param regulaObjectType
     * @return
     */
    public TableResultResponse<RegulaObjectType> getList(int page, int limit, RegulaObjectType regulaObjectType) {
        Example example = new Example(RegulaObjectType.class);
        example.setOrderByClause("order_num asc");
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(regulaObjectType.getObjectTypeCode())) {
            criteria.andLike("objectTypeCode", "%" + regulaObjectType.getObjectTypeCode() + "%");
        }
        if (StringUtils.isNotBlank(regulaObjectType.getObjectTypeName())) {
            criteria.andLike("objectTypeName", "%" + regulaObjectType.getObjectTypeName() + "%");
        }
        if (StringUtils.isNotBlank(regulaObjectType.getIsEnable())) {
            criteria.andEqualTo("isEnable", regulaObjectType.getIsEnable());
        }

        Page<Object> result = PageHelper.startPage(page, limit);
        List<RegulaObjectType> list = this.mapper.selectByExample(example);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    /**
     * 查询所有末删除的记录
     * 
     * @return
     */
    public List<RegulaObjectType> getValidateList() {
        Example example = new Example(RegulaObjectType.class);
        example.setOrderByClause("order_num asc");
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");

        List<RegulaObjectType> list = this.mapper.selectByExample(example);
        return list;
    }

    /**
     * 打量删除对象
     * 
     * @author 尚
     * @param ids
     */
    public void remove(Integer[] ids) {
        this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), new Date());
    }

    /**
     * 按Id集合查询
     * 
     * @author 尚
     * @param ids
     *            多个id集合，用逗号隔开，如"1,2,3,4"
     * @return
     */
    public List<RegulaObjectType> selectByIds(String ids) {
        return this.mapper.selectByIds(ids);
    }

    /**
     * 按ID集合分页查询记录
     * 
     * @author 尚
     * @param ids
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<RegulaObjectType> getByRelation(String ids, int page, int limit) {
        if (StringUtils.isNotBlank(ids)) {
            Example example = new Example(RegulaObjectType.class);
            example.setOrderByClause("order_num desc");
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids.split(",")));

            List<RegulaObjectType> list = this.mapper.selectByExample(example);

            Page<Object> pageInfo = PageHelper.startPage(page, limit);
            return new TableResultResponse<>(pageInfo.getTotal(), list);
        }
        return null;
    }

    /**
     * 分页查询记录
     * 
     * @author 尚
     * @param page
     * @param limit
     * @param regulaObjectType
     * @return
     */
    public List<RegulaObjectType> getBySecondCategory(List<Integer> secondCategoryId) {
        Example example = new Example(RegulaObjectType.class);
        example.setOrderByClause("order_num desc");
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (secondCategoryId != null) {
            criteria.andIn("parentObjectTypeId", secondCategoryId);
        }

        List<RegulaObjectType> list = this.mapper.selectByExample(example);
        return list;
    }

    /**
     * 
     * @return
     */
    public List<RegulaObjectType> getNames() {
        Example example = new Example(RegulaObjectType.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        List<RegulaObjectType> regulaObjectTypeList = this.selectByExample(example);
        List<RegulaObjectType> resultList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(regulaObjectTypeList)) {
            for (RegulaObjectType regulaObjectType : regulaObjectTypeList) {
                RegulaObjectType temp = new RegulaObjectType();
                temp.setId(regulaObjectType.getId());
                temp.setObjectTypeName(regulaObjectType.getObjectTypeName());
                resultList.add(temp);
            }
        }

        return resultList;
    }

    /**
     * 查询所有监管对象类型id和父id
     */
    public List<RegulaObjectType> selectIdAll() {
        return this.mapper.selectIdAll();
    }
}