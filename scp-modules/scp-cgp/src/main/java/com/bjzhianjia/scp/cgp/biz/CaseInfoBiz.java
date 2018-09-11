package com.bjzhianjia.scp.cgp.biz;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * CaseInfoBiz 预立案信息.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年8月8日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author bo
 *
 */
@Service
public class CaseInfoBiz extends BusinessBiz<CaseInfoMapper, CaseInfo> {

    @Autowired
    private MergeCore mergeCore;

    @Autowired
    private DictFeign dictFeign;
    
    /**
     * 查询未删除的总数
     * @return
     */
    public Integer getCount() {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setIsDeleted("0");
        return this.mapper.selectCount(caseInfo);
    }
    /**
     * 查询ID最大的那条记录
     * 
     * @author 尚
     * @return
     */
    public CaseInfo getMaxOne() {
        Example example = new Example(CaseInfo.class);
        example.setOrderByClause("id desc");
        PageHelper.startPage(1, 1);
        List<CaseInfo> caseInfoList = this.mapper.selectByExample(example);
        if (caseInfoList == null || caseInfoList.isEmpty()) {
            return null;
        }
        return caseInfoList.get(0);
    }

    /**
     * 按条件查询
     * 
     * @author 尚
     * @param conditions
     * @return
     */
    public List<CaseInfo> getByMap(Map<String, Object> conditions) {
        Example example = new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();

        Set<String> keySet = conditions.keySet();
        for (String key : keySet) {
            criteria.andEqualTo(key, conditions.get(key));
        }

        List<CaseInfo> list = this.selectByExample(example);
        return list;
    }

    /**
     * 按分布获取对象
     * 
     * @author 尚
     * @param caseInfo
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo, int page, int limit, boolean isNoFinish) {
        Example example = new Example(CaseInfo.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        if (isNoFinish) {
            criteria.andEqualTo("isFinished", "0");
            criteria.andNotEqualTo("id", caseInfo.getId());
        }

        if (caseInfo.getGrid() != null) {
            criteria.andEqualTo("grid", caseInfo.getGrid());
        }
        if (StringUtils.isNotBlank(caseInfo.getRegulaObjList())) {
            criteria.andIn("regulaObjList", Arrays.asList(caseInfo.getRegulaObjList().split(",")));
        }
        example.setOrderByClause("id desc");

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseInfo> list = this.mapper.selectByExample(example);

        try {
            mergeCore.mergeResult(CaseInfo.class, list);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new TableResultResponse<CaseInfo>(pageInfo.getTotal(), list);
    }

    /**
     * 按分布获取对象
     * 
     * @author 尚
     * @param caseInfo
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo, Set<Integer> ids, JSONObject queryData) {
        // 查询参数
        int page = StringUtils.isBlank(queryData.getString("page")) ? 1 : Integer.valueOf(queryData.getString("page"));
        int limit =
            StringUtils.isBlank(queryData.getString("limit")) ? 10 : Integer.valueOf(queryData.getString("limit"));
        String startQueryTime = queryData.getString("startQueryTime");
        String endQueryTime = queryData.getString("endQueryTime");

        String isSupervise = queryData.getString("isSupervise");
        String isUrge = queryData.getString("isUrge");
        String isOverTime = queryData.getString("isOverTime");
        String isFinished = queryData.getString("procCtaskname");// 1:已结案2:已终止
        String sourceType = queryData.getString("sourceType");// 来源类型
        String sourceCode = queryData.getString("sourceCode");// 来源id

        Example example = new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(caseInfo.getCaseTitle())) {
            if (caseInfo.getCaseTitle().length() > 127) {
                throw new BizException("事件名称应该小于127个字符");
            }
            criteria.andLike("caseTitle", "%" + caseInfo.getCaseTitle() + "%");
        }
        if (StringUtils.isNotBlank(caseInfo.getBizList())) {
            criteria.andLike("bizList", "%" + caseInfo.getBizList() + "%");
        }
        if (StringUtils.isNotBlank(caseInfo.getEventTypeList())) {
            criteria.andLike("eventTypeList", "%" + caseInfo.getEventTypeList() + "%");
        }
        if (StringUtils.isNotBlank(caseInfo.getSourceType())) {
            criteria.andEqualTo("sourceType", caseInfo.getSourceType());
        }
        if (!(StringUtils.isBlank(startQueryTime) || StringUtils.isBlank(endQueryTime))) {
            Date start = DateUtil.dateFromStrToDate(startQueryTime, "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.addDays(DateUtil.dateFromStrToDate(endQueryTime, "yyyy-MM-dd HH:mm:ss"), 1);
            criteria.andBetween("occurTime", start, end);
        }
        if (StringUtils.isNotBlank(caseInfo.getCaseLevel())) {
            criteria.andEqualTo("caseLevel", caseInfo.getCaseLevel());
        }
        if (ids != null && !ids.isEmpty()) {
            criteria.andIn("id", ids);
        }
        // 是否添加督办 (1:是|0:否)
        if (StringUtils.isNotBlank(isSupervise) && "1".equals(isSupervise)) {
            criteria.andEqualTo("isSupervise", caseInfo.getIsSupervise());
        }
        // 是否添加崔办(1:是|0:否)
        if (StringUtils.isNotBlank(isUrge) && "1".equals(isUrge)) {
            criteria.andEqualTo("isUrge", caseInfo.getIsUrge());
        }
        // 超时时间 (1:是|0:否)
        if (StringUtils.isNotBlank(isOverTime) && "1".equals(isOverTime)) {
            // 任务没有结束，当前日期和期限日期进行判断，任务结束，则判断完成日期和期限日期
            String date = DateUtil.dateFromDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
            criteria.andCondition("(dead_line > '" + date + "' or dead_line > finish_time)");
        }
        // 处理状态：已结案(0:未完成|1:已结案2:已终止)
        if (StringUtils.isNotBlank(isFinished) && !CaseInfo.FINISHED_STATE_TODO.equals(isFinished)) {
            // 只查询1:已结案2:已终止
            if (CaseInfo.FINISHED_STATE_FINISH.equals(queryData.getString("procCtaskname"))
                && CaseInfo.FINISHED_STATE_STOP.equals(queryData.getString("procCtaskname"))) {
                criteria.andEqualTo("isFinished", isFinished);
            }
        }
        if (StringUtils.isNotBlank(sourceType) && StringUtils.isNotBlank(sourceCode)
            && Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK.equals(sourceType)) {
            criteria.andEqualTo("sourceType", sourceType);
            criteria.andEqualTo("sourceCode", sourceCode);
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseInfo> list = this.mapper.selectByExample(example);

        return new TableResultResponse<CaseInfo>(pageInfo.getTotal(), list);
    }

    /**
     * 按ID集合查询对象集合
     * 
     * @author 尚
     * @param idList
     * @return
     */
    public List<CaseInfo> getListByIds(List<String> idList) {
        return this.mapper.selectByIds(String.join(",", idList));
    }

