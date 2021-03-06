package com.bjzhianjia.scp.security.wf.base.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Description: 工作流系统返回码，以02作为
 * @author scp
 * @version 1.0
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年8月31日    scp       1.0        1.0 Version 
 * </pre>
 */
public enum WorkflowEnumResults implements EnumResultTemplate {
    /** 工作流公共返回码，编号以0200开头  **/
    WF_COMM_02000001("02000001", "系统处理过程中发生异常", "系统处理过程中发生异常，请联系管理员"),
    WF_COMM_02000002("02000002", "初始化用户信息失败", "初始化用户信息失败，请联系管理员"),
    WF_COMM_02000003("02000003", "未通过用户认证，不能进行相关操作", "未指定或不支持的用户认证方式，不能进行相关操作"),
    WF_COMM_02000004("02000004", "未通过用户认证，不能进行相关操作", "用户Token认证未通过，不能进行相关操作"),
    WF_COMM_02000005("02000005", "未通过用户认证，不能进行相关操作", "用户未登录，不能进行相关操作"),
    WF_COMM_02000006("02000006", "未通过用户认证，不能进行相关操作", "不支持的用户认证方式，不能进行相关操作"),
    
    /** 流程设计相关返回码，编号以0201开头  **/
    WF_DESIGN_02010001("02010001", "流程部署失败，部署ID为空", "流程部署失败，部署ID为空"),
    WF_DESIGN_02010002("02010002", "当前用户没有权限部署该流程", "当前用户没有权限部署该流程"),
    WF_DESIGN_02010003("02010003", "流程部署失败", "数据处理错误"),
    
    WF_DESIGN_02010101("02010101", " 流程模型删除失败", "流程模型删除失败,模型ID为空"),
    WF_DESIGN_02010102("02010102", " 流程删除失败", "流程删除失败,流程部署ID为空"),
    WF_DESIGN_02010103("02010103", " 流程删除失败", "流程删除失败,流程不能删除"),
    WF_DESIGN_02010104("02010104", " 流程导出失败", "流程导出失败,流程不能导出"),
    WF_DESIGN_02010105("02010105", " 流程导出失败", "流程导出失败,模型ID不能为空"),
    WF_DESIGN_02010106("02010106", "当前用户没有权限删除该流程", "当前用户没有权限删除该流程"),
    WF_DESIGN_02010107("02010107", " 流程删除失败", "流程删除失败,未找到要删除的流程"),
    WF_DESIGN_02010108("02010108", " 流程导出失败", "流程导出失败,当前用户没有权限导出该流程"),
    
    /** 流程挂起恢复处理异常  020102 */
    WF_TASK_02010201("02030101", "流程定义挂起失败", "流程定义挂起失败，流程定义ID不能为空！"),
    WF_TASK_02010202("02030102", "流程定义挂起失败", "流程定义挂起失败，,未找到要挂起的流程定义"),
    
    /** 流程挂起恢复处理异常  020102 */
    WF_TASK_02010210("02010210", "流程定义激活失败", "流程定义激活失败，流程定义ID不能为空！"),
    WF_TASK_02010211("02010211", "流程定义激活失败", "流程定义激活失败，,未找到要激活的流程定义"),
    
    /** 流程定义历史版本相关异常020103 */
    WF_DESIGN_02010301("02010301", "没有找到流程定义", "没有找到流程定义"),
    WF_DESIGN_02010302("02010302", "查询到多条流程定义", "查询到多条流程定义，不能使用单条结果查询接口"),
    WF_DESIGN_02010399("02010399", "查询流程定义历史版本失败", "查询流程定义历史版本失败"),
    
