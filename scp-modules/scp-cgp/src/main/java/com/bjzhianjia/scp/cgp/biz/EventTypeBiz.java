package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 事件类型
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Service
public class EventTypeBiz extends BusinessBiz<EventTypeMapper, EventType> {

	@Autowired
	private EventTypeMapper eventTypeMapper;

	/**
	 * 根据事件编号获取
	 * 
	 * @param typeCode 事件编号
	 * @return
	 */
	public EventType getByTypeCode(String typeCode) {

		Example example = new Example(EventType.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("typeCode", typeCode);

		List<EventType> list = eventTypeMapper.selectByExample(example);

		if (list == null || list.size() == 0) {
			return null;
		}

		EventType eventType = list.get(0);

		if (eventType.getIsDeleted().equals("1")) {
			return null;
		}

		return eventType;
	}

	/**
	 * 根据事件名称获取
	 * 
	 * @param typeName 事件名称
	 * @return
	 */
	public EventType getByTypeName(String typeName) {
		Example example = new Example(EventType.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("typeName", typeName);

		List<EventType> list = eventTypeMapper.selectByExample(example);

		if (list == null || list.size() == 0) {
			return null;
		}

		EventType eventType = list.get(0);

		if (eventType.getIsDeleted().equals("1")) {
			return null;
		}

		return eventType;
	}

	/**
	 * 根据事件条线获取
	 * 
	 * @param bizType 事件条线
	 * @return
	 */
	public List<EventType> getByBizType(String bizType) {
		Example example = new Example(EventType.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDeleted", "0");
		criteria.andEqualTo("bizType", bizType);

		return eventTypeMapper.selectByExample(example);

	}

	/**
	 * 根据查询条件搜索
	 * 
	 * @param eventType
	 * @return
	 */
	public TableResultResponse<EventType> getList(int page, int limit, EventType eventType) {
		Example example = new Example(EventType.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("isDeleted", "0");
		if (StringUtils.isNotBlank(eventType.getTypeName())) {
			// 事件类别名称模糊匹配查询
			criteria.andLike("typeName", "%" + eventType.getTypeName() + "%");
//	    	criteria.andEqualTo("typeName", eventType.getTypeName());
		}
		if (StringUtils.isNotBlank(eventType.getBizType())) {
			criteria.andEqualTo("bizType", eventType.getBizType());
		}
		if (StringUtils.isNotBlank(eventType.getIsEnable())) {
			criteria.andEqualTo("isEnable", eventType.getIsEnable());
		}

		// 按创建时间倒序
		example.setOrderByClause("id desc");

		Page<Object> result = PageHelper.startPage(page, limit);
		List<EventType> list = eventTypeMapper.selectByExample(example);
		return new TableResultResponse<EventType>(result.getTotal(), list);
	}

	/**
	 * 按Id获取事件类型对象
	 * 
	 * @author 尚
	 * @param idList 事件类型Id集合
	 * @return
	 */
	public List<EventType> getByIds(List<String> idList) {
		Example example = new Example(EventType.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", idList);
		List<EventType> eventTypeList = this.mapper.selectByExample(example);
		return eventTypeList;
	}

	/**
	 * 根据编号获取终端
	 * 
	 * @param id 编号
	 * @return
	 */
	public EventType getById(Integer id) {

		EventType eventType = eventTypeMapper.selectByPrimaryKey(id);

		if (eventType != null && eventType.getIsDeleted().equals("1")) {
			return null;
		}

		return eventType;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		eventTypeMapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getName(), new Date());
	}

	/**
	 * 根据多个id获取
	 * 
	 * @param ids
	 */
	public Map<Integer, String> getByIds(String ids) {
		ids = "'" + ids.replaceAll(",", "','") + "'";
		List<EventType> list = eventTypeMapper.selectByIds(ids);

		return list.stream().collect(Collectors.toMap(EventType::getId, EventType::getTypeName));
		// 待聚和对象中enevtType属性封装的是Id，如果 返回typeCode:typeName组合，无法确定怎样去聚和
//		return list.stream().collect(Collectors.toMap(EventType::getTypeCode, EventType::getTypeName));
	}
}