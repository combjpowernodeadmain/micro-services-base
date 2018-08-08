package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectTypeMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * 监管对象
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:26
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
	 * @author 尚
	 * @param condition 封装条件的MAP集合，key:条件名，value:条件值
	 * @param isContainDelete 是否包含被删除对象
	 * @return
	 */
	public List<RegulaObjectType> getByMap(Map<String, Object> conditions,boolean isContainDelete){
		Example example=new Example(RegulaObjectType.class);
		Example.Criteria criteria=example.createCriteria();
		
		if(!isContainDelete) {
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
	 * @author 尚
	 * @param page
	 * @param limit
	 * @param regulaObjectType
	 * @return
	 */
	public TableResultResponse<RegulaObjectType> getList(int page,int limit,RegulaObjectType regulaObjectType){
		Example example=new Example(RegulaObjectType.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");
		if(StringUtils.isNotBlank(regulaObjectType.getObjectTypeCode())) {
			criteria.andLike("objectTypeCode","%"+ regulaObjectType.getObjectTypeCode()+"%");
		}
		if(StringUtils.isNotBlank(regulaObjectType.getObjectTypeName())) {
			criteria.andLike("objectTypeName","%"+ regulaObjectType.getObjectTypeName()+"%");
		}
		if(StringUtils.isNotBlank(regulaObjectType.getIsEnable())) {
			criteria.andEqualTo("isEnable", regulaObjectType.getIsEnable());
		}
		
		example.setOrderByClause("id desc");
		
		Page<Object> result = PageHelper.startPage(page, limit);
		List<RegulaObjectType> list = this.mapper.selectByExample(example);
		return new TableResultResponse<>(result.getTotal(),list);
	}
	
	/**
	 * 打量删除对象
	 * @author 尚
	 * @param ids
	 */
	public void remove(Integer[] ids) {
		this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), new Date());
	}
}