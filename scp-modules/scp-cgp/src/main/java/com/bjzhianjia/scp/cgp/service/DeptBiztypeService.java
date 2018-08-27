package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.DeptBiztypeBiz;
import com.bjzhianjia.scp.cgp.entity.DeptBiztype;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.DeptBizTypeVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * 
 * @author can
 *
 */
@Service
public class DeptBiztypeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeptBiztypeService.class);

	@Autowired
	private DeptBiztypeBiz deptBiztypeBiz;
	@Autowired
	private AdminFeign adminFeign;
	@Autowired
	private MergeCore mergeCore;
	@Autowired
	private DictFeign dictFeign;

	/**
	 * 根据条件进行分布查询
	 * 
	 * @param page        起始页
	 * @param limit       页容量
	 * @param deptBiztype 封装有查询条件的DeptBizType对象
	 * @return 与查询条件相符的查询结果
	 */
	public TableResultResponse<DeptBizTypeVo> getList(int page, int limit, DeptBiztype deptBiztype) {

		TableResultResponse<DeptBizTypeVo> tableResult = deptBiztypeBiz.getList(page, limit, deptBiztype);
		List<DeptBizTypeVo> listVo = tableResult.getData().getRows();

		queryAssist(listVo);

//		try {
//		mergeCore.mergeResult(DeptBiztype.class, list);
//	} catch (NoSuchMethodException e) {
//		e.printStackTrace();
//	} catch (InvocationTargetException e) {
//		e.printStackTrace();
//	} catch (IllegalAccessException e) {
//		e.printStackTrace();
//	} catch (ExecutionException e) {
//		e.printStackTrace();
//	}

		return tableResult;
	}

	private void queryAssist(List<DeptBizTypeVo> list) {
		Map<String, String> departs = new HashMap<>();
		Map<String, String> layerDepart = new HashMap<>();
		List<String> uniqueDeparts = list.stream().map((o) -> o.getDepartment()).distinct()
				.collect(Collectors.toList());

		// 部门存在多级情况，采用getLayerDepart方法进行聚和，弃用下面大段注释
		if (uniqueDeparts != null && !uniqueDeparts.isEmpty()) {
			layerDepart = adminFeign.getLayerDepart(String.join(",", uniqueDeparts));
		}

		if (!layerDepart.isEmpty()) {
			for (DeptBizTypeVo tmpVo : list) {
				String layerDeptInfo = layerDepart.get(tmpVo.getDepartment());
				if (StringUtils.isNotBlank(layerDeptInfo)) {
					JSONObject layerDeptJObject = JSONObject.parseObject(layerDeptInfo);
					tmpVo.setDepartment(layerDeptJObject.getString("name"));
				}
			}
		}

		// 业务条线数据聚和
		List<String> collect = list.stream().map(o -> o.getBizType()).distinct().collect(Collectors.toList());
		Map<String, String> byCodeIn = dictFeign.getByCodeIn(String.join(",", collect));
		if (byCodeIn != null && !byCodeIn.isEmpty()) {
			for (DeptBizTypeVo tmpDeptBiztype : list) {
				String[] split = tmpDeptBiztype.getBizType().split(",");

				for (String string : split) {
					if (StringUtils.isNotBlank(tmpDeptBiztype.getBizTypeName())) {
						tmpDeptBiztype.setBizTypeName(tmpDeptBiztype.getBizTypeName() + "," + byCodeIn.get(string));
					} else {
						tmpDeptBiztype.setBizTypeName(byCodeIn.get(string));
					}
				}
			}
		}
	}

	public TableResultResponse<DeptBizTypeVo> get(String id) {
		DeptBiztype deptBiztype = deptBiztypeBiz.selectById(Integer.valueOf(id));

		if (deptBiztype == null || deptBiztype.getIsDeleted().equals("1")) {
			return null;
		}

		DeptBizTypeVo deptBiztypeVo = BeanUtil.copyBean_New(deptBiztype, new DeptBizTypeVo());

		List<DeptBizTypeVo> voList = new ArrayList<>();
		voList.add(deptBiztypeVo);

		queryAssist(voList);

		TableResultResponse<DeptBizTypeVo> returnResult = new TableResultResponse<>(1, voList);

		return returnResult;
	}

	/**
	 * 新增部门业务条线
	 * 
	 * @param deptBiztype待添加的部门业务条线
	 * @return
	 */
	public Result<Void> createDeptBiztype(DeptBiztype deptBiztype) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// 验证部门是否存在
		if (StringUtils.isNotBlank(deptBiztype.getDepartment())) {
			Map<String, String> dept = adminFeign.getDepart(deptBiztype.getDepartment());
			if (dept == null || dept.isEmpty()) {
				result.setMessage("部门不存在");
				LOGGER.info("部门【" + deptBiztype.getDepartment() + "】不存在");
				return result;
			}
		}

		// 验证业务条线是否存在
		if (StringUtils.isNotBlank(deptBiztype.getBizType())) {
			Map<String, String> dictValues = dictFeign.getByCodeIn(deptBiztype.getBizType());
			for (String string : dictValues.keySet()) {
				if (dictValues == null || dictValues.isEmpty()) {
					result.setMessage("业务条线不存在");
					LOGGER.info("业务条线【" + string + "】不存在,不能进行新增操作");
					return result;
				}
			}
		}

		/*
		 * 验证部门唯一性<br/> 如现在已存在A部门——A1,A2,A3业务条线，此时该请求处理的是添加A部门——A4的业务条线，则提示用户该部门已存在<br/>
		 * 此时用户应进行的操作是在A部门下新增A4业务条线（对A部门进行更新操作）
		 */
		List<DeptBiztype> deptBiztypeInDB = deptBiztypeBiz.getByDept(deptBiztype.getDepartment());
		if (deptBiztypeInDB != null && deptBiztypeInDB.size() > 0) {
			result.setMessage("该部门已存在业务条线");
			LOGGER.info("部门【" + deptBiztype.getDepartment() + "】已存在业务条线");
			return result;
		}

		// 通过验证，进行业务条线添加操作
		/*
		 * 前台传入参数中包括【部门】【业务条线】两个内容 isDeleted isDisabled默认为0
		 */
		deptBiztypeBiz.insertSelective(deptBiztype);

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 修改部门业务条线
	 * 
	 * @param deptBiztype 待修改的部门业务条线
	 * @return
	 */
	public Result<Void> updateDeptBiztype(DeptBiztype deptBiztype) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// 验证部门是否存在
		if (StringUtils.isNotBlank(deptBiztype.getDepartment())) {
			Map<String, String> dept = adminFeign.getDepart(deptBiztype.getDepartment());
			if (dept == null || dept.isEmpty()) {
				result.setMessage("部门不存在");
				LOGGER.info("部门【" + deptBiztype.getDepartment() + "】不存在");
				return result;
			}
		} else {
			deptBiztype.setBizType(null);
		}

		// 验证业务条线是否存在
		if (StringUtils.isNotBlank(deptBiztype.getBizType())) {
			Map<String, String> dictValues = dictFeign.getByCodeIn(deptBiztype.getBizType());
			String[] split = deptBiztype.getBizType().split(",");
			if (dictValues == null || dictValues.isEmpty()) {
				for (String string : split) {
					result.setMessage("业务条线不存在");
					LOGGER.info("业务条线【" + string + "】不存在,不能进行更新操作");
					return result;
				}
			}
		} else {
			//防止前台传入了空字符串或空格
			deptBiztype.setBizType(null);
		}

		/*
		 * 验证部门唯一性<br/>
		 */
		List<DeptBiztype> deptBiztypeInDB = deptBiztypeBiz.getByDept(deptBiztype.getDepartment());
		if (deptBiztypeInDB != null && deptBiztypeInDB.size() > 0) {
			for (DeptBiztype deptBiztype2 : deptBiztypeInDB) {
				if (!deptBiztype2.getId().equals(deptBiztype.getId())) {
					/*
					 * 1 传入的部门ID在数据库记录中已存在 2
					 * 如果数据库中已存在的这个部门所对应的记录ID与传入的记录ID不一致，说明要把其它记录的部门更新为传入的部门ID所对应的部门，违背部门唯一性 3 不满足2
					 * ，说明要更新的即为传入部门所对应的那条记录，应予放行
					 */
					result.setMessage("该部门已存在");
					LOGGER.info("部门【" + deptBiztype.getDepartment() + "】已存在");
					return result;
				}
			}
		}

		deptBiztypeBiz.updateSelectiveById(deptBiztype);
		result.setIsSuccess(true);
		result.setMessage("成功");

		return result;
	}

	/**
	 * 删除单个部门业务条线
	 * 
	 * @param deptBiztype 待删除的部门业务条线
	 * @return
	 */
	public Result<Void> removeDeptBiztype(DeptBiztype deptBiztype) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		deptBiztypeBiz.updateSelectiveById(deptBiztype);
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
