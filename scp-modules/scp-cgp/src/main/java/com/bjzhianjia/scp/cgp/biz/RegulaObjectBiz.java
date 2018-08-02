package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
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
public class RegulaObjectBiz extends BusinessBiz<RegulaObjectMapper, RegulaObject> {
	/**
	 * 查询id最大的那条记录
	 * 
	 * @author 尚
	 * @return
	 */
	public RegulaObject getTheMaxOne() {
		Example example = new Example(RegulaObject.class);
		example.setOrderByClause("id desc");
		PageHelper.startPage(0, 1);
		List<RegulaObject> regulaObject = this.mapper.selectByExample(example);
		if (regulaObject != null && !regulaObject.isEmpty()) {
			return regulaObject.get(0);
		}
		return null;
	}

	/**
	 * 按条件分页查询
	 * @author 尚
	 * @param regulaObject
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<RegulaObject> getList(RegulaObject regulaObject, int page, int limit) {
		Example example=new Example(RegulaObject.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("isDeleted", "0");
		
		if(StringUtils.isNotBlank(regulaObject.getObjName())) {
			criteria.andLike("objName", "%"+regulaObject.getObjName());
		}
		
		if(StringUtils.isNotBlank(regulaObject.getObjType())) {
			criteria.andLike("objType", regulaObject.getObjType());
		}
		
		if(StringUtils.isNotBlank(regulaObject.getBizList())) {
			criteria.andLike("bizList", regulaObject.getBizList());
		}
		
		example.setOrderByClause("crt_time desc");
		
		Page<Object> result = PageHelper.startPage(page, limit);
		List<RegulaObject> list = this.mapper.selectByExample(example);
		return new TableResultResponse<RegulaObject>(result.getTotal(), list);
	}
}