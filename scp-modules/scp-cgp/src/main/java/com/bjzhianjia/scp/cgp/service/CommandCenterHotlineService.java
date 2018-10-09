package com.bjzhianjia.scp.cgp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CommandCenterHotlineBiz;
import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * @author 尚
 */
@Service
public class CommandCenterHotlineService {

    @Autowired
    private CommandCenterHotlineBiz commandCenterHotlineBiz;

    @Autowired
    private MergeCore mergeCore;

    @Autowired
    private EventTypeMapper eventTypeMapper;

    @Autowired
    private PropertiesProxy propertiesProxy;

    /**
     * 分页获取列表
     * 
     * @param commandCenterHotline
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> getList(CommandCenterHotline commandCenterHotline, int page, int limit) {
        TableResultResponse<CommandCenterHotline> restResult =
            commandCenterHotlineBiz.getList(commandCenterHotline, page, limit);

        List<CommandCenterHotline> hotLineList = restResult.getData().getRows();
        if (BeanUtil.isEmpty(hotLineList)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        try {
            mergeCore.mergeResult(CommandCenterHotline.class, hotLineList);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<JSONObject> jObjResult = queryAssist(hotLineList);
        return new TableResultResponse<>(restResult.getData().getTotal(), jObjResult);
    }

    private List<JSONObject> queryAssist(List<CommandCenterHotline> hotLineList) {
        List<JSONObject> jObjResult = new ArrayList<>();

        // 整合事件类别
        List<String> eventTypeIdList =
            hotLineList.stream().map(o -> String.valueOf(o.getEventType())).distinct().collect(Collectors.toList());
        Map<Integer, String> eventType_ID_NAME_Map = new HashMap<>();
        if (BeanUtil.isNotEmpty(eventTypeIdList)) {
            List<EventType> eventtypeList = eventTypeMapper.selectByIds(String.join(",", eventTypeIdList));
            if (BeanUtil.isNotEmpty(eventtypeList)) {
                for (EventType eventType : eventtypeList) {
                    eventType_ID_NAME_Map.put(eventType.getId(), eventType.getTypeName());
                }
            }
        }

        for (CommandCenterHotline commandCenterHotline : hotLineList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject =
                    propertiesProxy.swapProperties(commandCenterHotline, "id", "hotlnCode", "hotlnTitle","appealType",
                        "appealDatetime", "appealPerson", "exeStatus", "appealTel", "crtUserId", "bizType");
                jsonObject.put("eventTypeName", eventType_ID_NAME_Map.get(commandCenterHotline.getEventType()));
            } catch (Throwable e) {
                e.printStackTrace();
            }

            jObjResult.add(jsonObject);
        }
        return jObjResult;
    }

    /**
     * 按ID进行精确查询
     * 
     * @param id
     * @return
     */
    public JSONObject selectById(Integer id) {
        CommandCenterHotline hotline = commandCenterHotlineBiz.selectById(id);

        List<CommandCenterHotline> hotlines = new ArrayList<>();
        hotlines.add(hotline);
        try {
            mergeCore.mergeResult(CommandCenterHotline.class, hotlines);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<JSONObject> queryAssist = queryAssist(hotlines);

        return queryAssist.get(0);
    }
}
