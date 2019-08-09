package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.CommandCenterHotlineMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 记录来自指挥中心热线的事件
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-28 19:20:36
 */
@Service
public class CommandCenterHotlineBiz extends BusinessBiz<CommandCenterHotlineMapper, CommandCenterHotline> {

    @Autowired
    private CaseInfoBiz caseInfoBiz;

    @Autowired
    private Environment environment;

    @Autowired
    private DictFeign dictFeign;

    /**
     * 添加指挥长热线暂存<br/>
     * 
     * @param instance
     * @param exeStatus
     *            热线状态，如果该参数为null，则默认为【未发起】
     * @return
     */
    public Result<Void> createCache(CommandCenterHotline instance, String exeStatus) {
        Result<Void> result = checkCache(instance);
        if (!result.getIsSuccess()) {
            return result;
        }

        // 借用市长热线的完成状态
        instance
            .setExeStatus(exeStatus == null ? Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_TODO : exeStatus);
        this.insertSelective(instance);

        result.setIsSuccess(true);
        return result;
    }

    private Result<Void> checkCache(CommandCenterHotline instance) {
        Result<Void> result = new Result<>();
        result.setIsSuccess(false);

        // 生成热线编号
        CommandCenterHotline maxOne = getMaxOne();
        String currentCode = null;
        if (maxOne == null) {
            Result<String> geneResult = CommonUtil.generateCaseCode(null);
            currentCode = "ZXRX" + geneResult.getData();
        } else {
            Result<String> geneResult = CommonUtil.generateCaseCode(maxOne.getHotlnCode().substring(4));
            currentCode = "ZXRX" + geneResult.getData();
        }
        instance.setHotlnCode(currentCode);

        result.setIsSuccess(true);
        return result;
    }

    /**
     * 获取ID最大的那条记录
     * 
     * @return
     */
    public CommandCenterHotline getMaxOne() {
        Example example = new Example(CommandCenterHotline.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");

        example.setOrderByClause("id desc");
        PageHelper.startPage(1, 1);

        List<CommandCenterHotline> list = this.selectByExample(example);
        if (BeanUtil.isNotEmpty(list)) {
            return list.get(0);
        }

        return null;
    }

    /**
     * 添加预立案
     * 
     * @param instance
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Result<CaseInfo> createInstance(CommandCenterHotline instance) throws Exception {
        Result<CaseInfo> result = new Result<>();

        /*
         * 1->按指挥长热线事件ID进行查询，验证用户在使用过程中是否进行了暂存
         * 1.1->是：将该暂存记录与当前要进行存储的预立案记录关联，并更新热线记录处理状态为【处理中】 1.2->否：同时创建热线记录与预立案记录
         * 1.2->与前端约定，如果用户暂存过，则将暂存记录的ID传入后台，以判断是否进行过暂存
         */
        if (instance.getId() == null) {
            // 未暂存过
            Result<Void> createCacheResult =
                this.createCache(instance, Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DOING);
            if (!createCacheResult.getIsSuccess()) {
                throw new Exception(createCacheResult.getMessage());
            }
        } else {
            // 暂存过
            instance.setExeStatus(Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DOING);
            this.updateSelectiveById(instance);
        }

        // 创建预立案信息
        Result<CaseInfo> resultCaseInfo = createCaseInfo(instance);

        CaseInfo caseInfo = resultCaseInfo.getData();
        caseInfoBiz.insertSelective(caseInfo);

        result.setData(caseInfo);
        result.setIsSuccess(true);
        return result;
    }

