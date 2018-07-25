package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import com.bjzhianjia.scp.cgp.feign.IUserFeign;
import com.bjzhianjia.scp.cgp.mapper.EnforceTerminalMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
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
public class EnforceTerminalBiz extends BusinessBiz<EnforceTerminalMapper, EnforceTerminal> {

	@Autowired
	private EnforceTerminalMapper terminalMapper;
	@Autowired
	private IUserFeign iUserFeign;
	@Autowired
	private DeptUtilBiz deptUtilBiz;

	/**
	 * 根据终端号获取终端
	 * 
	 * @param terminalCode 终端号
	 * @return
	 */
	public EnforceTerminal getByTerminalCode(String terminalCode) {

		Example example = new Example(EnforceTerminal.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("terminalCode", terminalCode);

		List<EnforceTerminal> list = terminalMapper.selectByExample(example);

		if (list == null || list.size() == 0) {
			return null;
		}

		EnforceTerminal enforceTerminal = list.get(0);

		if (enforceTerminal.getIsDeleted().equals("1")) {
			return null;
		}

		return enforceTerminal;
	}

	/**
	 * 根据终端手机号获取终端
	 * 
	 * @param terminalPhone 终端手机号
	 * @return
	 */
	public EnforceTerminal getByTerminalPhone(String terminalPhone) {
		Example example = new Example(EnforceTerminal.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("terminalPhone", terminalPhone);

		List<EnforceTerminal> list = terminalMapper.selectByExample(example);

		if (list == null || list.size() == 0) {
			return null;
		}

		EnforceTerminal enforceTerminal = list.get(0);

		if (enforceTerminal.getIsDeleted().equals("1")) {
			return null;
		}

		return enforceTerminal;
	}

	/**
	 * 根据终端号获取终端
	 * 
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
	 * 
	 * @param terminal
	 * @return
	 */
	public TableResultResponse<EnforceTerminal> getList(int page, int limit, EnforceTerminal terminal) {
		Example example = new Example(EnforceTerminal.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");
		if (StringUtils.isNotBlank(terminal.getTerminalPhone())) {
			criteria.andEqualTo("terminalPhone", terminal.getTerminalPhone());
		}
		//按部门查询的，如果输入的部门条件为父部门，则将其子部门相关记录一并查出
		if (StringUtils.isNotBlank(terminal.getRetrievalDepartment())) {
			List<String> deptIds = deptUtilBiz.getDeptIds(terminal.getRetrievalDepartment());
			criteria.andIn("retrievalDepartment", deptIds);
//			criteria.andEqualTo("retrievalDepartment", terminal.getRetrievalDepartment());
		}
		if (StringUtils.isNotBlank(terminal.getRetrievalUser())) {
			JSONArray usersJsonArray = iUserFeign.getUsersByFakeName(terminal.getRetrievalUser());
			List<String> list = new ArrayList<>();
			for (int i = 0; i < usersJsonArray.size(); i++) {
				JSONObject jsonObject = usersJsonArray.getJSONObject(i);
				list.add(jsonObject.getString("id"));
			}
			if(list==null||list.size()==0) {
				/*
				 * 当list为空时，说明没有相关人员
				 * 如果 此时进行注入criteria.andIn("retrievalUser", list),将发生org.mybatis.spring.MyBatisSystemException
				 * <br/>而此时如果不注入criteria.andIn("retrievalUser", list)，则会忽略按名称查询，与需要不符<br/>
				 * 所以通过向list集合中添加一个“-1”的值，使list不为空<br/>
				 * 此时数据库中retrieval——user字段对应值不应为“-1”
				 * 
				 */
				list.add("-1");
			}
			criteria.andIn("retrievalUser", list);
		}
		if (StringUtils.isNotBlank(terminal.getTerminalType())) {
			criteria.andEqualTo("terminalType", terminal.getTerminalType());
		}
		if (StringUtils.isNotBlank(terminal.getIsEnable())) {
			criteria.andEqualTo("isEnable", terminal.getIsEnable());
		}

		example.setOrderByClause("id desc");

		Page<Object> result = PageHelper.startPage(page, limit);
		List<EnforceTerminal> list = terminalMapper.selectByExample(example);
		return new TableResultResponse<EnforceTerminal>(result.getTotal(), list);
	}

	/**
	 * 根据编号获取终端
	 * 
	 * @param terminalPhone 终端手机号
	 * @return
	 */
	public EnforceTerminal getById(Integer id) {

		EnforceTerminal enforceTerminal = terminalMapper.selectByPrimaryKey(id);

		if (enforceTerminal != null && enforceTerminal.getIsDeleted().equals("1")) {
			return null;
		}

		return enforceTerminal;
	}

	/**
	 * 删除终端
	 * 
	 * @param id 标识
	 */
	public void deleteById(Integer id) {

		terminalMapper.deleteById(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		terminalMapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getName(), new Date());
	}

	/**
	 * 根据手机号片段联想查询终端
	 * 
	 * @author wangkaige
	 * @param fakePhone 手机号片段
	 * @return
	 */
	public List<EnforceTerminal> mapPhone(String fakePhone) {

		Example example = new Example(EnforceTerminal.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDeleted", "0");
		criteria.andLike("terminalPhone", "%" + fakePhone + "%");

		PageHelper.startPage(1, 20);
		List<EnforceTerminal> list = terminalMapper.selectByExample(example);

		return list;
	}
}