    /** 删除流程定义相关异常020110 */
    WF_DESIGN_02011001("02011001", "没有指定流程部署ID，不能删除流程定义", "没有指定流程部署ID，不能删除流程定义"),
    WF_DESIGN_02011002("02011002", "当前用户没有权限删除该流程定义", "当前用户没有权限删除该流程定义"),
    WF_DESIGN_02011099("02011099", "删除流程定义失败", "删除流程定义失败，请检查系统配置是否正确"),
    
    
    /** 流程任务相关返回码，编号以0202开头   **/
    /** 流程公共校验异常020200 */
    WF_TASK_02020000("02020000", "流程请求参数错误，不能进行相关操作", "流程请求参数错误，不能进行相关操作"),
    WF_TASK_02020001("02020001", "流程实例不存在，不能进行相关操作", "流程实例不存在，不能进行相关操作"),
    WF_TASK_02020002("02020002", "流程实例已结束，不能进行相关操作", "流程实例已结束，不能进行相关操作"),
    WF_TASK_02020003("02020003", "流程实例已暂停，不能进行相关操作", "流程实例已暂停，不能进行相关操作"),
//    WF_TASK_02020004("02020004", "未通过用户认证，不能进行相关操作", "未指定或不支持的用户认证方式，不能进行相关操作"),
//    WF_TASK_02020005("02020005", "未通过用户认证，不能进行相关操作", "用户Token认证未通过，不能进行相关操作"),
//    WF_TASK_02020006("02020006", "未通过用户认证，不能进行相关操作", "用户未登录，不能进行相关操作"),
//    WF_TASK_02020007("02020007", "未通过用户认证，不能进行相关操作", "不支持的用户认证方式，不能进行相关操作"),
    WF_TASK_02020008("02020008", "未指定审批页面，不能进行相关操作", "未指定审批页面，不能进行相关操作"),
    WF_TASK_02020009("02020009", "投票权重属性配置错误，不能进行相关操作", "投票权重属性配置错误，不能进行相关操作"),
    WF_TASK_02020010("02020010", "投票阈值属性配置错误，不能进行相关操作", "投票阈值属性配置错误，不能进行相关操作"),
    WF_TASK_02020011("02020011", "未找到流程定义信息，不能进行流程相关操作", "未找到流程定义信息，不能进行流程相关操作"),
    
    /** 流程启动相关参数校验异常020201 */
    WF_TASK_02020101("02020101", "没有指定流程定义编码，无法启动工作流", "没有指定流程定义编码，无法启动工作流"),
    WF_TASK_02020102("02020102", "没有指定流程定义ID，无法启动工作流", "没有指定流程定义ID，无法启动工作流"),
    WF_TASK_02020103("02020103", "没有指定关联业务，无法启动工作流", "没有指定关联业务ID，无法启动工作流"),
    WF_TASK_02020104("02020104", "没有指定业务所属机构，无法启动工作流", "没有指定业务所属机构，无法启动工作流"),
    WF_TASK_02020105("02020105", "没有找到流程定义，无法启动工作流", "没有找到流程定义，无法启动工作流"),
    WF_TASK_02020106("02020106", "流程定义已挂起，无法启动工作流", "流程定义已挂起，无法启动工作流"),
    WF_TASK_02020107("02020107", "当前用户没有权限启动该流程", "当前用户没有权限启动该流程"),
    WF_TASK_02020199("02020199", "启动工作流失败", "启动工作流失败，请检查系统配置是否正确"),
    
    /** 流程数据更新过程相关异常020202 */
    WF_TASK_02020201("02020201", "流程数据为空，流程处理失败", "流程数据为空，流程处理失败"),
    WF_TASK_02020202("02020202", "流程任务数据为空，流程处理失败", "流程任务数据为空，流程处理失败"),
    
    /** 流程任务签收相关异常020203 */
    WF_TASK_02020301("02020301", "未找到将要签收的流程任务", "没有指定流程任务ID，不能进行流程任务签收"),
    WF_TASK_02020302("02020302", "未找到将要签收的流程任务", "未找到将要签收的流程任务"),
    WF_TASK_02020303("02020303", "当前用户没有权限签收流程任务", "当前用户没有权限签收流程任务"),
    WF_TASK_02020304("02020304", "未找到将要签收的流程任务", "没有指定流程实例ID，不能进行流程任务签收"),
    WF_TASK_02020305("02020305", "未找到将要签收的流程任务", "没有指定流程任务代码，不能进行流程任务签收"),
    WF_TASK_02020306("02020306", "流程实例已暂停，无法进行流程任务签收", "流程实例已暂停，无法进行流程任务签收"),
    WF_TASK_02020307("02020307", "当前流程任务状态不能进行流程任务签收", "当前流程任务状态不能进行流程任务签收"),
    WF_TASK_02020399("02020399", "流程任务签收失败", "流程任务签收失败，请检查系统配置是否正确"),
    