    /**
     * 事件处理统计
     * 
     * @return
     */
    public JSONObject getStatisCaseState(CaseInfo caseInfo, String startTime, String endTime) {
        JSONObject result = new JSONObject();
        //已终止、已完成、处理中、总数
        String[] stateKey = {"stop","finish","todo","total"};
        
        //超时统计
        Integer overtime = this.mapper.selectOvertime(caseInfo, startTime, endTime);
        //处理状态统计
        List<Map<String, Integer>> finishedState = this.mapper.selectState(caseInfo, startTime, endTime);
        String stateName = "";
        for(Map<String, Integer> state : finishedState) {
            switch (String.valueOf(state.get("state"))) {
                case CaseInfo.FINISHED_STATE_STOP:
                    stateName = stateKey[0];
                    break;
                case CaseInfo.FINISHED_STATE_FINISH:
                    stateName = stateKey[1];
                    break;
                case CaseInfo.FINISHED_STATE_TODO:
                    stateName = stateKey[2];
                    break;
              
            }
            result.put(stateName, state.get("count"));
        }
        
        //封装处理中、总数
        if(overtime != null && BeanUtil.isNotEmpty(finishedState)) {
            result.put("overtime", overtime);
            //已终止的
            Integer stopCount = result.getInteger(stateKey[0]);
            stopCount = stopCount == null?0:stopCount;
            //已完成
            Integer finishCount = result.getInteger(stateKey[1]);
            finishCount = finishCount == null?0:stopCount;
            //已完成 = 已完成 + 已终止
            result.put(stateKey[1],stopCount+finishCount);
            
            
            //事件总数
            result.put(stateKey[3], this.getCount());
            //删除多余字段
            result.remove(stateKey[0]);
        }
        return result;
    }

