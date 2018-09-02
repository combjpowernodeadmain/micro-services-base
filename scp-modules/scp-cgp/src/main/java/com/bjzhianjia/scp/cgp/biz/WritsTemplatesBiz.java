package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.constances.WorkFlowConstances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.mapper.WritsTemplatesMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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
	
	/**
	 * 按ID集合获取对象记录
	 * @author 尚
	 * @param ids
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<WritsTemplates> getListByNode(String node,int page,int limit){
		String ids="";
		
		ids = getWritsTemplateIds(node, ids);
		
		TableResultResponse<WritsTemplates> restResult=new TableResultResponse<>();
		
		if(StringUtils.isBlank(ids)) {
			restResult.setStatus(400);
			restResult.setMessage("请指定待查询节点");
			return restResult;
		}
		
		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<WritsTemplates> list = this.mapper.selectByIds(ids);
		
		restResult.getData().setTotal(pageInfo.getTotal());
		restResult.getData().setRows(list);
		
		return restResult;
	}

	private String getWritsTemplateIds(String node, String ids) {
		switch(node) {
		case WorkFlowConstances.WritsTemplateIdsInNode.SPOT_CHECK:
			ids=WorkFlowConstances.WritsTemplateIdsInNode.SPOT_CHECK_IDS;
			break;
		case WorkFlowConstances.WritsTemplateIdsInNode.SPOT_PUNISHMENT:
			ids=WorkFlowConstances.WritsTemplateIdsInNode.SPOT_PUNISHMENT_IDS;
			break;
		case WorkFlowConstances.WritsTemplateIdsInNode.RECTIFICATION:
			ids=WorkFlowConstances.WritsTemplateIdsInNode.RECTIFICATION_IDS;
			break;
		case WorkFlowConstances.WritsTemplateIdsInNode.INFORM:
			ids=WorkFlowConstances.WritsTemplateIdsInNode.INFORM_IDS;
			break;
		}
		return ids;
	}
}