    /** 流程任务取消签收相关异常020204 */
    WF_TASK_02020401("02020401", "未找到将要取消签收的流程任务", "未找到将要取消签收的流程任务"),
    WF_TASK_02020402("02020402", "未找到将要取消签收的流程任务", "没有指定流程任务ID，不能进行流程任务取消签收操作"),
    WF_TASK_02020403("02020403", "当前用户不是流程任务签收人", "当前用户不是流程任务签收人"),
    WF_TASK_02020404("02020404", "未找到将要取消签收的流程任务", "没有指定流程实例ID，不能进行流程任务取消签收操作"),
    WF_TASK_02020405("02020405", "未找到将要取消签收的流程任务", "没有指定流程任务代码，不能进行流程任务取消签收操作"),
    WF_TASK_02020406("02020406", "流程任务指定了处理人，不能进行流程任务取消签收操作", "流程任务指定了处理人，不能进行流程任务取消签收操作"),
    WF_TASK_02020407("02020407", "当前流程任务状态不能进行流程任务取消签收操作", "当前流程任务状态不能进行流程任务取消签收操作"),
    WF_TASK_02020499("02020499", "流程任务取消签收失败", "流程任务取消签收失败，请检查系统配置是否正确"),
    
    /** 流程任务审批相关异常020205 */
    WF_TASK_02020501("02020501", "未找到将要审批的流程任务", "未找到将要审批的流程任务"),
    WF_TASK_02020502("02020502", "没有流程任务ID，不能进行流程任务审批", "没有指定流程任务ID，不能进行流程任务审批"),
    WF_TASK_02020503("02020503", "未指定流程任务候选用户组，不能进行流程任务审批", "未指定流程任务候选用户组，不能进行流程任务审批"),
    WF_TASK_02020504("02020504", "没有流程任务审批结论，无法提交流程任务", "没有流程任务审批结论，无法提交流程任务"),
    WF_TASK_02020505("02020505", "未找到将要审批的流程任务", "没有指定流程实例ID，不能进行流程任务审批操作"),
    WF_TASK_02020506("02020506", "未找到将要审批的流程任务", "没有指定流程任务代码，不能进行流程任务审批操作"),
    WF_TASK_02020507("02020507", "当前用户没有权限审批流程任务", "当前用户没有权限审批流程任务"),
    WF_TASK_02020508("02020508", "当前用户不是流程任务签收人，不能进行流程任务审批", "当前用户不是流程任务签收人，不能进行流程任务审批"),
    WF_TASK_02020509("02020509", "未配置任务回退节点，不能进行流程任务回退处理", "未配置任务回退节点，不能进行流程任务回退处理"),
    WF_TASK_02020510("02020510", "流程任务审批未通过处理失败", "流程任务审批未通过处理失败"),
    WF_TASK_02020511("02020511", "起始任务节点不允许审批拒绝处理", "起始任务节点不允许审批拒绝处理"),
    WF_TASK_02020512("02020512", "流程配置不正确，不能进行流程任务审批", "没有配置或配置了多个目标任务，不能进行流程任务审批"),
    WF_TASK_02020513("02020513", "流程配置不正确，不能进行流程任务审批", "投票决策任务未汇聚，不能进行流程任务审批"),
    WF_TASK_02020514("02020514", "流程配置不正确，不能进行流程任务审批", "投票审批任务配置的投票阈值不一致，不能进行流程任务审批"),
    WF_TASK_02020515("02020515", "流程配置不正确，不能进行流程任务审批", "投票审批任务配置的投票规则不一致，不能进行流程任务审批"),
    WF_TASK_02020516("02020516", "流程配置不正确，不能进行流程任务审批", "投票总数小于投票阈值，不能进行流程任务审批"),
    WF_TASK_02020599("02020599", "流程任务审批失败", "流程任务审批失败，请检查系统配置是否正确"),
    
    /** 流程任务删除相关异常020206 */
    WF_TASK_02020601("02020601", "未找到将要删除的流程任务", "未找到将要删除的流程任务"),
    WF_TASK_02020602("02020602", "未找到将要删除的流程任务", "没有指定流程任务ID，无法删除流程任务"),
    WF_TASK_02020603("02020603", "只有起始任务才可以被删除，当前流程任务不是起始任务", "只有起始任务才可以被删除，当前流程任务不是起始任务"),
    WF_TASK_02020604("02020604", "只有流程发起人才可以删除流程任务，当前用户不是流程发起人", "只有流程发起人才可以删除流程任务，当前用户不是流程发起人"),
    WF_TASK_02020605("02020605", "存在其他未审批的流程任务，无法删除流程任务", "存在其他未审批的流程任务，无法删除流程任务"),
    WF_TASK_02020699("02020699", "流程任务删除失败", "流程任务删除失败，请检查系统配置是否正确"),
    
