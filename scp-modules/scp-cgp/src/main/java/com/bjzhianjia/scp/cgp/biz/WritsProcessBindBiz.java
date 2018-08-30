package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.constances.WorkFlowConstances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsProcessBind;
import com.bjzhianjia.scp.cgp.mapper.WritsProcessBindMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 文书与流程绑定
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Service
public class WritsProcessBindBiz extends BusinessBiz<WritsProcessBindMapper,WritsProcessBind> {
	
	/**
	 * 按分布查询记录
	 * @author 尚
	 * @param writsProcessBind
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<WritsProcessBind> getList(WritsProcessBind writsProcessBind,int page,int limit){
		Example example=new Example(WritsProcessBind.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");
		if(StringUtils.isNotBlank(writsProcessBind.getProcessDefId())) {
			criteria.andEqualTo("processDefId", writsProcessBind.getProcessDefId());
		}
		if(StringUtils.isNotBlank(writsProcessBind.getProcessNodeId())) {
			criteria.andEqualTo("processNodeId", writsProcessBind.getProcessNodeId());
		}
		
		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<WritsProcessBind> list = this.selectByExample(example);
		
		return new TableResultResponse<>(pageInfo.getTotal(), list);
	}
	
	/**
	 * 批量删除记录
	 * @author 尚
	 * @param ids
	 * @return
	 */
	public Result<Void> remove(Integer[] ids){
		Result<Void> result=new Result<>();
		
		this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), new Date());
		
		result.setIsSuccess(true);
		return result;
	}
	
	/**
	 * 添加单个对象
	 * @author 尚
	 * @param writsProcessBind
	 * @return
	 */
	public Result<Void> createWritsProcessBind(WritsProcessBind writsProcessBind){
		Result<Void> result=new Result<>();
		
		check(writsProcessBind);
		
		this.insertSelective(writsProcessBind);
		
		result.setIsSuccess(true);
		return result;
	}

	private void check(WritsProcessBind writsProcessBind) {
		//判断当前审批是几级
		if(writsProcessBind.getProcessNodeId().endsWith(WorkFlowConstances.ProcessNode.SQUADRONLEADER_SUFFIX)) {
			//中队领导
			writsProcessBind.setApprovalRating(1);
		}else if(writsProcessBind.getProcessNodeId().endsWith(WorkFlowConstances.ProcessNode.LEGAL_SUFFIX)) {
			//法治科
			writsProcessBind.setApprovalRating(2);
		}else {
			writsProcessBind.setApprovalRating(3);
		}
	}
	
	/**
	 * 更新单个对象
	 * @author 尚
	 * @param writsProcessBind
	 * @return
	 */
	public Result<Void> update(WritsProcessBind writsProcessBind){
		Result<Void> result=new Result<>();
		
		check(writsProcessBind);
		
		this.updateSelectiveById(writsProcessBind);
		
		result.setIsSuccess(true);
		return result;
	}
}
