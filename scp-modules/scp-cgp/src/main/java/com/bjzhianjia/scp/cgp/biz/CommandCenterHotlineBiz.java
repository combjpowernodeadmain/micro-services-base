package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.mapper.CommandCenterHotlineMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

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
     * @return
     */
    public TableResultResponse<CommandCenterHotline> getList(CommandCenterHotline commandCenterHotline, int page,
        int limit) {
        Example example = new Example(commandCenterHotline.getClass());

        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(commandCenterHotline.getHotlnTitle())) {
            criteria.andLike("hotlnTitle", "%"+commandCenterHotline.getHotlnTitle()+"%");
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

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CommandCenterHotline> list = this.selectByExample(example);

        return new TableResultResponse<>(pageInfo.getTotal(), list);
    }
}