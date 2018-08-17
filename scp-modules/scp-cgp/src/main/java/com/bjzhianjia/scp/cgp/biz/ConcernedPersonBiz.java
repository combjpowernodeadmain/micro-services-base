package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.mapper.ConcernedPersonMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * 立案信息当事人
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-16 19:16:02
 */
@Service
public class ConcernedPersonBiz extends BusinessBiz<ConcernedPersonMapper,ConcernedPerson> {
	
	/**
	 * 分页获取对象集合
	 * @author 尚
	 * @param concernedPerson
	 * @return
	 */
	public TableResultResponse<ConcernedPerson> getList(ConcernedPerson concernedPerson,int page,int limit){
		Example example = new Example(ConcernedPerson.class);

		example.setOrderByClause("id desc");
		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		
		List<ConcernedPerson> rows=this.mapper.selectByExample(example);
		return new TableResultResponse<>(pageInfo.getTotal(), rows);
	}
}