package com.bjzhianjia.scp.cgp.biz;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Calendar;
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
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
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

    @Autowired
    private AreaGridBiz areaGridBiz;

    /**
     * 查询未删除的总数
     * 
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
    public JSONObject getStatisCaseState(CaseInfo caseInfo, String startTime, String endTime, String grids) {
        JSONObject result = new JSONObject();
        // 已终止、已完成、处理中、总数
        String[] stateKey = { "stop", "finish", "todo", "total" };

        // 超时统计
        Integer overtime = this.mapper.selectOvertime(caseInfo, startTime, endTime);
        // 处理状态统计
        List<Map<String, Integer>> finishedState = this.mapper.selectState(caseInfo, startTime, endTime, grids);
        String stateName = "";
        for (Map<String, Integer> state : finishedState) {
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

        // 封装处理中、总数
        if (overtime != null && BeanUtil.isNotEmpty(finishedState)) {
            result.put("overtime", overtime);
            // 已终止的
            Integer stopCount = result.getInteger(stateKey[0]);
            stopCount = stopCount == null ? 0 : stopCount;
            // 已完成
            Integer finishCount = result.getInteger(stateKey[1]);
            finishCount = finishCount == null ? 0 : stopCount;
            // 已完成 = 已完成 + 已终止
            result.put(stateKey[1], stopCount + finishCount);

            // 事件总数
            result.put(stateKey[3], this.getCount());
            // 删除多余字段
            result.remove(stateKey[0]);
        }
        return result;
    }

    /**
     * 事件来源分布统计
     * 
     */
    public JSONArray getStatisEventSource(CaseInfo caseInfo, String startTime, String endTime, String gridIds) {
        JSONArray result = new JSONArray();

        List<Map<String, Object>> eventSourceList =
            this.mapper.getStatisEventSource(caseInfo, startTime, endTime, gridIds);
        Map<String, String> eventtypeMap = dictFeign.getByCode(Constances.ROOT_BIZ_EVENTTYPE);
        if (BeanUtil.isNotEmpty(eventtypeMap)) {
            // 事件来源数量
            Integer count = 0;
            // 事件来源类型集处理
            Map<String, Integer> eventSourceMap = new HashMap<>();
            for (Map<String, Object> eventSource : eventSourceList) {
                String key = String.valueOf(eventSource.get("sourceType"));
                count = Integer.valueOf(String.valueOf(eventSource.get("count")));
                eventSourceMap.put(key, count);
            }

            // 封装前台显示数据
            JSONObject obj = null;
            Set<String> eventtypeKey = eventtypeMap.keySet();
            if (eventtypeKey != null && !eventtypeKey.isEmpty()) {
                for (String key : eventtypeKey) {
                    obj = new JSONObject();
                    obj.put("sourceTypeName", eventtypeMap.get(key));
                    count = eventSourceMap.get(key);
                    obj.put("count", count != null ? count : 0);
                    result.add(obj);
                }
            }
        }
        return result;
    }

    /**
     * 事件量趋势统计
     * 
     * @param caseInfod
     *            查询参数
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public List<Map<String, Object>> getStatisCaseInfo(CaseInfo caseInfo, Date startTime, Date endTime,
        String gridIds) {

        String _startTime = DateUtil.dateFromDateToStr(startTime, "yyyy-MM-dd HH:mm:ss");
        String _endTime = DateUtil.dateFromDateToStr(endTime, "yyyy-MM-dd HH:mm:ss");

        List<Map<String, Object>> list = this.mapper.getStatisCaseInfo(caseInfo, _startTime, _endTime, gridIds);

        if (BeanUtil.isNotEmpty(list)) {
            Map<String, Map<String, Object>> tempData = new HashMap<>();
            // 年月
            String ymKey = "";
            for (Map<String, Object> map : list) {
                 ymKey = String.valueOf(map.get("cyear")) + String.valueOf(map.get("cmonth"));
                tempData.put(ymKey, map);
            }
            
            // 初始化日期
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(startTime);
            int yearStart = calStart.get(Calendar.YEAR);
            int monthStart = calStart.get(Calendar.MONTH) + 1;

            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endTime);
            int yearEnd = calEnd.get(Calendar.YEAR);
            int monthEnd = calEnd.get(Calendar.MONTH) + 1;
            
            //封装返回集，并补充为空的数据
            list.clear();
            for (int i = yearStart; i <= yearEnd; i++) {
                for (int j = monthStart; j <= monthEnd; j++) {
                    ymKey = i+""+j;
                    Map<String, Object> resultData = tempData.get(ymKey);
                    //当月没有数据则初始化为0
                    if(resultData == null) {
                        resultData = new HashMap<>();
                        resultData.put("cyear", i); //年
                        resultData.put("cmonth", j); //月
                        resultData.put("total", 0); //总数
                        resultData.put("root_biz_eventLevel_teji", 0); //特急
                        resultData.put("root_biz_eventLevel_jinji", 0);//紧急
                        resultData.put("root_biz_eventLevel_normal", 0);//一般
                    }
                    list.add(resultData);
                   }
            }
        }else {
            
        }
        return list;
    }

    /**
     * 业务条线分布统计
     * 
     * @param caseInfo
     *            查询条件
     * @param gridLevel
     *            网格等级
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public JSONArray getStatisBizLine(CaseInfo caseInfo, String gridLevel, String startTime, String endTime) {
        JSONArray result = new JSONArray();

        // 网格等级
        List<AreaGrid> areaGridList = null;
        // 网格ids
        StringBuilder gridIds = new StringBuilder();
        if (StringUtils.isNotBlank(gridLevel)) {
            areaGridList = areaGridBiz.getByGridLevel(gridLevel);
            int size = areaGridList.size();
            for (int i = 0; i < size; i++) {
                AreaGrid areaGrid = areaGridList.get(i);
                if (i == size - 1) {
                    gridIds.append(areaGrid.getId());
                } else {
                    gridIds.append(areaGrid.getId()).append(",");
                }
            }
        }

        Map<String, String> bizType = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
        if (BeanUtil.isEmpty(bizType)) {
            bizType = new HashMap<>();
        }

        JSONObject obj = null;
        // 返回集初始化
        Map<String, JSONObject> temp = new HashMap<>();
        Set<String> setKey = bizType.keySet();
        for (String key : setKey) {
            obj = new JSONObject();
            obj.put("bizList", key);
            obj.put("count", 0);
            obj.put("bizListName", bizType.get(key));
            temp.put(key, obj);
        }
        // 封装数据库中的数据
        List<Map<String, Object>> bizLineList =
            this.mapper.selectBizLine(caseInfo, gridIds.toString(), startTime, endTime);
        if (BeanUtil.isNotEmpty(bizLineList)) {
            for (Map<String, Object> bizLineMap : bizLineList) {
                obj = new JSONObject();
                String bizList = String.valueOf(bizLineMap.get("bizList"));
                // 业务条线
                obj.put("bizList", bizList);
                obj.put("count", bizLineMap.get("count"));
                obj.put("bizListName", bizType.get(bizList));
                temp.put(bizList, obj);
            }
        }
        // 封装返回集数据
        setKey = temp.keySet();
        for (String key : setKey) {
            result.add(temp.get(key));
        }
        return result;
    }

    /**
     * 网格事件统计
     * 
     * @param caseInfo
     *            查询条件
     * @param gridLevel
     *            网格等级
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public TableResultResponse<Map<String, Object>> getGrid(CaseInfo caseInfo, String gridLevel, String startTime,
        String endTime, Integer page, Integer limit) {
        // 网格等级
        List<AreaGrid> areaGridList = null;
        // 网格ids
        StringBuilder gridIds = new StringBuilder();
        if (StringUtils.isNotBlank(gridLevel)) {
            areaGridList = areaGridBiz.getByGridLevel(gridLevel);
            int size = areaGridList.size();
            for (int i = 0; i < size; i++) {
                AreaGrid areaGrid = areaGridList.get(i);
                if (i == size - 1) {
                    gridIds.append(areaGrid.getId());
                } else {
                    gridIds.append(areaGrid.getId()).append(",");
                }
            }
        }

        Map<String, String> level = dictFeign.getByCode(Constances.ROOT_BIZ_GRID_LEVEL);
        if (BeanUtil.isEmpty(level)) {
            level = new HashMap<>();
        }
        Page<Object> result = PageHelper.startPage(page, limit);
        List<Map<String, Object>> list = this.mapper.selectGrid(caseInfo, startTime, endTime, gridIds.toString());
        if (BeanUtil.isNotEmpty(list)) {
            for (Map<String, Object> map : list) {
                String levelName = level.get(map.get("gridLevel"));
                map.put("gridLevelName", levelName);
            }
            return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
        }
        return new TableResultResponse<Map<String, Object>>(0, null);

    }
}