    /**查询流程审批历史*/
    WF_TASK_02020701("02020701", "查询流程审批历史失败", "查询流程审批历史失败，流程实例ID参数为空"),
    WF_TASK_02020799("02020799", "查询流程审批历史失败", "查询流程审批历史失败，请检查系统配置是否正确"),
    
    /**流程任务回调函数初始化*/
    WF_TASK_02020801("02020801", "流程回调函数初始化失败", "流程回调函数初始化失败，请检查流程"),
    WF_TASK_02020802("02020802", "流程前回调函数执行失败", "流程前回调函数执行失败，请检查流程回调类"),
    WF_TASK_02020803("02020803", "流程后回调函数执行失败", "流程后回调函数执行失败，请检查流程回调类"),
    WF_TASK_02020804("02020804", "流程回调类合法性检查错误", "流程回调类合法性检查错误，请使用系统支持的类定义"),
    
    /** 流程任务委托相关异常 020209 */
    WF_TASK_02020901("02020901", "流程任务不能委托给自己", "任务委托失败，流程任务不能委托给自己"),
    WF_TASK_02020902("02020902", "未找到将要委托的流程", "没有指定流程任务ID，不能进行流程委托操作"),
    WF_TASK_02020903("02020903", "未找到将要委托的流程", "未找到将要委托的流程，不能进行流程委托操作"),
    WF_TASK_02020904("02020904", "当前用户不是流程任务审批人，不能进行委托操作", "当前用户不是流程任务审批人，不能进行委托操作"),
    WF_TASK_02020905("02020905", "流程任务已被委托给当前用户，不能重复委托", "流程任务已被委托给当前用户，不能重复委托"),
    WF_TASK_02020999("02020999", "流程委托处理失败", "流程委托处理失败，请检查系统配置是否正确"),

    /** 流程任务取消相关异常020210 */
    WF_TASK_02021001("02021001", "未找到将要取消的流程任务", "未找到将要取消的流程任务"),
    WF_TASK_02021002("02021002", "未找到将要取消的流程任务", "没有指定流程实例，不能进行流程任务取消操作"),
    WF_TASK_02021003("02021003", "当前用户不是流程创建人，不能取消流程", "当前用户不是流程创建人，不能取消流程"),
    WF_TASK_02021004("02021004", "未找到要取消的流程任务或流程任务已经被签收或审批，不能取消流程", "未找到要取消的流程任务或流程任务已经被签收或审批，不能取消流程"),
    WF_TASK_02021005("02021005", "要取消的流程任务已经被签收或审批，不能取消流程", "要取消的流程任务已经被签收或审批，不能取消流程"),
    WF_TASK_02021006("02021006", "要取消的流程任务不是流程创建人提交，不能取消流程", "要取消的流程任务不是流程创建人提交，不能取消流程"),
    WF_TASK_02021007("02021007", "存在不可取消的流程任务，不能取消流程", "存在不可取消的流程任务，不能取消流程"),
    WF_TASK_02021099("02021099", "流程任务取消失败", "流程任务取消失败，请检查系统配置是否正确"),
    
    /** 流程任务撤回相关异常020211 */
    WF_TASK_02021101("02021101", "未找到将要撤回的流程任务或流程任务已经被签收或处理", "未找到将要撤回的流程任务或流程任务已经被签收或处理"),
    WF_TASK_02021102("02021102", "未找到将要撤回的流程任务", "没有指定流程任务ID，无法撤回流程任务"),
    WF_TASK_02021103("02021103", "当前用户不是流程任务审批人，不能进行撤回操作", "当前用户不是流程任务审批人，不能进行撤回操作"),
    WF_TASK_02021104("02021104", "投票类流程任务不允许撤回，不能进行撤回操作", "投票类流程任务不允许撤回，不能进行撤回操作"),
    WF_TASK_02021105("02021105", "流程任务已签收或审批，不能进行撤回操作", "流程任务已签收或审批，不能进行撤回操作"),
    WF_TASK_02021106("02021106", "流程任务被定义为不可撤回任务，不能进行撤回操作", "流程任务被定义为不可撤回任务，不能进行撤回操作"),
    WF_TASK_02021199("02021199", "流程任务撤回失败", "流程任务撤回失败，请检查系统配置是否正确"),
    
