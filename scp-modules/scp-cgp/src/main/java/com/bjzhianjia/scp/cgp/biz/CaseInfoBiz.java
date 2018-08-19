package com.bjzhianjia.scp.cgp.biz;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 预立案信息
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-08 16:14:17
 */
@Service
public class CaseInfoBiz extends BusinessBiz<CaseInfoMapper,CaseInfo> {
	/**
	 * 查询ID最大的那条记录
	 * @author 尚
	 * @return
	 */
	public CaseInfo getMaxOne() {
		Example example=new Example(CaseInfo.class);
		example.setOrderByClause("id desc");
		PageHelper.startPage(1, 1);
		List<CaseInfo> caseInfoList = this.mapper.selectByExample(example);
		if(caseInfoList==null||caseInfoList.isEmpty()) {
			return null;
		}
		return caseInfoList.get(0);
	}
	
	/**
	 * 按条件查询
	 * @author 尚
	 * @param conditions
	 * @return
	 */
	public List<CaseInfo> getByMap(Map<String, Object> conditions){
		Example example=new Example(CaseInfo.class);
		Criteria criteria = example.createCriteria();
		
		Set<String> keySet = conditions.keySet();
		for(String key:keySet) {
			criteria.andEqualTo(key, conditions.get(key));
		}
		
		List<CaseInfo> list = this.selectByExample(example);
		return list;
	}
	
	/**
	 * 按分布获取对象
	 * @author 尚
	 * @param caseInfo
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo,int page,int limit){
		Example example =new Example(CaseInfo.class);
		
		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<CaseInfo> list = this.mapper.selectByExample(example);
		
		return new TableResultResponse<CaseInfo>(pageInfo.getTotal(), list);
	}
}