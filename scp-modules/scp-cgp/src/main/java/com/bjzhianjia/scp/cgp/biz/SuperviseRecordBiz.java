package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.MessageCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.SuperviseRecord;
import com.bjzhianjia.scp.cgp.mapper.SuperviseRecordMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;


/**
 * SuperviseRecordBiz 事件督办记录.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月5日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
@Service
public class SuperviseRecordBiz extends BusinessBiz<SuperviseRecordMapper,SuperviseRecord> {
	@Autowired
	private SuperviseRecordMapper superviseRecordMapper;

	@Autowired
	private CaseInfoBiz caseInfoBiz;

	@Autowired
	private MessageCenterBiz messageCenterBiz;
	
	/**
	 * 通过立案单id，翻页查询
	 * 
	 * @param page       页码
	 * @param limit      页容量
	 * @param caseInfoId 立案单id
	 * @return
	 * 
	 */
	public TableResultResponse<SuperviseRecord> getList(Integer page, Integer limit, Integer caseInfoId) {
		Example example = new Example(SuperviseRecord.class);
		Example.Criteria criteria = example.createCriteria();
		example.setOrderByClause("id DESC");
	
		criteria.andEqualTo("caseInfoId", caseInfoId);

		Page<Object> result = PageHelper.startPage(page, limit);

		List<SuperviseRecord> list = superviseRecordMapper.selectByExample(example);
		return new TableResultResponse<SuperviseRecord>(result.getTotal(), list);
	}
	
	/**
	 * 新增
	 */
	public void createSuperviseRecord(SuperviseRecord superviseRecord){
	    //是否督办（0否| 1是）
	    String isSupervise = "1";
		//更新事件表督办状态
		Integer caseInfoId = superviseRecord.getCaseInfoId();
		CaseInfo caseInfo = caseInfoBiz.selectById(caseInfoId);
		if(caseInfo != null) {
		    if(!isSupervise.equals(caseInfo.getIsSupervise())){ //没有记录督办时，修改督办状态
	            caseInfo = new CaseInfo();
	            caseInfo.setId(caseInfoId);
	            caseInfo.setIsSupervise(isSupervise);
	            caseInfoBiz.updateSelectiveById(caseInfo);
	        }
	        super.insertSelective(superviseRecord);

			/*
			 * 添加督办消息
			 */
			MessageCenter messageCenter=new MessageCenter();
			messageCenter.setMsgSourceType("case_info_02");
			messageCenter.setMsgSourceId(String.valueOf(caseInfo.getId()));
			// 指明按哪个类型进行查询正常的消息记录
			JSONObject sourceJObj=new JSONObject();
			sourceJObj.put("msgSourceType", "case_info_00");

			messageCenterBiz.addMsgCenterRecord(messageCenter,sourceJObj);
		}
		
	}
}