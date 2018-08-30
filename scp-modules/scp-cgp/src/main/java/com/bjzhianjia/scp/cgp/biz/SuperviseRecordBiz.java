package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

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
 * 督办记录
 *
 * @author chenshuai
 * @email 
 * @version 2018-08-26 09:02:29
 */
@Service
public class SuperviseRecordBiz extends BusinessBiz<SuperviseRecordMapper,SuperviseRecord> {
	@Autowired
	private SuperviseRecordMapper superviseRecordMapper;

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
		//更新事件表督办状态
		Integer caseInfoId = superviseRecord.getCaseInfoId();
		CaseInfo caseInfo = caseInfoBiz.selectById(caseInfoId);
		if(caseInfo == null) {
			return;
		}
		if(!"1".equals(caseInfo.getIsSupervise())){ //没有记录督办时，修改督办状态
			caseInfo = new CaseInfo();
			caseInfo.setId(caseInfoId);
			caseInfo.setIsSupervise("1");
			caseInfoBiz.updateSelectiveById(caseInfo);
		}
		super.insertSelective(superviseRecord);
	}
}