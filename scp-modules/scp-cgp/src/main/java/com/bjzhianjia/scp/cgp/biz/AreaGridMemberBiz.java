package com.bjzhianjia.scp.cgp.biz;

import static org.hamcrest.CoreMatchers.endsWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMemberMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 网格成员
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Service
public class AreaGridMemberBiz extends BusinessBiz<AreaGridMemberMapper, AreaGridMember> {
	/**
	 * 按条件获取网格成员对象集合
	 * 
	 * @author 尚
	 * @param conditions
	 * @return
	 */
	public List<AreaGridMember> getByMap(Map<String, Object> conditions) {
		Example example = new Example(AreaGridMember.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");

		Set<String> keySet = conditions.keySet();
		for (String string : keySet) {
			criteria.andEqualTo(string, conditions.get(string));
		}

		List<AreaGridMember> list = this.mapper.selectByExample(example);

		return list;
	}

	/**
	 * 
	 * @author 尚
	 * @param conditionList
	 * @return
	 */
	public List<AreaGridMember> getAreaGridMember(List<AreaGridMember> conditionList) {
		Example example = new Example(AreaGridMember.class);

		for (AreaGridMember areaGridMember : conditionList) {
			Criteria criteria = example.or();
			criteria.andEqualTo("isDeleted", "0");
			criteria.andEqualTo("gridMember", areaGridMember.getGridMember());
			criteria.andEqualTo("gridRole", areaGridMember.getGridRole());
			criteria.andEqualTo("gridId", areaGridMember.getGridId());
		}

		List<AreaGridMember> list = this.mapper.selectByExample(example);
		return list;
	}
	
	/**
	 * 按gridId删除网格成员记录
	 * @author 尚
	 * @param ids
	 */
	public void deleteByGridId(List<Integer> delList) {
		Example example=new Example(AreaGridMember.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andIn("gridId", delList);
		
		this.mapper.deleteByExample(example);
	}
	
	/**
	 * 按gridId删除网格成员记录
	 * @author 尚
	 * @param ids
	 */
	public void deleteByGridId(Integer id) {
		Example example=new Example(AreaGridMember.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("gridId", id);
		
		this.mapper.deleteByExample(example);
	}
}