    private Result<CaseInfo> createCaseInfo(CommandCenterHotline instance) throws Exception {
        Result<CaseInfo> result = new Result<>();

        CaseInfo maxOne = caseInfoBiz.getMaxOne();

        CaseInfo caseInfo = new CaseInfo();
        // 来源信息
        caseInfo.setSourceType(Constances.BizEventType.ROOT_BIZ_EVENTTYPE_COMMAND_LINE);
        caseInfo.setSourceCode(String.valueOf(instance.getId()));
        // 生成立案单编号
        Result<String> caseCodeResult = CommonUtil.generateCaseCode(maxOne == null ? null : maxOne.getCaseCode());
        if (!caseCodeResult.getIsSuccess()) {
            result.setMessage(caseCodeResult.getMessage());
            throw new Exception(caseCodeResult.getMessage());// 向外抛出异常，使事务回滚
        }
        caseInfo.setCaseCode(caseCodeResult.getData());
        caseInfo.setCaseTitle(instance.getHotlnTitle());
        caseInfo.setCaseDesc(instance.getAppealDesc());
        caseInfo.setOccurTime(instance.getAppealDatetime());

        caseInfo.setBizList(instance.getBizType());
        caseInfo.setEventTypeList(String.valueOf(instance.getEventType()));

        result.setIsSuccess(true);
        result.setData(caseInfo);
        return result;
    }

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
    public TableResultResponse<CommandCenterHotline> getList(CommandCenterHotline commandCenterHotline, int page,
                                                             int limit, String startTime, String endTime,String sortColumn) {
        Example example = new Example(commandCenterHotline.getClass());

        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        // 通过热线名称或者编码查询
        if (StringUtils.isNotBlank(commandCenterHotline.getHotlnTitle())) {
            StringBuilder sql = new StringBuilder();
            sql.append("( (hotln_title like '%").append(commandCenterHotline.getHotlnTitle()).append("%') ")
                    .append(" OR (hotln_code like '%").append(commandCenterHotline.getHotlnCode()).append("%')")
                    .append(") ");
            criteria.andCondition(sql.toString());
        }

        if (StringUtils.isNotBlank(commandCenterHotline.getAppealPerson())) {
            criteria.andLike("appealPerson", "%"+commandCenterHotline.getAppealPerson()+"%");
        }
        if (StringUtils.isNotBlank(commandCenterHotline.getBizType())) {
            criteria.andEqualTo("bizType", commandCenterHotline.getBizType());
        }
        if (StringUtils.isNotBlank(commandCenterHotline.getExeStatus())) {
            criteria.andEqualTo("exeStatus", commandCenterHotline.getExeStatus());
        }
        if(BeanUtil.isNotEmpty(startTime)&&BeanUtil.isNotEmpty(endTime)){
            /*
             * 当开始时间与结束时间都不为空时才进行按时间范围查询
             * 查询结束时间包括当天
             */
            Date endTimeForQuery =
                DateUtil.theDayOfTommorrow(
                    DateUtil.dateFromStrToDate(endTime, DateUtil.DEFAULT_DATE_FORMAT));
            Date startTimeForQuery =
                DateUtil.dateFromStrToDate(startTime, DateUtil.DEFAULT_DATE_FORMAT);
            criteria.andBetween("appealDatetime", startTimeForQuery, endTimeForQuery);
        }

        // 判断是否存在排序字段
        if (StringUtils.isNotBlank(sortColumn)) {
            this.setSortColumn(example, sortColumn);
        } else {
            // 默认id降序
            example.setOrderByClause(" id desc");
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CommandCenterHotline> list = this.selectByExample(example);
        return new TableResultResponse<>(pageInfo.getTotal(), list);
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
                    // 热线事项编号
                    case "hotlnCode":
                        orderColumn = "hotln_code ";
                        break;
                    // 诉求时间
                    case "appealDatetime":
                        orderColumn = "appeal_datetime ";
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
     * 批量删除对象
     *
     * @author 尚
     * @param ids
     *            待删除ID集合
     */
    public Result<Void> remove(Integer[] ids) {
        Result<Void> result = new Result<>();

        List<String> idList = new ArrayList<>();
        for (Integer integer : ids) {
            idList.add(String.valueOf(integer));
        }

        List<CommandCenterHotline> selectByIds = this.mapper.selectByIds(String.join(",", idList));

        // 指挥中心热线状态借用市长热线的状态root_biz_12345state_todo
        String toDoExeStatus = environment.getProperty("root_biz_12345state_todo");

        for (CommandCenterHotline commandCenterHotline : selectByIds) {
            if (!toDoExeStatus.equals(commandCenterHotline.getExeStatus())) {
                Map<String, String> todoNameMap = dictFeign.getByCode(toDoExeStatus);
                result.setIsSuccess(false);
                result.setMessage("不能删除非【" + todoNameMap.get(toDoExeStatus) + "】的事件");
                return result;
            }
        }

        this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(),
                BaseContextHandler.getUsername(), new Date());

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }
}