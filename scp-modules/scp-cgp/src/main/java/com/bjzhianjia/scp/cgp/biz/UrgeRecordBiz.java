package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.UrgeRecord;
import com.bjzhianjia.scp.cgp.mapper.UrgeRecordMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * 催办记录表
 *
 * @author chenshuai
 * @email
 * @version 2018-08-26 09:02:28
 */
@Service
public class UrgeRecordBiz extends BusinessBiz<UrgeRecordMapper, UrgeRecord> {

	@Autowired
	private UrgeRecordMapper urgeRecordMapper;
	
	@Autowired
	private CaseInfoBiz caseInfoBiz;

	/**
	 * 通过立案单id，翻页查询
	 * 
	 * @param page       页码
	 * @param limit      页容量
	 * @param caseInfoId 立案单id
	 * @return
	 * 
	 */
	public TableResultResponse<UrgeRecord> getList(Integer page, Integer limit, Integer caseInfoId) {
		Example example = new Example(UrgeRecord.class);
		Example.Criteria criteria = example.createCriteria();
		example.setOrderByClause("id DESC");
	
		criteria.andEqualTo("caseInfoId", caseInfoId);

		Page<Object> result = PageHelper.startPage(page, limit);

		List<UrgeRecord> list = urgeRecordMapper.selectByExample(example);
		return new TableResultResponse<UrgeRecord>(result.getTotal(), list);
	}
	/**
	 * 新增
	 */
	public void createUrgeRecord(UrgeRecord urgeRecord){
		//更新事件表催办状态
		Integer caseInfoId = urgeRecord.getCaseInfoId();
		CaseInfo caseInfo = caseInfoBiz.selectById(caseInfoId);
		if(caseInfo == null) {
			return;
		}
		if(!"1".equals(caseInfo.getIsUrge())){ //没有记录催办时，修改催办状态
			caseInfo = new CaseInfo();
			caseInfo.setId(caseInfoId);
			caseInfo.setIsUrge("1");
			caseInfoBiz.updateSelectiveById(caseInfo);
		}
		super.insertSelective(urgeRecord);
	}
}