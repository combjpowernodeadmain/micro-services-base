package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.SpecialEvent;
import com.bjzhianjia.scp.cgp.mapper.SpecialEventMapper;
import com.bjzhianjia.scp.cgp.vo.SpecialEventVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 专项管理
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-07 10:24:58
 */
@Service
public class SpecialEventBiz extends BusinessBiz<SpecialEventMapper, SpecialEvent> {

	/**
	 * 按条件查询未被删除的记录
	 * 
	 * @author 尚
	 * @param conditions
	 * @return
	 */
	public List<SpecialEvent> getByMap(Map<String, Object> conditions) {
		Example example = new Example(SpecialEvent.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");

		Set<String> keySet = conditions.keySet();
		for (String string : keySet) {
			criteria.andEqualTo(string, conditions.get(string));
		}

		List<SpecialEvent> result = this.mapper.selectByExample(example);
		return result;
	}

	/**
	 * 按分页获取对象
	 * 
	 * @author 尚
	 * @param vo
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<SpecialEvent> getList(SpecialEventVo vo, int page, int limit) {
		Example example = new Example(SpecialEvent.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");
		if (StringUtils.isNotBlank(vo.getSpeCode())) {
			criteria.andLike("speCode", "%" + vo.getSpeCode() + "%");
		}
		if (StringUtils.isNotBlank(vo.getSpeName())) {
			criteria.andLike("speName", "%" + vo.getSpeName() + "%");
		}
		if (StringUtils.isNotBlank(vo.getPublisher())) {
			criteria.andLike("publisher", "%" + vo.getPublisher() + "%");
		}
		if (StringUtils.isNotBlank(vo.getSpeStatus())) {
			criteria.andEqualTo("speStatus", vo.getSpeStatus());
		}
		if (StringUtils.isNotBlank(vo.getBizList())) {
			criteria.andLike("bizList", "%"+vo.getBizList()+"%");
		}
		example.setOrderByClause("id desc");
		Page<Object> pageInfo = PageHelper.startPage(page, limit);

		List<SpecialEvent> result = this.mapper.selectByExample(example);

		return new TableResultResponse<SpecialEvent>(pageInfo.getTotal(), result);
	}

	/**
	 * 获取单个对象
	 * 
	 * @author 尚
	 * @param id
	 * @return
	 */
	public ObjectRestResponse<SpecialEvent> get(Integer id) {
		SpecialEvent result = this.mapper.selectByPrimaryKey(id);
		if (result.getIsDeleted().equals("1")) {
			return null;
		}
		return new ObjectRestResponse<SpecialEvent>().data(result);
	}
	
	/**
	 * 批量删除对象
	 * @author 尚
	 * @param ids
	 */
	public void remove(Integer[] ids) {
		this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getName(),
				new Date());
	}
}