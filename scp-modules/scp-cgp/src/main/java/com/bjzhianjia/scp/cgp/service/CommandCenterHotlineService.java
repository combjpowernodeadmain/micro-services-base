package com.bjzhianjia.scp.cgp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.bjzhianjia.scp.cgp.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CommandCenterHotlineBiz;
import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
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
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private DictFeign dictFeign;

    /**
     * 分页获取列表
     * 
     * @param commandCenterHotline
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     */
    public TableResultResponse<JSONObject> getList(CommandCenterHotline commandCenterHotline,
                                                   int page, int limit, String startTime, String endTime) {
        TableResultResponse<CommandCenterHotline> restResult =
            commandCenterHotlineBiz.getList(commandCenterHotline, page, limit, startTime, endTime);

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
        
        Set<String> dictKeySet=new HashSet<>(); 
        for (CommandCenterHotline commandCenterHotline : hotLineList) {
            if(StringUtils.isNotBlank(commandCenterHotline.getAppealType())) {
                dictKeySet.add(commandCenterHotline.getAppealType());
            }
            if(StringUtils.isNotBlank(commandCenterHotline.getBizType())) {
                dictKeySet.add(commandCenterHotline.getBizType());
            }
        }
        
        Map<String, String> dictValueMap=new HashMap<>();
        if(BeanUtil.isNotEmpty(dictKeySet)) {
            dictValueMap=dictFeign.getByCodeIn(String.join(",", dictKeySet));
        }

        for (CommandCenterHotline commandCenterHotline : hotLineList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject =
                    propertiesProxy.swapProperties(commandCenterHotline, "id", "hotlnCode", "hotlnTitle", "appealType",
                        "appealDatetime", "appealPerson", "exeStatus", "appealTel", "crtUserId", "bizType",
                        "crtUserName","appealDesc","eventType");
                jsonObject.put("eventTypeName", eventType_ID_NAME_Map.get(commandCenterHotline.getEventType()));
                jsonObject.put("bizTypeName", dictValueMap.get(commandCenterHotline.getBizType()));
                jsonObject.put("appealTypeName", dictValueMap.get(commandCenterHotline.getAppealType()));
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
    
    /**
     * 按ID进行精确查询
     * 
     * @param id
     * @return
     */
    public ObjectRestResponse<JSONObject> getToDo(Integer id) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();
        CommandCenterHotline hotline = commandCenterHotlineBiz.selectById(id);

        if (!environment.getProperty("root_biz_12345state_todo").equals(hotline.getExeStatus())) {
            restResult.setStatus(400);
            restResult.setMessage("当前记录不能修改，只有【未发起】的热线记录可修改！");
            return restResult;
        }

        List<CommandCenterHotline> hotlines = new ArrayList<>();
        hotlines.add(hotline);
//        try {
//            mergeCore.mergeResult(CommandCenterHotline.class, hotlines);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        List<JSONObject> queryAssist = queryAssist(hotlines);

        restResult.setStatus(200);
        restResult.setMessage("成功");
        restResult.setData(queryAssist.get(0));
        return restResult;
    }
}
