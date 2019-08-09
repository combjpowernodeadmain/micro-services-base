package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private EventTypeMapper eventTypeMapper;

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

		// 判断是否存在排序字段
		if (StringUtils.isNotBlank(vo.getSortColumn())) {
			this.setSortColumn(example,vo.getSortColumn());
		} else {
			// 默认id降序
			example.setOrderByClause(" id desc");
		}

		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<SpecialEvent> result = this.mapper.selectByExample(example);
		return new TableResultResponse<>(pageInfo.getTotal(), result);
	}
	/**
	 * 设置排序字段
	 * <p>
	 * 此方法直接接受前端的参数进行sql拼接，修改此方法需注意sql注入
	 * </p>
	 *
	 * @param example   查询对象
	 * @param sortColumn 查询条件
	 */
	private void setSortColumn(Example example,String sortColumn) {
		// 判断是否有排序条件
		if (BeanUtils.isNotEmpty(sortColumn)) {
			String[] columns = sortColumn.split(":");
			// 排序字段的解析长度
			int len = 2;
			if (len == columns.length) {
				String orderColumn = null;
				// 获取sql拼接字段
				switch (columns[0]) {
					// ID
					case "id":
						orderColumn = "id ";
						break;
					// 专项开始时间
					case "speStartDate":
						orderColumn = "spe_start_date ";
						break;
						//专项编号
					case "speCode":
						orderColumn = "spe_code ";
						break;
					default:
						break;
				}
				// 获取排序规则
				String sort = "desc";
				if (!sort.equals(columns[1])) {
					sort = "asc";
				}
				// 设置排序字段和规则
				example.setOrderByClause(orderColumn + sort);
			}
		}
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

	/**
	 * 查询专项id下，与bizList对应的事件类别
	 * @param id
	 * @param bizList
	 * @return
	 */
    public TableResultResponse<EventType> eventTypeInSApeEvent(Integer id, String bizList) {
        TableResultResponse<EventType> tableresult = new TableResultResponse<>();

        SpecialEvent specialEvent = this.selectById(id);
        if (BeanUtil.isEmpty(specialEvent)) {
            tableresult.setMessage("未找到相关专项任务");
            tableresult.setStatus(400);
            return tableresult;
        }

        if (StringUtils.isNotBlank(specialEvent.getBizList())
            && StringUtils.isNotBlank(specialEvent.getEventTypeList())) {
            String[] bizListSplits = specialEvent.getBizList().split(",");
            String[] eventTypeSplits = specialEvent.getEventTypeList().split(";");

            // 待收集事件类别ID集合
			Set<String> eventTypeIdSet=new HashSet<>();

			for (int i = 0; i < bizListSplits.length; i++) {
				if (bizList.equals(bizListSplits[i])) {

                    // 从bizListSplits中找到与传入业务条线对应的记录
                    String eventTypeSplit = eventTypeSplits[i];
                    if (StringUtils.isNotBlank(eventTypeSplit) && !"-".equals(eventTypeSplit)) {
						eventTypeIdSet.addAll(Arrays.asList(eventTypeSplit.split(",")));
                    }
                }
            }

            // 查询事件类别
            if (BeanUtil.isNotEmpty(eventTypeIdSet)) {
                List<EventType> eventTypes =
                    eventTypeMapper.selectByIds(String.join(",", eventTypeIdSet));
                if (BeanUtil.isNotEmpty(eventTypes)) {
                    List<EventType> forRegurnList = new ArrayList<>();
                    for (EventType tmpEventType : eventTypes) {
                        if ("0".equals(tmpEventType.getIsDeleted())) {
                            // 返回特定字段
                            EventType forReturn = new EventType();
                            forReturn.setId(tmpEventType.getId());
                            forReturn.setTypeName(tmpEventType.getTypeName());
                            forRegurnList.add(forReturn);
                        }
                    }
                    tableresult.getData().setTotal(forRegurnList.size());
                    tableresult.getData().setRows(forRegurnList);
                    return tableresult;
                }
            }
        }

        tableresult.setMessage("未找到相关专项任务");
        tableresult.setStatus(400);
        return tableresult;
	}
}