    /** 流程任务终止相关异常 020212 */
    WF_TASK_02021201("02021201", "没有指定流程实例，不能进行流程终止处理", "没有指定流程实例，不能进行流程终止处理"),
    WF_TASK_02021202("02021202", "未找到将要终止的流程任务，不能进行流程终止", "未找到将要终止的流程任务，不能进行流程终止"),
    WF_TASK_02021299("02021299", "流程任务终止失败，请检查系统配置是否正确", "流程任务终止失败，请检查系统配置是否正确"),
    
    /**流程实例暂停处理异常代码 020213   */
    WF_TASK_02021301("02021301", "没有指定流程实例ID，不能进行流程实例暂停处理", "没有指定流程实例ID，不能进行流程实例暂停处理"),
    WF_TASK_02021399("02021399", "流程实例暂停处理失败", "流程实例暂停处理失败，请检查系统配置是否正确"),
    
    /**流程实例激活处理异常代码 020214   */
    WF_TASK_02021401("02021401", "没有指定流程实例ID，不能进行流程实例激活处理", "没有指定流程实例ID，不能进行流程实例激活处理"),
    WF_TASK_02021402("02021402", "流程实例状态不是已暂停状态，不能进行流程实例激活处理", "流程实例状态不是已暂停状态，不能进行流程实例激活处理"),
    WF_TASK_02021499("02021499", "流程实例激活处理失败", "流程实例激活处理失败，请检查系统配置是否正确"),
    
    /** 指定流程任务处理人处理异常 020215 */
    WF_TASK_02021501("02021501", "没有指定流程任务ID，不能对流程任务指定处理人", "没有指定流程任务ID，不能对流程任务指定处理人"),
    WF_TASK_02021502("02021502", "没有指定流程任务处理人，不能对流程任务指定处理人", "没有指定流程任务处理人，不能对流程任务指定处理人"),
    WF_TASK_02021599("02021599", "流程任务指定处理人处理失败", "流程任务指定处理人处理失败，请检查系统配置是否正确"),
    
    /** 指定流程任务处理人处理异常 020216 */
    WF_TASK_02021601("02021601", "没有指定流程实例ID，不能进行取消流程委托操作", "没有指定流程实例ID，不能进行取消流程委托操作"),
    WF_TASK_02021602("02021602", "受托人有待处理的流程任务，不能取消流程委托", "受托人有待处理的流程任务，不能取消流程委托");
    
    /** 响应代码 **/
    private String retCode = "";
    
    /** 提示信息-用户提示信息 **/
    private String retUserInfo = "";
    
    /** 响应码含义-实际响应信息 **/
    private String retFactInfo = "";
        
    /**
     * 
     * @param retCode 响应代码
     * @param retFactInfo 响应码含义-实际响应信息 
     * @param retUserInfo  提示信息-用户提示信息
     */
    private WorkflowEnumResults(String retCode, String retFactInfo, String retUserInfo) {
        this.retCode = retCode;
        this.retFactInfo = retFactInfo;
        this.retUserInfo = retUserInfo;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetFactInfo() {
        return retFactInfo;
    }

    public void setRetFactInfo(String retFactInfo) {
        this.retFactInfo = retFactInfo;
    }

    public String getRetUserInfo() {
        return retUserInfo;
    }

    public void setRetUserInfo(String retUserInfo) {
        this.retUserInfo = retUserInfo;
    }
    
    /**
     * 通过响应代码 获取对应的ReturnInfo
     * @param retCode-返回码
     * @return 响应枚举类型
     */
    public EnumResultTemplate getReturnCodeInfoByCode(EnumResultTemplate returnInfo) {
        if (map.get(returnInfo.getRetCode()) != null) {
            return map.get(returnInfo.getRetCode());
        } else {
            return BaseEnumResults.BASE00000099;
        }
    }
    
    /**
     * 重写toString
     */
    public String toString() {
        return new StringBuffer("{retCode:").append(retCode)
                .append(";retFactInfo(实际响应信息):").append(retFactInfo)
                .append(";retUserInfo(客户提示信息):").append(retUserInfo).append("}").toString();
    }

    /**存放全部枚举的缓存对象*/
    private static Map<String,WorkflowEnumResults> map = new HashMap<String,WorkflowEnumResults>();
    
    /**将所有枚举缓存*/
    static{
        EnumSet<WorkflowEnumResults> currEnumSet = EnumSet.allOf(WorkflowEnumResults.class);
        
        for (WorkflowEnumResults retCodeType : currEnumSet) {
            map.put(retCodeType.getRetCode(), retCodeType);
        }
    }
}
