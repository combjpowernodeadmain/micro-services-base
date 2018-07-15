package com.bjzhianjia.scp.cgp.mapper;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 执法终端
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
public interface EnforceTerminalMapper extends CommonMapper<EnforceTerminal> {
	
	/**
	 * 删除终端
	 * @param id 标识
	 */
	public void deleteById(@Param("id")Integer id);
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids")Integer[] ids);
}
