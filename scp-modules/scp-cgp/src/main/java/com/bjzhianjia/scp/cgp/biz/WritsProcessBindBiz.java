package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.constances.WorkFlowConstances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsProcessBind;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.mapper.WritsProcessBindMapper;
import com.bjzhianjia.scp.cgp.mapper.WritsTemplatesMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.WritsProcessBindVo;
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
	@Autowired
	private WritsTemplatesMapper writsTemplatesMapper;
	
	/**
	 * 按分布查询记录
	 * @author 尚
	 * @param writsProcessBind
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<WritsProcessBindVo> getList(WritsProcessBind writsProcessBind,int page,int limit){
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
		
		List<String> writsIdList = list.stream().map(o->String.valueOf(o.getWritsId())).distinct().collect(Collectors.toList());
		List<WritsProcessBindVo> voList = BeanUtil.copyBeanList_New(list, WritsProcessBindVo.class);
		
		List<WritsTemplates> writsTemplageList = writsTemplatesMapper.selectByIds(String.join(",", writsIdList));
		Map<Integer, String> writsTemplateMap = writsTemplageList.stream().collect(Collectors.toMap(WritsTemplates::getId, WritsTemplates::getName));
		
		for(WritsProcessBindVo tmp:voList) {
			tmp.setWritsName(writsTemplateMap.get(tmp.getWritsId()));
		}
		return new TableResultResponse<>(pageInfo.getTotal(), voList);
	}
	
	/**
	 * 分页获取记录列表<br/>
	 * 部分字段数据
	 * @author 尚
	 * @param writsProcessBind
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<WritsProcessBindVo> getListOfCurrentNode(WritsProcessBind writsProcessBind,int page,int limit){
		TableResultResponse<WritsProcessBindVo> restResult = getList(writsProcessBind, page, limit);
		List<WritsProcessBindVo> voRows = restResult.getData().getRows();
		
		List<WritsProcessBindVo> resultList=new ArrayList<>();
		for(WritsProcessBindVo vo:voRows) {
			WritsProcessBindVo resultVo=new WritsProcessBindVo();
			resultVo.setId(vo.getId());
			resultVo.setWritsId(vo.getWritsId());
			resultVo.setWritsName(vo.getWritsName());
			resultVo.setIsDefault(vo.getIsDefault());
			resultList.add(resultVo);
		}
		
		return new TableResultResponse<WritsProcessBindVo>(restResult.getData().getTotal(), resultList);
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
	/**
	 * 添加单个对象<br/>
	 * 文书模板以名称形式传入
	 * @author 尚
	 * @param writsProcessBind
	 * @return
	 */
	public Result<Void> createWritsProcessBind(WritsProcessBind writsProcessBind,String writsTemplateName){
		Result<Void> result=new Result<>();
		
		if(StringUtils.isBlank(writsTemplateName)) {
			result.setIsSuccess(false);
			result.setMessage("请指定文书模板名称");
			return result;
		}
		Result<Void> setResult = setWritsTemplateId(writsProcessBind,writsTemplateName);
		if(!setResult.getIsSuccess()) {
			result.setIsSuccess(false);
			result.setMessage(setResult.getMessage());
			return result;
		}
		
		Result<Void> check = check(writsProcessBind);
		if(!check.getIsSuccess()) {
			result.setIsSuccess(false);
			result.setMessage(check.getMessage());
			return result;
		}
		
		this.insertSelective(writsProcessBind);
		
		result.setIsSuccess(true);
		return result;
	}

	private Result<Void> setWritsTemplateId(WritsProcessBind writsProcessBind,String writsTemplateName) {
		Result<Void> result=new Result<>();
		
		Example example=new Example(WritsTemplates.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDeleted", "0");
		criteria.andEqualTo("name", writsTemplateName);
		
		List<WritsTemplates> writsTemplaeList = writsTemplatesMapper.selectByExample(example);
		if(writsTemplaeList!=null&&!writsTemplaeList.isEmpty()) {
			//文书模板名称唯一，如果有返回结果 ，则结果 集只有一个
			writsProcessBind.setWritsId(writsTemplaeList.get(0).getId());
		}else {
			result.setIsSuccess(false);
			result.setMessage("该文书模板不存在");
			return result;
		}
		
		result.setIsSuccess(true);
		return result;
	}

	private Result<Void> check(WritsProcessBind writsProcessBind) {
		Result<Void> result =new Result<>();
		
		//process_def_id，process_node_id，writs_id三者联合唯一
		Example example=new Example(WritsProcessBind.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");
		criteria.andEqualTo("processDefId", writsProcessBind.getProcessDefId());
		criteria.andEqualTo("processNodeId", writsProcessBind.getProcessNodeId());
		criteria.andEqualTo("writsId", writsProcessBind.getWritsId());
		
		List<WritsProcessBind> bindInDB = this.selectByExample(example);
		if(bindInDB!=null&&!bindInDB.isEmpty()) {
			result.setIsSuccess(false);
			result.setMessage("该节点下已存在相应文书模板");
			return result;
		}
		
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
		
		result.setIsSuccess(true);
		return result;
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
