package com.bjzhianjia.scp.cgp.biz;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import com.bjzhianjia.scp.cgp.mapper.EnforceTerminalMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 执法终端
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Service
public class EnforceTerminalBiz extends BusinessBiz<EnforceTerminalMapper,EnforceTerminal> {
	
	@Autowired
	private EnforceTerminalMapper terminalMapper;
	
	/**
	 * 根据终端号获取终端
	 * @param terminalCode 终端号
	 * @return
	 */
	public EnforceTerminal getByTerminalCode(String terminalCode) {
		
		Example example = new Example(EnforceTerminal.class);
		
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("terminalCode", terminalCode);
		
		List<EnforceTerminal> list = terminalMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			return null;
		}
		
		EnforceTerminal enforceTerminal = list.get(0);
		
		if(enforceTerminal.getIsDeleted().equals("1")) {
			return null;
		}
		
		return enforceTerminal;
	}
	
	/**
	 * 根据终端手机号获取终端
	 * @param terminalPhone 终端手机号
	 * @return
	 */
	public EnforceTerminal getByTerminalPhone(String terminalPhone) {
		Example example = new Example(EnforceTerminal.class);
		
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("terminalPhone", terminalPhone);
		
		List<EnforceTerminal> list = terminalMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			return null;
		}
		
		EnforceTerminal enforceTerminal = list.get(0);
		
		if(enforceTerminal.getIsDeleted().equals("1")) {
			return null;
		}
		
		return enforceTerminal;
	}
	
	/**
	 * 根据终端号获取终端
	 * @param terminalCode 终端号
	 * @return
	 */
	public Map<Integer, String> map() {
		
		Example example = new Example(EnforceTerminal.class);
		
		Criteria criteria = example.createCriteria();
		 criteria.andEqualTo("isDeleted", "0");
		
		List<EnforceTerminal> list = terminalMapper.selectByExample(example);
		
		return list.stream().collect(Collectors.toMap(EnforceTerminal::getId, EnforceTerminal::getTerminalCode));
	}
	
	/**
	 * 根据查询条件搜索
	 * @param terminal
	 * @return
	 */
	public TableResultResponse<EnforceTerminal> getList(int page, int limit, EnforceTerminal terminal) {
		Example example = new Example(EnforceTerminal.class);
	    Example.Criteria criteria = example.createCriteria();
	    
	    criteria.andEqualTo("isDeleted", "0");
	    if(StringUtils.isNotBlank(terminal.getTerminalPhone())){
	    	criteria.andEqualTo("terminalPhone", terminal.getTerminalPhone());
	    }
	    if(StringUtils.isNotBlank(terminal.getRetrievalDepartment())){
	    	criteria.andEqualTo("retrievalDepartment", terminal.getRetrievalDepartment());
	    }
	    if(StringUtils.isNotBlank(terminal.getRetrievalUser())){
	    	criteria.andEqualTo("retrievalUser", terminal.getRetrievalUser());
	    }
	    if(StringUtils.isNotBlank(terminal.getTerminalType())){
	    	criteria.andEqualTo("terminalType", terminal.getTerminalType());
	    }
	    if(StringUtils.isNotBlank(terminal.getIsEnable())){
	    	criteria.andEqualTo("isEnable", terminal.getIsEnable());
	    }
	    
	    example.setOrderByClause("id desc");

	    Page<Object> result = PageHelper.startPage(page, limit);
	    List<EnforceTerminal> list = terminalMapper.selectByExample(example);
	    return new TableResultResponse<EnforceTerminal>(result.getTotal(), list);
	}
	
	/**
	 * 根据编号获取终端
	 * @param terminalPhone 终端手机号
	 * @return
	 */
	public EnforceTerminal getById(Integer id) {
		
		EnforceTerminal enforceTerminal = terminalMapper.selectByPrimaryKey(id);
		
		if(enforceTerminal != null && enforceTerminal.getIsDeleted().equals("1")) {
			return null;
		}

		return enforceTerminal;
	}
	
	/**
	 * 删除终端
	 * @param id 标识
	 */
	public void deleteById(Integer id) {

		terminalMapper.deleteById(id);
    }
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		terminalMapper.deleteByIds(ids);
	}
}