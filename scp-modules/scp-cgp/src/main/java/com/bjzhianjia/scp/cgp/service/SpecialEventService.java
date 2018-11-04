package com.bjzhianjia.scp.cgp.service;

import com.bjzhianjia.scp.cgp.biz.SpecialEventBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.SpecialEvent;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.SpecialEventVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 专项管理
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class SpecialEventService {
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private EventTypeMapper eventTypeMapper;
	@Autowired
	private RegulaObjectTypeMapper regulaObjectTypeMapper;
	@Autowired
	private SpecialEventBiz specialEventBiz;
	@Autowired
	private MergeCore mergeCore;
	@Autowired
	private Environment environment;

	/**
	 * 添加专项管理记录
	 * 
	 * @author 尚
	 * @param specialEventVo
	 * @return
	 */
	public ObjectRestResponse<SpecialEventVo> createSpecialEvent(SpecialEventVo specialEventVo) {
		ObjectRestResponse<SpecialEventVo> restResult = new ObjectRestResponse<>();

		Result<Void> check = check(specialEventVo, false);
		if (!check.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(check.getMessage());
			return restResult;
		}

		specialEventBiz.insertSelective(specialEventVo);
		restResult.setData(specialEventVo);
		return restResult;
	}

	/**
	 * 验证待添加或更新的记录是否合法
	 * 
	 * @author 尚
	 * @param specialEventVo
	 * @param isUpdate
	 * @return
	 */
	private Result<Void> check(SpecialEventVo specialEventVo, boolean isUpdate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// ii. 验证涵盖业务条线是否已被删除
		String bizListStr = specialEventVo.getBizList();
		if (StringUtils.isNotBlank(bizListStr) && !bizListStr.equals("-")) {
			String[] bizTypeIds = bizListStr.split(",");
			List<String> bizTypeIdList = new ArrayList<>();
			for (String string : bizTypeIds) {
				if (!string.equals("-")) {
					bizTypeIdList.add(string);
				}
			}

			Map<String, String> bizTypeMapInDB = dictFeign.getByCodeIn(String.join(",", bizTypeIdList));
			for (String bizTypeId : bizTypeIdList) {
				if (!bizTypeMapInDB.containsKey(bizTypeId)) {
					result.setMessage("业务条线不存在或已删除");
					return result;
				}
			}

		}

		// iii. 验证涵盖事件类别是否已被删除
		String eventTypeListStr = specialEventVo.getEventTypeList();
		if (StringUtils.isNotBlank(eventTypeListStr) && !eventTypeListStr.equals("-")) {
			List<String> eventTypeIdList = new ArrayList<>();
			String[] eventTypeIds = eventTypeListStr.split(";|\\,");
			for (String string : eventTypeIds) {
				if (!string.equals("-")) {
					eventTypeIdList.add(string);
				}
			}

			List<EventType> eventTypeListInDB = eventTypeMapper.selectByIds(String.join(",", eventTypeIdList));
			Map<Integer, String> eventTypeMapInDB = eventTypeListInDB.stream()
					.collect(Collectors.toMap(EventType::getId, EventType::getIsDeleted));
			for (String eventTypeId : eventTypeIdList) {
				if (!eventTypeMapInDB.containsKey(Integer.valueOf(eventTypeId))
						|| eventTypeMapInDB.get(Integer.valueOf(eventTypeId)).equals("1")) {
					result.setMessage("事件类型不存在或已删除");
					return result;
				}
			}

		}

		// iv. 验证涉及监管对象（类型）是否已被删除
		String regObjListStr = specialEventVo.getRegObjList();
		if (StringUtils.isNotBlank(regObjListStr)) {
			List<RegulaObjectType> regObjTypeListInDB = regulaObjectTypeMapper.selectByIds(regObjListStr);
			for (RegulaObjectType regulaObjectType : regObjTypeListInDB) {
				if (regulaObjectType.getIsDeleted().equals("1")) {
					result.setMessage("监管对象【" + regulaObjectType.getObjectTypeName() + "】不存在或已删除");
					return result;
				}
			}
		}

		// v. 验证各数据字典所选值是否已被删除
		// 专项状态原为数据字典
		
		
		if (StringUtils.isNotBlank(specialEventVo.getSpeCode())) {
			Map<String, Object> conditions = new HashMap<>();
			conditions.put("speCode", specialEventVo.getSpeCode());
			List<SpecialEvent> byMap = specialEventBiz.getByMap(conditions);
			if (isUpdate) {
				for (SpecialEvent specialEvent : byMap) {
					if (!specialEvent.getId().equals(specialEventVo.getId())) {
						// 说明数据库中已存在一条专项编号相等，但ID不相等的记录==》编号重复
						result.setMessage("该专项编号已存在");
						return result;
					}
				}
			} else {
				if (byMap != null && !byMap.isEmpty()) {
					result.setMessage("该专项编号已存在");
					return result;
				}
			}
		}

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 更新单个对象
	 * 
	 * @author 尚
	 * @param specialEventVo
	 * @return
	 */
	public ObjectRestResponse<SpecialEventVo> update(SpecialEventVo specialEventVo) {
		ObjectRestResponse<SpecialEventVo> restResult = new ObjectRestResponse<>();

		Result<Void> check = check(specialEventVo, true);

		if (!check.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(check.getMessage());
			return restResult;
		}

		specialEventBiz.updateSelectiveById(specialEventVo);
		restResult.data(specialEventVo);
		return restResult;
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
	public TableResultResponse<SpecialEventVo> getList(SpecialEventVo vo, int page, int limit) {
		TableResultResponse<SpecialEvent> restResult = specialEventBiz.getList(vo, page, limit);
		List<SpecialEvent> rows = restResult.getData().getRows();

		if(BeanUtil.isEmpty(rows)){
			TableResultResponse<SpecialEventVo> voTableResultResponse=new TableResultResponse<>();
			voTableResultResponse.setStatus(400);
			voTableResultResponse.setMessage("没有相关数据");
			return voTableResultResponse;
		}

		try {
			mergeCore.mergeResult(SpecialEvent.class, rows);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		List<SpecialEventVo> voList = _queryAssist(rows);

		return new TableResultResponse<SpecialEventVo>(restResult.getData().getTotal(), voList);
	}

	private List<SpecialEventVo> _queryAssist(List<SpecialEvent> rows) {
		List<SpecialEventVo> voList = BeanUtil.copyBeanList_New(rows, SpecialEventVo.class);

        // 收集相关事件类别ID,事件类别保存形式如31,32;30;-;29
        Set<String> eventTypeSet = new HashSet<>();
        Map<Integer, Set<String>> special_ID_EVENTTYPEID_Map = new HashMap<>();//封装专项记录里ID与对应事件类别ID集合
        for (SpecialEventVo vo : voList) {
            if (StringUtils.isNotBlank(vo.getEventTypeList())) {
                Set<String> eventTypeIdSetIn = new HashSet<>();// 用于存在当前对象的事件类别ID
                for (String eventTypeIdT1 : vo.getEventTypeList().split(";")) {
                    if (StringUtils.isNotBlank(eventTypeIdT1) && !"-".equals(eventTypeIdT1)) {
                        for (String eventType2 : eventTypeIdT1.split(",")) {
                            eventTypeSet.add(eventType2);
                            eventTypeIdSetIn.add(eventType2);
                        }
                    }
                }

                special_ID_EVENTTYPEID_Map.put(vo.getId(), eventTypeIdSetIn);
            }
        }
        // 查询相关事件类别
        List<EventType> eventTypes = null;
        if (BeanUtil.isNotEmpty(eventTypeSet)) {
            // 在进行"in"条件查询时，如果条件集合为空，会报sql错误，需要进行非空判断
            eventTypes = eventTypeMapper.selectByIds(String.join(",", eventTypeSet));
        }
        Map<Integer, String> eventType_ID_NAME_Map = new HashMap<>();//封装事件类别ID与名称
        if (BeanUtil.isNotEmpty(eventTypes)) {
            for (EventType eventType : eventTypes) {
                eventType_ID_NAME_Map.put(eventType.getId(),
                    eventType.getTypeName().endsWith(",")
                        ? eventType.getTypeName().substring(0, eventType.getTypeName().length() - 1)
                        : eventType.getTypeName());
            }
        }

		// 字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
		Map<String, String> bizTypeMap = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
		for (SpecialEventVo voTmp : voList) {
			// 聚和业务条线
			if (StringUtils.isNotBlank(voTmp.getBizList())) {
				String[] split = voTmp.getBizList().split(",");
				List<String> bizTypeNameList = new ArrayList<>();
				for (String string : split) {
					bizTypeNameList.add(bizTypeMap.get(string));
				}
				voTmp.setBizListName(String.join(",", bizTypeNameList));
			}

			// 解析监管对象
			String regObjListStr = voTmp.getRegObjList();
			if(StringUtils.isNotBlank(regObjListStr)) {
				List<RegulaObjectType> regObjList = regulaObjectTypeMapper.selectByIds(regObjListStr);
				List<String> regObjTypeNameList = regObjList.stream().map(o -> o.getObjectTypeName()).distinct()
						.collect(Collectors.toList());
				voTmp.setRegObjTypeName(String.join(",", regObjTypeNameList));
			}

            // 聚积事件类别
            List<String> eventTypeNameList = new ArrayList<>();
            Set<String> eventTypeSetIn = special_ID_EVENTTYPEID_Map.get(voTmp.getId());
            for (String eventTypeId : eventTypeSetIn) {
                eventTypeNameList.add(eventType_ID_NAME_Map.get(Integer.valueOf(eventTypeId)));
            }
            voTmp.setEventTypeName(String.join(",", eventTypeNameList));
		}
		return voList;
	}

	/**
	 * 获取单个对象
	 * 
	 * @author 尚
	 * @param id
	 * @return
	 */
	public SpecialEventVo getOne(Integer id) {
		SpecialEvent one = specialEventBiz.selectById(id);
		List<SpecialEvent> rows = new ArrayList<>();
		
		try {
			mergeCore.mergeResult(SpecialEvent.class, rows);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		rows.add(one);
		List<SpecialEventVo> result = _queryAssist(rows);
		return result.get(0);
	}

	/**
	 * 获取单个对象
	 *
	 * @author 尚
	 * @param id
	 * @return
	 */
	public Result<SpecialEventVo> getOneToDo(Integer id) {
        Result<SpecialEventVo> result = new Result<>();
        result.setIsSuccess(false);

        SpecialEvent one = specialEventBiz.selectById(id);

        // root_biz_specialType_todo
        if (!environment.getProperty("specialTypeTodo").equals(one.getSpeStatus())) {
            // 该专项已不是【未启用】
            result.setMessage("当前记录不能修改，只有【未启用】的专项记录可修改！");
            return result;
        }

        List<SpecialEvent> rows = new ArrayList<>();

        try {
            mergeCore.mergeResult(SpecialEvent.class, rows);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rows.add(one);
        List<SpecialEventVo> resultVo = _queryAssist(rows);
        result.setIsSuccess(true);
        result.setData(resultVo.get(0));
        return result;
	}
}
