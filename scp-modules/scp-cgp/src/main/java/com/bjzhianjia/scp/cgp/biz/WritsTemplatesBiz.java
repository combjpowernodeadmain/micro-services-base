package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.mapper.WritsTemplatesMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 文书模板
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Service
public class WritsTemplatesBiz extends BusinessBiz<WritsTemplatesMapper,WritsTemplates> {
	
	/**
	 * 添加单个对象
	 * @author 尚
	 * @param writsTemplates
	 * @return
	 */
	public Result<Void> created(WritsTemplates writsTemplates){
		Result<Void> result=check(writsTemplates);
		if(!result.getIsSuccess()) {
			return result;
		}
		
		this.insertSelective(writsTemplates);
		
		result.setIsSuccess(true);
		return result;
	}

	private Result<Void> check(WritsTemplates writsTemplates) {
		Result<Void> result=new Result<>();
		
		Example example=new Example(WritsTemplates.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDeleted", "0");
		//验证名称是否重复
		if(StringUtils.isNotBlank(writsTemplates.getName())) {
			criteria.andEqualTo("name",writsTemplates.getName());
			List<WritsTemplates> instanceInDBList = this.selectByExample(example);
			if(instanceInDBList!=null&&!instanceInDBList.isEmpty()) {
				result.setIsSuccess(false);
				result.setMessage("模板名称已存在");
				return result;
			}
		}
		
		result.setIsSuccess(true);
		return result;
	}
}