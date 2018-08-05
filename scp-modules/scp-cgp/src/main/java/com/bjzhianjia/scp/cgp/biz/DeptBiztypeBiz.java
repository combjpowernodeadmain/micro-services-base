package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.DeptBiztype;
import com.bjzhianjia.scp.cgp.mapper.DeptBiztypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.DeptBizTypeVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * 
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-19 09:20:01
 */
@Service
public class DeptBiztypeBiz extends BusinessBiz<DeptBiztypeMapper, DeptBiztype> {
	@Autowired
	private DeptBiztypeMapper deptBiztypeMapper;
	@Autowired
	private DeptUtilBiz deptUtilBiz;

	/**
	 * 根据条件进行分布查询
	 * 
	 * @param page        起始页
	 * @param limit       页容量
	 * @param deptBiztype 封装有查询条件的DeptBizType对象
	 * @return 与查询条件相符的查询结果
	 */
	public TableResultResponse<DeptBizTypeVo> getList(int page, int limit, DeptBiztype deptBiztype) {
		Example example = new Example(DeptBiztype.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");
		if (StringUtils.isNotBlank(deptBiztype.getBizType())) {
			criteria.andLike("bizType", "%" + deptBiztype.getBizType() + "%");
//			criteria.andEqualTo("bizType",deptBiztype.getBizType());
		}
		// 按部门查询的，如果输入的部门条件为父部门，则将其子部门相关记录一并查出
		if (StringUtils.isNotBlank(deptBiztype.getDepartment())) {
//			String deptStrs=deptBiztype.getDepartment();
//			String[] split = deptStrs.split(",");
			List<String> deptList = deptUtilBiz.getDeptIds(deptBiztype.getDepartment());
			criteria.andIn("department", deptList);
//			criteria.andEqualTo("department",deptBiztype.getDepartment());
		}

		example.setOrderByClause("id desc");

		Page<Object> result = PageHelper.startPage(page, limit);
		List<DeptBiztype> list = deptBiztypeMapper.selectByExample(example);
		List<DeptBizTypeVo> listVo = new ArrayList<>();
		for (DeptBiztype tmp : list) {
			DeptBizTypeVo copyBean_New = BeanUtil.copyBean_New(tmp, new DeptBizTypeVo());
			listVo.add(copyBean_New);
		}
		TableResultResponse<DeptBizTypeVo> returnResult = new TableResultResponse<>(result.getTotal(), listVo);
		return returnResult;
	}

	/**
	 * 按部门获取业务条线记录<br/>
	 * 
	 * @param deptName 部门条件
	 * @return
	 */
	public List<DeptBiztype> getByDept(String deptName) {
		Example example = new Example(DeptBiztype.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");
		criteria.andEqualTo("department", deptName);

		List<DeptBiztype> deptBiztypeInDB = deptBiztypeMapper.selectByExample(example);
		return deptBiztypeInDB;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		/*
		 * 未加更新人及更新时间，这块逻辑需要添加上
		 */
		deptBiztypeMapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getName(), new Date());
	}
}