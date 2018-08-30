package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.constances.WorkFlowConstances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import com.bjzhianjia.scp.cgp.mapper.WritsInstancesMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * 文书模板实例
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Service
public class WritsInstancesBiz extends BusinessBiz<WritsInstancesMapper, WritsInstances> {

	/**
	 * 分页查询记录列表
	 * 
	 * @author 尚
	 * @param writsInstances
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<WritsInstances> getList(WritsInstances writsInstances, int page, int limit) {
		Example example = new Example(WritsInstances.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");
		if (StringUtils.isNotBlank(writsInstances.getCaseId())) {
			criteria.andEqualTo("caseId", writsInstances.getCaseId());
		}
		if (writsInstances.getTemplateId() != null) {
			criteria.andEqualTo("templateId", writsInstances.getTemplateId());
		}
		if (StringUtils.isNotBlank(writsInstances.getProcTaskId())) {
			criteria.andEqualTo("procTaskId", writsInstances.getProcTaskId());
		}

		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<WritsInstances> list = this.selectByExample(example);

		return new TableResultResponse<>(pageInfo.getTotal(), list);
	}

	/**
	 * 更新或插入对象
	 * @author 尚
	 * @param bizData
	 * @return
	 */
	@Transactional
	public Result<Void> updateOrInsert(JSONObject bizData) {
		Result<Void> result=new Result<>();
		
		WritsInstances writsInstances = JSON.parseObject(bizData.toJSONString(), WritsInstances.class);
		
		//判断当前处于哪个节点上(中队，法治科，镇局)
		String procNode=bizData.getString("processNode");
		
		String fillContext=writsInstances.getFillContext();

		if(writsInstances.getId()==null) {
			JSONObject jObjInDB = mergeFillContext(procNode, fillContext, null);
			
			//把处理后的文书内容(fillContext)放回，进行更新操作
			writsInstances.setFillContext(jObjInDB.toJSONString());
			
			//还没有插入过对象
			this.insertSelective(writsInstances);
		}else {
			/*
			 * 处理逻辑<br/>
			 * 判断当前是谁在审批——从已存在的记录中将审批记录(fill_context)取出，对json字符串进行处理
			 */
			WritsInstances writsInstancesInDB = this.selectById(writsInstances.getId());
			
			String fillContextInDB=writsInstancesInDB.getFillContext();
			
			JSONObject jObjInDB = JSONObject.parseObject(fillContextInDB);
			jObjInDB = mergeFillContext(procNode, fillContext, jObjInDB);
			
			//把处理后的文书内容(fillContext)放回，进行更新操作
			writsInstances.setFillContext(jObjInDB.toJSONString());
			
			this.updateSelectiveById(writsInstances);
		}
		
		result.setIsSuccess(true);
		return result;
	}

	private JSONObject mergeFillContext(String procNode, String fillContext, JSONObject jObjInDB) {
		if(jObjInDB==null) {
			jObjInDB=new JSONObject();
		}
		
		if(procNode.endsWith(WorkFlowConstances.ProcessNode.SQUADRONLEADER_SUFFIX)) {
			//中队领导
			jObjInDB.put("SquadronLeader", fillContext);
		}else if(procNode.endsWith(WorkFlowConstances.ProcessNode.LEGAL_SUFFIX)) {
			//法治科
			jObjInDB.put("Legal", fillContext);
		}else {
			//镇局
			jObjInDB.put("TownLeader", fillContext);
		}
		return jObjInDB;
	}
}