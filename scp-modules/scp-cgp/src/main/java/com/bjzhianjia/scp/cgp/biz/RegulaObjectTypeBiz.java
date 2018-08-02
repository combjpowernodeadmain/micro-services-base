package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectTypeMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 监管对象
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:26
 */
@Service
public class RegulaObjectTypeBiz extends BusinessBiz<RegulaObjectTypeMapper, RegulaObjectType> {
	/**
	 * 查询id最大的那条记录
	 * 
	 * @author 尚
	 * @return
	 */
	public RegulaObjectType getTheMaxOne() {
		Example example = new Example(RegulaObjectType.class);
		example.setOrderByClause("id desc");
		PageHelper.startPage(0, 1);
		List<RegulaObjectType> RegulaObjectType = this.mapper.selectByExample(example);
		if (RegulaObjectType != null && !RegulaObjectType.isEmpty()) {
			return RegulaObjectType.get(0);
		}
		return null;
	}


}