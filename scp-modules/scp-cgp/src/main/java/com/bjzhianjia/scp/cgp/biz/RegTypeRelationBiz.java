package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.RegTypeRelation;
import com.bjzhianjia.scp.cgp.mapper.RegTypeRelationMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 用于配置监管对象类型与业务之间的关系，如在某种情况下只需要某几个类型的监管对象类型
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 11:04:06
 */
@Service
public class RegTypeRelationBiz extends BusinessBiz<RegTypeRelationMapper,RegTypeRelation> {
	
	
	public List<RegTypeRelation> getList(RegTypeRelation regTypeRelation){
		Example example=new Example(RegTypeRelation.class);
		Criteria criteria = example.createCriteria();
		
		if(StringUtils.isNotBlank(regTypeRelation.getCode())) {
			criteria.andEqualTo("code", regTypeRelation.getCode());
		}
		if(StringUtils.isNotBlank(regTypeRelation.getProjectSign())) {
			criteria.andEqualTo("projectSign", regTypeRelation.getProjectSign());
		}
		
		List<RegTypeRelation> list = this.mapper.selectByExample(example);
		return list;
	}
}