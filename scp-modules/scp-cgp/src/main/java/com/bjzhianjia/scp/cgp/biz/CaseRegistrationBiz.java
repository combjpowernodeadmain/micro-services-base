package com.bjzhianjia.scp.cgp.biz;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.mapper.CaseRegistrationMapper;
import com.bjzhianjia.scp.cgp.vo.CaseRegistrationVo;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 综合执法 - 案件登记
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:08
 */
@Service
public class CaseRegistrationBiz extends BusinessBiz<CaseRegistrationMapper, CaseRegistration> {
	@Autowired
	private CLEConcernedCompanyBiz cLEConcernedCompanyBiz;
	@Autowired
	private CLEConcernedPersonBiz cLEConcernedPersonBiz;

	/**
	 * 添加立案记录，如果有当事人，则一并添加当事人
	 * 
	 * @author 尚
	 * @param procBizData
	 */
	public Result<Void> addCase(JSONObject caseRegJObj) {
		Result<Void> result = new Result<>();

		// 添加当事人
		int concernedId = addConcerned(caseRegJObj);

		// 添加立案单
		CaseRegistration caseRegistration = JSON.parseObject(caseRegJObj.toJSONString(), CaseRegistration.class);
		// 生成caseRegistration主键
		String id = UUIDUtils.generateUuid();
		caseRegistration.setId(id);
		// 当事人主键
		if (concernedId != -1) {
			caseRegistration.setConcernedId(concernedId);
		}

		this.insertSelective(caseRegistration);
		// 将生成的立案ID装入procBizData带回工作流，在工作流中会对procBizId属性进行是否为“-1”的判断，如果是“-1”，将用该ID替换“-1”
		caseRegJObj.put("procBizId", id);

		result.setIsSuccess(true);
		return result;
	}

	/**
	 * 添加当事人记录
	 * 
	 * @author 尚
	 * @param caseRegJObj
	 * @return
	 */
	private int addConcerned(JSONObject caseRegJObj) {
		int resultId = -1;

		// 判断是否传入当事人信息
		JSONObject concernedJObj = caseRegJObj.getJSONObject("concerned");
		if (concernedJObj != null) {
			// 有当事人信息
			String concernedType = caseRegJObj.getString("concernedType");

			switch (concernedType) {
			case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG:
				// 当事人以单位形式存在
				CLEConcernedCompany concernedCompany = JSON.parseObject(concernedJObj.toJSONString(),
						CLEConcernedCompany.class);
				cLEConcernedCompanyBiz.insertSelective(concernedCompany);
				resultId = concernedCompany.getId();
				break;
			case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON:
				// 当事人以人个形式存在
				CLEConcernedPerson concernedPerson = JSON.parseObject(concernedJObj.toJSONString(),
						CLEConcernedPerson.class);
				cLEConcernedPersonBiz.insertSelective(concernedPerson);
				resultId = concernedPerson.getId();
				break;
			}
		}

		return resultId;
	}

	/**
	 * 分页获取列表
	 * 
	 * @author 尚
	 * @param ids
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<CaseRegistrationVo> getListByIds(String ids, int page, int limit) {
		Set<String> idSet = new HashSet<>();

		String[] split = ids.split(",");
		for (String string : split) {
			idSet.add(string);
		}

		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<CaseRegistrationVo> list = this.mapper.getListByIds(idSet, page, limit);

		return new TableResultResponse<CaseRegistrationVo>(pageInfo.getTotal(), list);
	}
	
	/**
	 * 按执法人分页查询对象
	 * @param userId
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<CaseRegistration> getListByExecutePerson(String userId,int page,int limit){
	    //enforcers
	    TableResultResponse<CaseRegistration> restResult=new TableResultResponse<>();
	    
	    Example example=new Example(CaseRegistration.class);
	    Criteria criteria = example.createCriteria();
	    
	    criteria.andEqualTo("isDeleted", "0");
	    criteria.andEqualTo("enforcers", userId);
	    
	    Page<Object> pageInfo = PageHelper.startPage(page, limit);
	    List<CaseRegistration> rows=this.selectByExample(example);
	    
	    restResult.getData().setTotal(pageInfo.getTotal());
	    restResult.getData().setRows(rows);
	    restResult.setStatus(200);
	    return restResult;
	}
}