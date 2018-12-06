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