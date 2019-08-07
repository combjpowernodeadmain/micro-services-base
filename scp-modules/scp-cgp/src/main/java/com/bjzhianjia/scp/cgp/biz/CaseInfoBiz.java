package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.*;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMapper;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.wf.base.constant.Constants;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private PropertiesProxy propertiesProxy;

    @Autowired
    private WfMonitorServiceImpl wfMonitorService;

    @Autowired
    private PatrolTaskBiz patrolTaskBiz;

    @Autowired
    private SpecialEventBiz specialEventBiz;

    @Autowired
    private RegulaObjectBiz regulaObjectBiz;

    @Autowired
    private AreaGridMapper areaGridMapper;

    @Autowired
    private CaseRegistrationBiz caseRegistrationBiz;

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
    public TableResultResponse<JSONObject> getList(CaseInfo caseInfo, int page, int limit, boolean isNoFinish) {
        TableResultResponse<JSONObject> result=new TableResultResponse<>();

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
        if(StringUtils.isNotBlank(caseInfo.getCaseTitle())) {
            criteria.andLike("caseTitle", "%" + caseInfo.getCaseTitle() + "%");
        }
        if(StringUtils.isNotBlank(caseInfo.getCaseCode())) {
            criteria.andLike("caseCode", "%" + caseInfo.getCaseCode() + "%");
        }
        example.setOrderByClause("crt_time desc");

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

        // 添加限时标志
        List<JSONObject> jsonResult=new ArrayList<>();
        for(CaseInfo item:list){
            jsonResult.add(JSONObject.parseObject(JSONObject.toJSONString(item)));
        }
        try {
            addDeadlineFlag(jsonResult);
        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage(e.getMessage());
            return result;
        }

        return new TableResultResponse<>(pageInfo.getTotal(), jsonResult);
    }

    public void addDeadlineFlag(List<JSONObject> list) throws Exception {
        if(BeanUtils.isEmpty(list)){
            return;
        }

        String deadlineFlag = environment.getProperty("deadlineFlag");
        if(StringUtils.isBlank(deadlineFlag)){
            throw new Exception("请设置工单时间字典配置:deadlineFlag=root_system_deadlineFlag");
        }
        Map<String, String> dictValies = dictFeign.getDictIdByCode(deadlineFlag,true);
        if(BeanUtils.isEmpty(dictValies)){
            throw new Exception("请设置工单时间字典");
        }


        Integer[] fraction=new Integer[dictValies.size()];
        Map<Integer,JSONObject> dictValueinstanceMap=new HashMap<>();
        int i=0;
        for(Map.Entry<String,String> e:dictValies.entrySet()){
            JSONObject jsonObject = JSONObject.parseObject(e.getValue());
            Integer value = jsonObject.getInteger("value");
            fraction[i]=value;
            dictValueinstanceMap.put(value, jsonObject);
            i++;
        }

        Arrays.sort(fraction);

        for (JSONObject item : list) {
            if(StringUtils.equals(item.getString("isFinished"), "0")){
                // 还在进行中
                Date crtTime = item.getDate("crtTime");
                Date deadLine = item.getDate("deadLine");
                Date now = new Date();

                if (BeanUtils.isEmpty(deadLine) || BeanUtils.isEmpty(crtTime) || deadLine.before(crtTime)) {
                    // 如果办理期限比创建时间还要短，则认为该事件期限为不紧急
                    JSONObject dictValue = dictValueinstanceMap.get(fraction[0]);
                    item.put("deadLineFlag", dictValue.getString("labelDefault"));
                    continue;
                }

                if(now.after(deadLine)){
                    JSONObject dictValue = dictValueinstanceMap.get(fraction[fraction.length-1]);
                    item.put("deadLineFlag", dictValue.getString("labelDefault"));
                    continue;
                }

                long diff = deadLine.getTime() - crtTime.getTime();
                for (int j = fraction.length-1; j >= 0; j--) {
                    Integer index = fraction[j];
                    JSONObject dictvalue = dictValueinstanceMap.get(index);
                    if ((crtTime.getTime() + diff * (dictvalue.getInteger("value") / 100.0)) <= (now.getTime())) {
                        item.put("deadLineFlag", dictvalue.getString("labelDefault"));
                        break;
                    }
                }
            }else{
                JSONObject dictValue = dictValueinstanceMap.get(fraction[0]);
                item.put("deadLineFlag", dictValue.getString("labelDefault"));
            }
        }
    }

    /**
     * 按分布获取对象
     * 
     * @author 尚
     * @param caseInfo
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
        String isDeleted = queryData.getString("isDeleted");
        boolean caesRegIng = queryData.getBooleanValue("caesRegIng");
        boolean processing = queryData.getBooleanValue("processing");

        Example example = new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();

        if (queryData.getBooleanValue("isIntegratedQuery")) {
            // 综合查询
            // 综合查询有需求按是否删除来进行查询
            if (StringUtils.isNotBlank(isDeleted)) {
                criteria.andEqualTo("isDeleted", isDeleted);
            }
        } else {
            // 非综合查询，只能查询到未删除的
            criteria.andEqualTo("isDeleted", "0");
        }
        // 事件标题或事件编号
        if (StringUtils.isNotBlank(caseInfo.getCaseTitle()) || StringUtils.isNotBlank(caseInfo.getCaseCode())) {
            if (caseInfo.getCaseTitle().length() > 127) {
                throw new BizException("事件名称应该小于127个字符");
            }
            StringBuilder sql = new StringBuilder();
            sql.append(" ( ")
                .append(" ( case_title LIKE ").append(" '%").append(caseInfo.getCaseTitle().trim()).append("%') ")
                .append(" OR ( case_code LIKE ").append(" '%").append(caseInfo.getCaseCode().trim()).append("%') ")
                .append(" ) ");
            criteria.andCondition(sql.toString());
        }
        // 事件相关业务条线
        if (StringUtils.isNotBlank(caseInfo.getBizList())) {
            criteria.andLike("bizList", "%" + caseInfo.getBizList() + "%");
        }
        // 事件相关事件类别
        if (StringUtils.isNotBlank(caseInfo.getEventTypeList())) {
            criteria.andLike("eventTypeList", "%" + caseInfo.getEventTypeList() + "%");
        }
        // 事件来源
        if (StringUtils.isNotBlank(caseInfo.getSourceType())) {
            criteria.andEqualTo("sourceType", caseInfo.getSourceType());
        }
        // 事发起止时间
        if (!(StringUtils.isBlank(startQueryTime) || StringUtils.isBlank(endQueryTime))) {
            Date start = DateUtil.dateFromStrToDate(startQueryTime, "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.addDays(DateUtil.dateFromStrToDate(endQueryTime, "yyyy-MM-dd HH:mm:ss"), 1);
            criteria.andBetween("occurTime", start, end);
        }
        // 事件级别
        if (StringUtils.isNotBlank(caseInfo.getCaseLevel())) {
            criteria.andEqualTo("caseLevel", caseInfo.getCaseLevel());
        }
        // 按ID集合查询
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
            // 将OR关键字两侧条件作为一个整体
            criteria.andCondition(
                "((dead_line < '" + date + "' and is_finished=0) or (dead_line < finish_time and is_finished in(1,2)))");
        }
        // 处理状态：已结案(0:未完成|1:已结案2:已终止)
        if (StringUtils.isNotBlank(isFinished)){
            // 为true表明需要只查询已结案或已终止
            boolean isFinishFlag =
                    CaseInfo.FINISHED_STATE_FINISH.equals(isFinished)
                            || CaseInfo.FINISHED_STATE_STOP.equals(isFinished);
            /*
             * isFinished变量由procCtaskname参数自前端带入，如果该值不为空，表明前端按条件进行查询
             */
            if (isFinishFlag) {
                // 只查询1:已结案2:已终止
                criteria.andEqualTo("isFinished", isFinished);
            } else {
                // 处理状态：只查询未完成
                criteria.andEqualTo("isFinished", CaseInfo.FINISHED_STATE_TODO);
            }
        }

        if (StringUtils.isNotBlank(sourceType) && StringUtils.isNotBlank(sourceCode)
            && Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK.equals(sourceType)) {
            criteria.andEqualTo("sourceType", sourceType);
            criteria.andEqualTo("sourceCode", sourceCode);
        }

        // 查询事件中正在进行案件办理的部分
        if (caesRegIng) {
            List<CaseRegistration> caseRegistrationList = this.caseInfoInReg(ids);
            if (BeanUtil.isNotEmpty(caseRegistrationList)) {
                List<String> idsInReg = caseRegistrationList.stream().map(CaseRegistration::getCaseSource).distinct()
                    .collect(Collectors.toList());
                criteria.andIn("id", idsInReg);
            }
        }

        // 查询事件中没有进行案件办理的部分，即真办结状态
        if (processing) {
            List<CaseRegistration> caseRegistrationList = this.caseInfoInReg(ids);
            if (BeanUtil.isNotEmpty(caseRegistrationList)) {
                List<String> idsInReg = caseRegistrationList.stream().map(CaseRegistration::getCaseSource).distinct()
                    .collect(Collectors.toList());
                criteria.andNotIn("id", idsInReg);
            }
        }

        example.setOrderByClause("crt_time desc");
        // 设置排序字段
        this.setSortColumn(example, queryData);

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseInfo> list = this.mapper.selectByExample(example);

        return new TableResultResponse<CaseInfo>(pageInfo.getTotal(), list);
    }

    /**
     * 设置排序字段
     * <p>
     * 此方法直接接受前端的参数进行sql拼接，修改此方法需注意sql注入
     * </p>
     *
     * @param example   查询对象
     * @param queryData 查询条件
     */
    private void setSortColumn(Example example, JSONObject queryData) {
        // 排序字段。格式: "字段名:(ascending|descending)" ascending升序，descending降序
        Object sortColumn = queryData.get("sortColumn");
        // 判断是否有排序条件
        if (BeanUtils.isNotEmpty(sortColumn)) {
            String column = String.valueOf(sortColumn);
            String[] columns = column.split(":");
            // 排序字段的解析长度
            int len = 2;
            if (len == columns.length) {
                String orderColumn = null;
                // 获取sql拼接字段
                switch (columns[0]) {
                    // 案件编号
                    case "caseCode":
                        orderColumn = "case_code ";
                        break;
                    // 发生时间
                    case "occurTime":
                        orderColumn = "occur_time ";
                        break;
                    // 处理时间
                    case "updTime":
                        orderColumn = "upd_time ";
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

    private List<CaseRegistration> caseInfoInReg(Set<Integer> ids) {
        Example example=new Example(CaseRegistration.class).selectProperties("caseSource");

        Criteria criteria = example.createCriteria();
        criteria.andIn("caseSource",ids);
        criteria.andEqualTo("caseSourceType", environment.getProperty("caseSourceTypeCenter"));

        return caseRegistrationBiz.selectByExample(example);
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
        Integer overtime = this.mapper.selectOvertime(caseInfo, startTime, endTime, grids);
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
            finishCount = finishCount == null ? 0 : finishCount;
            // 已完成 = 已完成 + 已终止
            result.put(stateKey[1], stopCount + finishCount);

            // 事件总数
            result.put(stateKey[3], this.getCount());
            // 删除多余字段
            result.remove(stateKey[0]);
        }

        // 补0
        for (int i = 1; i < stateKey.length; i++) {
            result.putIfAbsent(stateKey[i], 0);
        }
        result.putIfAbsent("overtime",0);
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
     * 
     * @param caseInfo
     *            查询参数
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param gridIds
     * @return
     */
    public List<Map<String, Object>> getStatisCaseInfo(CaseInfo caseInfo, String startTime, String endTime,
        String gridIds) {
        List<Map<String, Object>> list = this.mapper.getStatisCaseInfo(caseInfo, startTime, endTime, gridIds);

        if (BeanUtil.isNotEmpty(list)) {
            Map<String, Map<String, Object>> tempData = new HashMap<>();
            // 年月
            String ymKey = "";
            for (Map<String, Object> map : list) {
                ymKey = String.valueOf(map.get("cyear")) + String.valueOf(map.get("cmonth"));
                tempData.put(ymKey, map);
            }

            // 封装返回集，并补充为空的数据
            list.clear();

            // 开始日期
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(DateUtil.dateFromStrToDate(startTime, DateUtil.DEFAULT_DATE_FORMAT));
            // 结束日期
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(DateUtil.dateFromStrToDate(endTime, DateUtil.DEFAULT_DATE_FORMAT));

            int startYear = 0;
            int startMonth = 0;

            while (calStart.before(calEnd) || (calStart.get(Calendar.MONTH) == calEnd.get(Calendar.MONTH))) {
                startYear = calStart.get(Calendar.YEAR);
                startMonth = calStart.get(Calendar.MONTH) + 1;

                ymKey = startYear + "" + startMonth;
                Map<String, Object> resultData = tempData.get(ymKey);
                // 当月没有数据则初始化为0
                if (resultData == null) {
                    resultData = new HashMap<>();
                    resultData.put("cyear", startYear); // 年
                    resultData.put("cmonth", startMonth); // 月
                    resultData.put("total", 0); // 总数
                    resultData.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_TEJI, 0); // 特急
                    resultData.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_JINJI, 0);// 紧急
                    resultData.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_NORMAL, 0);// 一般
                }
                list.add(resultData);

                calStart.add(Calendar.MONTH, 1);
            }

        } else {
            list = new ArrayList<>();
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

        // 网格ids
        Set<String> gridIdSet = new HashSet<>();
        Map<Integer, Set<String>> gridIdBindChildren =
            areaGridBiz.getByLevelBindChildren(gridLevel);
        for (Map.Entry<Integer, Set<String>> e : gridIdBindChildren.entrySet()) {
            gridIdSet.addAll(e.getValue());
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
            this.mapper.selectBizLine(caseInfo, gridIdSet, startTime, endTime);
        if (BeanUtil.isNotEmpty(bizLineList)) {
            for (Map<String, Object> bizLineMap : bizLineList) {
                obj = new JSONObject();
                String bizList = String.valueOf(bizLineMap.get("bizList"));
                if(StringUtils.isNotBlank(bizType.get(bizList))){
                    // 业务条线
                    obj.put("bizList", bizList);
                    obj.put("count", bizLineMap.get("count"));
                    obj.put("bizListName", bizType.get(bizList));
                    temp.put(bizList, obj);
                }
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
    public TableResultResponse<JSONObject> getGrid(CaseInfo caseInfo, String gridLevel, String startTime,
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
        List<JSONObject> list = this.mapper.selectByGrid(caseInfo, startTime, endTime, gridIds.toString(),gridLevel);

        if (BeanUtil.isNotEmpty(list)) {
            for (JSONObject map : list) {
                String levelName = level.get(map.get("gridLevel"));
                map.put("gridLevelName", levelName);
                /*
                 * 在mysql里，count为关键字，修改sql语句加下划线，但前端已按count字样对接
                 * 在此做下处理
                 */
                map.put("total", map.getInteger("total_") == null ? 0 : map.getInteger("total_"));
                map.put("count", map.getInteger("count_") == null ? 0 : map.getInteger("count_"));
                map.remove("total_");
                map.remove("count_");
            }
            return new TableResultResponse<>(result.getTotal(), list);
        }
        return new TableResultResponse<>(0, null);

    }
    
    /**
     * 查询类型为专项管理的事件
     * @param patrolId
     * @return
     */
    public TableResultResponse<JSONObject> patrolCaseInfo(Integer patrolId){
        TableResultResponse<JSONObject> restResult=new TableResultResponse<>();
        
        Example example=new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("sourceType", environment.getProperty("sourceTypeKeyPatrol"));
        // 对是否输入了按patrolId进行查询进行判断，如果为null，则认为查询所有巡查事项类型的事件
        if(patrolId!=null){
            criteria.andEqualTo("sourceCode", patrolId);
        }

        List<CaseInfo> caseInfo = this.selectByExample(example);
        List<JSONObject> resultJObjList=new ArrayList<>();
        if(BeanUtil.isNotEmpty(caseInfo)) {
            for (CaseInfo caseInfo2 : caseInfo) {
                try {
                    JSONObject caseInfoJobj = propertiesProxy.swapProperties(caseInfo2, "id","caseTitle","mapInfo");
                    resultJObjList.add(caseInfoJobj);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            
            restResult.setStatus(200);
            restResult.getData().setTotal(resultJObjList.size());
            restResult.getData().setRows(resultJObjList);
            return restResult;
        }
        
        restResult.getData().setRows(new ArrayList<>());
        restResult.getData().setTotal(0);
        return restResult;
    }

    /**
     * 专项事件全部定位
     * @return
     */
    public TableResultResponse<JSONObject> allPositionPatrol() {
        return this.patrolCaseInfo(null);
    }

    /**
     * 通过部门id和事件等级获取事件列表
     *
     * @param caseLevel 事件等级
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<Map<String, Object>> getCaseInfoByDeptId(String caseLevel, String deptId, int page,
                                                                        int limit) {
        Page<Object> pageHelper = PageHelper.startPage(page, limit);
        List<Map<String, Object>> result = this.mapper.selectCaseInfoByDeptId(deptId, caseLevel);
        if (BeanUtil.isEmpty(result)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
        //拼接业务ids
        StringBuilder procBizid = new StringBuilder();
        Map<String, Object> tmap;
        for (int i = 0; i < result.size(); i++) {
            tmap = result.get(i);
            if (BeanUtils.isEmpty(tmap)) {
                continue;
            }
            procBizid.append("'").append(tmap.get("id")).append("'");
            if (i < result.size() - 1) {
                procBizid.append(",");
            }
        }
        //工作流业务
        JSONObject  bizData = new JSONObject();
        //事件流程编码
        bizData.put("procKey","comprehensiveManage");
        //业务ids
        bizData.put(Constants.WfProcessBizDataAttr.PROC_BIZID,procBizid.toString());
        JSONObject objs = this.initWorkflowQuery(bizData);

        //缓存工作流实例id
        Map<String,Map<String,Object>> tempProcInstIds = new HashMap<>();
        List<Map<String,Object>>  procInstIdList = wfMonitorService.getProcInstIdByUserId(objs);
        for(Map<String,Object> map : procInstIdList){
            //procInstId实例id , procBizid 业务id
            tempProcInstIds.put(String.valueOf(map.get(Constants.WfProcessBizDataAttr.PROC_BIZID)),map);
        }

        Map<String,String>  dictTemp = dictFeign.getByCode(Constances.ROOT_BIZ_EVENTLEVEL);

        for(Map<String,Object> map : result){
            String caseLevelName = dictTemp.get(map.get("caseLevel"));
            map.put("caseLevelName",caseLevelName);
            map.put("isSupervise","0".equals(map.get("isSupervise"))?false:true);
            map.put("isUrge","0".equals(map.get("isUrge"))?false:true);

            // 向结果集添加工作流数据
            Map<String, Object> wfTaskMap = tempProcInstIds.get(String.valueOf(map.get("id")));
            if(BeanUtil.isNotEmpty(wfTaskMap)){
                map.put("procInstId",wfTaskMap.get("procInstId"));
                map.put("procTaskCommittime",wfTaskMap.get("procTaskCommittime"));
                map.put("procCtaskName",wfTaskMap.get("procCtaskName"));
            }
        }
        return new TableResultResponse<>(pageHelper.getTotal(), result);
    }
    /**
     * 初始化工作流参数
     * @param bizData 工作流参数
     * @return
     */
    private JSONObject initWorkflowQuery(JSONObject  bizData){
        JSONObject objs = new JSONObject();
        //流程参数
        JSONObject  procData = new JSONObject();
        //用户认证方式
        JSONObject  authData = new JSONObject();
        authData.put("procAuthType",2);
        authData.put(Constants.WfProcessAuthData.PROC_DEPATID, BaseContextHandler.getDepartID());
        //流程变量
        JSONObject  variableData = new JSONObject();
        //工作流参数
        objs.put(Constants.WfRequestDataTypeAttr.PROC_BIZDATA,bizData);
        objs.put(Constants.WfRequestDataTypeAttr.PROC_PROCDATA,procData);
        objs.put(Constants.WfRequestDataTypeAttr.PROC_AUTHDATA,authData);
        objs.put(Constants.WfRequestDataTypeAttr.PROC_VARIABLEDATA,variableData);
        return objs;
    }

    /**
     * 执法片区事件统计
     *
     * @param gridLevel 网格等级
     * @param limlt     保留多少条数据
     * @return
     */
    public List<JSONObject> getGridZFPQ(String gridLevel, Integer limlt) {
        // 判断是否存在网格等级，没有则采用默认配置文件中的属性
        if (StringUtils.isBlank(gridLevel)) {
            gridLevel = environment.getProperty("statisticsCenter.areaGrid.gridLevel");
        }

        List<JSONObject> gridZFPQListFinish = this.mapper.getGridZFPQ("1", gridLevel);
        List<JSONObject> gridZFPQListTodo = this.mapper.getGridZFPQ("0", gridLevel);


        List<AreaGrid> areaGridList = areaGridBiz.getByGridLevel(gridLevel);

        List<JSONObject> resultJObjList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(areaGridList)) {
            Map<Integer, JSONObject> zfpqIdJObjMapTodo = new HashMap<>();
            Map<Integer, JSONObject> zfpqIdJObjMapFinish = new HashMap<>();
            if (BeanUtil.isNotEmpty(gridZFPQListTodo)) {
                for (JSONObject gridJobjTmp : gridZFPQListTodo) {
                    zfpqIdJObjMapTodo.put(gridJobjTmp.getInteger("zfqp"), gridJobjTmp);
                }
            }
            if (BeanUtil.isNotEmpty(gridZFPQListFinish)) {
                for (JSONObject gridJobjTmp : gridZFPQListFinish) {
                    zfpqIdJObjMapFinish.put(gridJobjTmp.getInteger("zfqp"), gridJobjTmp);
                }
            }

            // 整合处理结果
            for (AreaGrid areaGridTmp : areaGridList) {
                JSONObject resultJobj = new JSONObject();

                JSONObject jsonObjectTodo =
                        zfpqIdJObjMapTodo.get(areaGridTmp.getId()) == null ? new JSONObject()
                                : zfpqIdJObjMapTodo.get(areaGridTmp.getId());
                JSONObject jsonObjectFinish =
                        zfpqIdJObjMapFinish.get(areaGridTmp.getId()) == null ? new JSONObject()
                                : zfpqIdJObjMapFinish.get(areaGridTmp.getId());

                resultJobj.put("grid", areaGridTmp.getId());
                resultJobj.put("stateTodo", jsonObjectTodo.getInteger("ccount") == null ? 0
                        : jsonObjectTodo.getInteger("ccount"));
                resultJobj.put("stateFinish", jsonObjectFinish.getInteger("ccount") == null ? 0
                        : jsonObjectFinish.getInteger("ccount"));
                resultJobj.put("gridLevel", areaGridTmp.getGridLevel());
                resultJobj.put("gridName", areaGridTmp.getGridName());
                resultJobj.put("gridCode", areaGridTmp.getGridCode());

                resultJObjList.add(resultJobj);
            }
            if (BeanUtil.isNotEmpty(resultJObjList)) {
                // 办结量从大到小排序
                resultJObjList.sort(new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        return o2.getInteger("stateFinish") - o1.getInteger("stateFinish");
                    }
                });
                // 保留指定条数，多余剔除
                if (resultJObjList.size() > limlt) {
                    List<JSONObject> temp = new ArrayList<>();
                    for (int i = 0; i < limlt; i++) {
                        temp.add(resultJObjList.get(i));
                    }
                    // 缓存中数据替换结果集
                    resultJObjList = temp;
                }
            }
        }
        return resultJObjList;
    }

    /**
     * 事件势力学统计
     * @param caseInfo
     */
    public TableResultResponse<JSONObject> heatMap(CaseInfo caseInfo,String startDate,String endDate) {
        DateTime dateTime = new DateTime(DateUtil.dateFromStrToDate(endDate, "yyyy-MM").getTime());
        DateTime dateTime1 = dateTime.plusMonths(1);
        endDate =
                DateUtil.dateFromDateToStr(new Date(dateTime1.plusMonths(1).getMillis()), "yyyy-MM");

        List<JSONObject> rows = this.mapper.heatMap(caseInfo, startDate,endDate);
        if(BeanUtil.isNotEmpty(rows)){
            return new TableResultResponse<>(0, rows);
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * 按条件查询事件列表，查询结果不进行分页
     * @param queryCaseInfo
     * @param specialEventId
     */
    public List<CaseInfo> getList(CaseInfo queryCaseInfo, Integer specialEventId) {
        // 查询与专项管理记录对应的巡查事项
        Map<String,Object> conditionMap=new HashMap<>();
        conditionMap.put("sourceType", environment.getProperty("patrolType.special"));
        conditionMap.put("sourceTaskId", specialEventId);
        List<PatrolTask> patrolTaskList = patrolTaskBiz.getByMap(conditionMap);
        List<Integer> patrolTaskIdList;
        if(BeanUtil.isNotEmpty(patrolTaskList)){
            patrolTaskIdList = patrolTaskList.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());
        }else{
            // 如果没有查到对应的专项管理记录，则不会有专项巡查及专项巡查对应的事件存在
            return new ArrayList<>();
        }

        Example example=new Example(CaseInfo.class).selectProperties("id","mapInfo","caseTitle");
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");

        if(StringUtils.isNotBlank(queryCaseInfo.getSourceType())){
            criteria.andEqualTo("sourceType", queryCaseInfo.getSourceType());
        }
        if(StringUtils.isNotBlank(queryCaseInfo.getIsFinished())){
            criteria.andEqualTo("isFinished", queryCaseInfo.getIsFinished());
        }
        if(BeanUtil.isNotEmpty(patrolTaskIdList)){
            criteria.andIn("sourceCode", patrolTaskIdList);
        }

        List<CaseInfo> caseInfoList = this.selectByExample(example);
        if(BeanUtil.isNotEmpty(caseInfoList)){
            return caseInfoList;
        }

        return new ArrayList<>();
    }

    /**
     * 为指挥中心首页查询列表
     *
     * @param queryData
     * @return
     */
    public TableResultResponse<JSONObject> getListForHome(JSONObject queryData) {
        TableResultResponse<JSONObject> tableResult = new TableResultResponse<>();

        // 查询专项管理
        SpecialEvent specialEvent =
            specialEventBiz.selectById(Integer.valueOf(queryData.getString("sourceTaskId")));

        List<RegulaObject> regulaObjectList;
        if (BeanUtil.isNotEmpty(specialEvent)
            && StringUtils.isNotBlank(specialEvent.getRegObjList())) {
            Set<String> objTypeIdS=new HashSet<>();
            objTypeIdS.addAll(Arrays.asList(specialEvent.getRegObjList().split(",")));
            /*
             * 查询专项任务里监管对象类型下的监管对象
             * 专项任务里记录的是监管对象类型，需要使用监管对象类型查询出监管对象
             */
            regulaObjectList = regulaObjectBiz.getListByObjTypeIds(objTypeIdS);

            if(BeanUtil.isEmpty(regulaObjectList)){
                // 如果没有找到监管对象，则提示错误
                tableResult.setMessage("未找到相关监管对象");
                tableResult.setStatus(400);
                return tableResult;
            }
        } else {
            // 如果专项下没有记录监管对象类型，则提示错误
            tableResult.setMessage("未找到相关监管对象");
            tableResult.setStatus(400);
            return tableResult;
        }

        queryData.put("regulaObjectId", null);

        List<JSONObject> listForHome = this.mapper.getListForHome(queryData);
        List<Integer> regulaObjectIds=new ArrayList<>();
        if (BeanUtil.isNotEmpty(listForHome)) {
            regulaObjectIds =
                listForHome.stream().map(o -> o.getInteger("regulaObjectId")).distinct()
                    .collect(Collectors.toList());
        }

        // 将还没有巡查到的监管对象进行整合
        if (BeanUtil.isNotEmpty(regulaObjectList)) {
            Map<Integer, RegulaObject> collectMap = new HashMap<>();
            for (RegulaObject tmpReg : regulaObjectList) {
                if ("0".equals(tmpReg.getIsDeleted())) {
                    collectMap.put(tmpReg.getId(), tmpReg);
                }
            }
            for (Map.Entry<Integer, RegulaObject> e : collectMap.entrySet()) {
                if (!regulaObjectIds.contains(e.getKey())) {
                    // 说明查到的结果集里不包含该监管对象，需要补充到结果集里
                    JSONObject tmpJobj = new JSONObject();
                    tmpJobj.put("patrolCount", 0);
                    tmpJobj.put("objName", e.getValue().getObjName());
                    tmpJobj.put("mapInfo", e.getValue().getMapInfo());
                    tmpJobj.put("regulaObjectId", e.getKey());
                    tmpJobj.put("pCountWithProblem", 0);
                    listForHome.add(tmpJobj);
                }
            }
        }

        tableResult.getData().setRows(listForHome);
        tableResult.getData().setTotal(listForHome.size());
        return tableResult;
    }

    /**
     * 通过监管对象ids，获取监管对象所属事件量
     *
     * @param regulaObjIds 监管对象ids
     * @return
     */
    public List<Map<String, Long>> getByRegulaIds(Set<String> regulaObjIds) {
        if (BeanUtil.isEmpty(regulaObjIds)) {
            return new ArrayList<>();
        }
        List<Map<String, Long>> result = this.mapper.selectByRegulaIds(regulaObjIds);
        return BeanUtil.isEmpty(regulaObjIds) ? new ArrayList<>() : result;
    }

    /**
     * 按网格等级统计事件量，所统计的事件量包含子网格<br/>
     *
     * @param caseInfo
     * @param gridLevel
     * @param startTime
     * @param endTime
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> statisticsByGridLevel(CaseInfo caseInfo, String gridLevel, String startTime, String endTime, Integer page, Integer limit){
        if (BeanUtils.isEmpty(gridLevel)) {
            gridLevel = environment.getProperty("areaGrid.gridLevel.zrwg.zfpq");
        }

        // 查询与gridLevel对应的网格ID,返回结果如：(Map){"2","2,3,4,5"},{"10","12,13,14,15"}
        Map<Integer, Set<String>> gridIdBindChildrenMap = areaGridBiz.getByLevelBindChildren(gridLevel);
        /*
         * 处理查询的网格数据，整理后格式如：(List)
         * [
         *      {"parentId":"2","gridChildrenSet":"'2','3','4','5'"},
         *      {"parentId":"10","gridChildrenSet":"'10','13','14','15'"}
         * ]
         */
        List<JSONObject> queryData=new ArrayList<>();
        if(BeanUtil.isNotEmpty(gridIdBindChildrenMap)){
            for(Map.Entry<Integer,Set<String>> e:gridIdBindChildrenMap.entrySet()){
                JSONObject gridIdSetBindChildrenJObj=new JSONObject();
                gridIdSetBindChildrenJObj.put("parentId", e.getKey());
                String join = String.join(",", e.getValue());
                String gridChildrenSet = "'" + StringUtils.replace(join, ",", "','") + "'";
                gridIdSetBindChildrenJObj.put("gridChildrenSet", gridChildrenSet);
                queryData.add(gridIdSetBindChildrenJObj);
            }
        } else {
            // 如果连网格都没有找到，则认为查询结果可为空，直接返回空结果
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        // 查询网格等级,"root_biz_grid_level"是网格等级在字典中前缀
        Map<String, String> gridLevelDictValueMap = dictFeign.getByCode(environment.getProperty("gridLevelType"));

        List<JSONObject> result = this.mapper.statisticsByGridLevel(queryData,caseInfo,startTime,endTime);
        Set<Integer> gridIdInResult=new HashSet<>();
        if(BeanUtil.isNotEmpty(result)){

            // 查询网格等级

            if (BeanUtil.isEmpty(gridLevelDictValueMap)) {
                gridLevelDictValueMap = new HashMap<>();
            }

            for(JSONObject tmp:result){
                int total=tmp.getIntValue("stateTodo")+tmp.getIntValue("stateStop")+tmp.getIntValue("stateFinish");
                // 计算事件量总数
                tmp.put("total", total);
                // 整合网格等级名称
                tmp.put("gridLevelName", gridLevelDictValueMap.get(tmp.getString("gridLevel")));
                // 收集已经查询到的网格
                gridIdInResult.add(tmp.getInteger("grid"));
            }

        }

        /*
         * "把没涉及到的网格填充到结果集内",把该逻辑放到"BeanUtil.isNotEmpty(result)"非空判断外
         * 以保证在result为空时，也会执行"把没涉及到的网格填充到结果集内"逻辑
         */
        // 把没涉及到的网格填充到结果集内
        Set<String> gridIdToFilling=new HashSet<>();
        for(Integer gridId:gridIdBindChildrenMap.keySet()){
            if(!gridIdInResult.contains(gridId)){
                gridIdToFilling.add(String.valueOf(gridId));
            }
        }
        if(BeanUtil.isNotEmpty(gridIdToFilling)){
            List<AreaGrid> areaGrids = areaGridMapper.selectByIds(String.join(",", gridIdToFilling));
            List<String> parentIds =
                    areaGrids.stream().map(o -> String.valueOf(o.getGridParent())).distinct()
                            .collect(Collectors.toList());
            List<AreaGrid> areaGrids1 = areaGridMapper.selectByIds(String.join(",", parentIds));
            Map<Integer, String> areaGridIdNameMap = new HashMap<>();
            if (BeanUtil.isNotEmpty(areaGrids1)) {
                areaGridIdNameMap =
                        areaGrids1.stream()
                                .collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));
            }
            if(BeanUtil.isNotEmpty(areaGrids)){
                for(AreaGrid tmp:areaGrids){
                    JSONObject jsonTmp=new JSONObject();
                    jsonTmp.put("grid", tmp.getId());
                    jsonTmp.put("gridName", tmp.getGridName());
                    jsonTmp.put("stateFinish", 0);
                    jsonTmp.put("stateTodo", 0);
                    jsonTmp.put("stateStop", 0);
                    jsonTmp.put("total", 0);
                    jsonTmp.put("gridLevel", tmp.getGridLevel());
                    jsonTmp.put("gridLevelName", gridLevelDictValueMap.get(tmp.getGridLevel()));
                    jsonTmp.put("gridCode", tmp.getGridCode());
                    jsonTmp.put("gridParent", tmp.getGridParent());
                    jsonTmp.put("gridParentName", areaGridIdNameMap.get(tmp.getGridParent()));
                    result.add(jsonTmp);
                }
            }
        }
        return new TableResultResponse<>(result.size(), result);
    }

    public TableResultResponse<CaseInfo> getListFocusOn(Set<Integer> bizIds,int page,int limit) {
        TableResultResponse<CaseInfo> result=new TableResultResponse<>();

        if(BeanUtils.isEmpty(bizIds)){
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        Example example = new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andEqualTo("isFinished",BooleanUtil.BOOLEAN_FALSE);
        criteria.andIn("id", bizIds);

        String nowDate = DateUtil.dateFromDateToStr(new Date(), DateUtil.DEFAULT_DATE_FORMAT);
        StringBuffer sqlBuffer = new StringBuffer("(is_supervise='1' OR is_urge='1'\n" + "\tOR '").append(nowDate)
            .append("' > dead_line\n");

        String focusOnCaseLevel = environment.getProperty("focusOnCaseLevel");
        if(StringUtils.isBlank(focusOnCaseLevel)){
            result.setMessage("请设置focusOnCaseLevel");
            result.setStatus(400);
            return result;
        }

        String[] split = focusOnCaseLevel.split(",");
        for(int i=0;i<split.length;i++){
            String str=split[i];
            if(i==split.length-1){
                sqlBuffer.append("\tOR case_level='").append(str).append("')");
            }else{
                sqlBuffer.append("\tOR case_level='").append(str).append("'");
            }
        }


        criteria.andCondition(sqlBuffer.toString());

        example.setOrderByClause("crt_time desc");

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseInfo> list = this.mapper.selectByExample(example);

        return new TableResultResponse<>(pageInfo.getTotal(), list);
    }

    /**
     * 查询员工考核里工单的信息
     * @param month
     * @param userId
     * @param exeStatus
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> gongdaiInMemberStatistics(String month, String userId, String exeStatus,
        Integer page, Integer limit) {
        Date startTime;
        Date endTime;

        // 查询起止时间为某月第一天凌晨到次月第一天凌晨
        if (StringUtils.isBlank(month)) {
            startTime = DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(new Date()));
            endTime = DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(DateUtil.theDayOfMonthPlus(new Date(), 1)));
        } else {
            startTime =
                DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(DateUtil.dateFromStrToDate(month, "yyyy-MM")));

            endTime = DateUtil.getDayStartTime(DateUtil
                .theFirstDayOfMonth(DateUtil.theDayOfMonthPlus(DateUtil.dateFromStrToDate(month, "yyyy-MM"), 1)));
        }


        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<JSONObject> jsonObjects = this.mapper.gongdaiInMemberStatistics(startTime, endTime, userId, exeStatus);

        return new TableResultResponse<>(pageInfo.getTotal(), jsonObjects);
    }
}