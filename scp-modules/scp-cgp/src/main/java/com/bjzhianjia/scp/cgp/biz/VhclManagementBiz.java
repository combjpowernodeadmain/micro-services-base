package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.VhclManagement;
import com.bjzhianjia.scp.cgp.mapper.VhclManagementMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 车辆管理
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Service
public class VhclManagementBiz extends BusinessBiz<VhclManagementMapper, VhclManagement> {

	@Autowired
	private VhclManagementMapper vhclManagermentMapper;
	@Autowired
	private DeptUtilBiz deptUtilBiz;

	/**
	 * 根据车辆号获取终端
	 * 
	 * @param terminalCode 终端号
	 * @return
	 */
	public VhclManagement getByVehicleNum(String vehicleNum) {

		Example example = new Example(VhclManagement.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("vehicleNum", vehicleNum);

		List<VhclManagement> list = vhclManagermentMapper.selectByExample(example);

		if (list == null || list.size() == 0) {
			return null;
		}

		VhclManagement vhcl = list.get(0);

		if (vhcl.getIsDeleted().equals("1")) {
			return null;
		}

		return vhcl;
	}

	/**
	 * 根据查询条件搜索
	 * 
	 * @param terminal
	 * @return
	 */
	public TableResultResponse<VhclManagement> getList(int page, int limit, VhclManagement vhcl) {
		Example example = new Example(VhclManagement.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(vhcl.getVehicleNum())) {
            criteria.andLike("vehicleNum", "%" + vhcl.getVehicleNum() + "%");
        }
		if (StringUtils.isNotBlank(vhcl.getVehicleType())) {
			criteria.andEqualTo("vehicleType", vhcl.getVehicleType());
		}
		// 按部门查询的，如果输入的部门条件为父部门，则将其子部门相关记录一并查出
		if (StringUtils.isNotBlank(vhcl.getDepartment())) {
			List<String> deptIds = deptUtilBiz.getDeptIds(vhcl.getDepartment());
			criteria.andIn("department", deptIds);
//	    	criteria.andEqualTo("department", vhcl.getDepartment());
		}

		example.setOrderByClause("id desc");

		Page<Object> result = PageHelper.startPage(page, limit);
		List<VhclManagement> list = vhclManagermentMapper.selectByExample(example);
		return new TableResultResponse<VhclManagement>(result.getTotal(), list);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		vhclManagermentMapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getName(),
				new Date());
	}
	
	/**
	 * 查询未删除车辆数量
	 * @return
	 */
	public int countOfVhcl() {
	    Example example=new Example(VhclManagement.class);
	    Criteria criteria = example.createCriteria();
	    criteria.andEqualTo("isDeleted", "0");
	    int count = this.selectCountByExample(example);
	    return count;
	}
}