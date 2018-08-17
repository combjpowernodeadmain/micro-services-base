package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.mapper.ExecuteInfoMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * 案件办理情况
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-16 19:16:02
 */
@Service
public class ExecuteInfoBiz extends BusinessBiz<ExecuteInfoMapper,ExecuteInfo> {
	
	/**
	 * 分页获取对象
	 * @author 尚
	 * @param executeInfo
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<ExecuteInfo> getList(ExecuteInfo executeInfo,int page,int limit){
		Example example=new Example(ExecuteInfo.class);
		
		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<ExecuteInfo> rows = this.mapper.selectByExample(example);
		
		return new TableResultResponse<>(pageInfo.getTotal(), rows);
	}
}