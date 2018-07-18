package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.InspectItems;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

/**
 * 巡查事项
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-16 16:08:44
 */
public interface InspectItemsMapper extends CommonMapper<InspectItems>, DeleteByIdsMapper<InspectItems> {
	
}