    /**
     * 事件来源分布统计
     * 
     */
    public JSONArray getStatisEventSource(CaseInfo caseInfo, String startTime, String endTime) {
        JSONArray result = new JSONArray();

        List<Map<String, Object>> eventSourceList = this.mapper.getStatisEventSource(caseInfo, startTime, endTime);
        Map<String, String> eventtypeMap = dictFeign.getByCode(Constances.ROOT_BIZ_EVENTTYPE);
        if (BeanUtil.isNotEmpty(eventtypeMap)) {
            //事件来源数量
            Integer count = 0;
            //事件来源类型集处理
            Map<String ,Integer> eventSourceMap = new HashMap<>();
            for (Map<String, Object> eventSource : eventSourceList) {
                String key = String.valueOf(eventSource.get("sourceType"));
                count = Integer.valueOf(String.valueOf(eventSource.get("count")));
                eventSourceMap.put(key, count);
            }
        
            //封装前台显示数据
            JSONObject obj = null;
            Set<String> eventtypeKey = eventtypeMap.keySet();
            if(eventtypeKey != null && !eventtypeKey.isEmpty()) {
                for(String key: eventtypeKey) {
                    obj = new JSONObject();
                    obj.put("sourceTypeName", eventtypeMap.get(key));
                    count = eventSourceMap.get(key);
                    obj.put("count", count!=null?count:0);
                    result.add(obj);
                }
            }
        }
        return result;
    }

    /**
     * 事件量趋势统计
     * 
     */
    public JSONArray getStatisCaseInfo(CaseInfo caseInfo, String startTime, String endTime) {
        JSONArray result = new JSONArray();

        List<Map<String, Object>> eventSourceList = this.mapper.getStatisCaseInfo(caseInfo, startTime, endTime);

        if (BeanUtil.isNotEmpty(eventSourceList)) {

            // 数据筛选
            Map<String, Map<String, Object>> eventSourceMap = new HashMap<>();
            for (Map<String, Object> eventSource : eventSourceList) {

                String year = String.valueOf(eventSource.get("year"));
                String month = String.valueOf(eventSource.get("month"));
                String key = year + "-" + month;

                // 事件级别名称
                String caseLevel = String.valueOf(eventSource.get("caseLevel"));

                // 当前事件级别个数
                Integer count = Integer.valueOf(String.valueOf(eventSource.get("count")));

                // 是否已存在记录，存在则追加
                Map<String, Object> _eventSource = eventSourceMap.get(key);
                if (_eventSource != null) {
                    _eventSource.put(caseLevel, eventSource.get("count"));

                    // 计算总数
                    Integer total = Integer.valueOf(String.valueOf(_eventSource.get("total")));
                    total += count;

                    _eventSource.put("total", total);

                } else {
                    eventSource.put(caseLevel, count);
                    // 总数
                    eventSource.put("total", count);
                    // 删除多余字段
                    eventSource.remove("caseLevel");
                    eventSource.remove("count");

                    eventSourceMap.put(key, eventSource);
                }
            }

            // 封装插件需要的数据
            Set<String> eventSourceKey = eventSourceMap.keySet();
            //回显数据的长度固定6条
            int size = 6;
            for (String key : eventSourceKey) {
                JSONObject obj = JSONObject.parseObject(JSONObject.toJSONString(eventSourceMap.get(key)));
                // 判断是否缺失回显数据个数，如果缺失默认给0
                if (obj.size() == size) {
                    result.add(obj);
                } else {
                    // 紧急
                    if (obj.get(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_JINJI) == null) {
                        obj.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_JINJI, 0);
                    }
                    // 特急
                    if (obj.get(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_TEJI) == null) {
                        obj.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_TEJI, 0);
                    }
                    // 一般级别
                    if (obj.get(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_NORMAL) == null) {
                        obj.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_NORMAL, 0);
                    }
                    result.add(obj);
                }
            }

        }
        return result;
    }
    
    /**
     * 业务条线分布统计 
     * @param caseInfo 查询条件
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @return
     */
    public JSONArray getStatisBizLine(CaseInfo caseInfo, String startTime, String endTime) {
        JSONArray result = new JSONArray();
        
        Map<String, String> bizType = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
        if(BeanUtil.isEmpty(bizType)) {
            bizType = new HashMap<>();
        }
        
        List<Map<String,Object>> bizLineList = this.mapper.selectBizLine(caseInfo, startTime, endTime);
        if(BeanUtil.isNotEmpty(bizLineList)) {
            JSONObject obj = null;
            for(Map<String,Object> bizLineMap : bizLineList) {
                obj = new JSONObject();
                String bizList= String.valueOf(bizLineMap.get("bizList"));
                //业务条线
                obj.put("bizList", bizList);
                obj.put("count", bizLineMap.get("count"));
                obj.put("bizListName", bizType.get(bizList));
                result.add(obj);
            }
        }
        return result;
    }
    
    
}