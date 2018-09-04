package com.bjzhianjia.scp.cgp.biz;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
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
    public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo, int page, int limit,
        boolean isNoFinish) {
        Example example = new Example(CaseInfo.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        if (isNoFinish) {
            criteria.andEqualTo("isFinished", "0");
            criteria.andNotEqualTo("id", caseInfo.getId());
        }
        
        if (caseInfo.getGrid()!=null) {
            criteria.andEqualTo("grid", caseInfo.getGrid());
        }
        if(StringUtils.isNotBlank(caseInfo.getRegulaObjList())) {
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
    public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo, Set<Integer> ids,
        JSONObject queryData) {
        // 查询参数
        int page =
            StringUtils.isBlank(queryData.getString("page")) ? 1
                : Integer.valueOf(queryData.getString("page"));
        int limit =
            StringUtils.isBlank(queryData.getString("limit")) ? 10
                : Integer.valueOf(queryData.getString("limit"));
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
            Date end =
                DateUtils.addDays(DateUtil.dateFromStrToDate(endQueryTime, "yyyy-MM-dd HH:mm:ss"),
                    1);
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
        if (StringUtils.isNotBlank(isFinished)
            && !CaseInfo.FINISHED_STATE_TODO.equals(isFinished)) {
            criteria.andEqualTo("isFinished", isFinished);
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
}