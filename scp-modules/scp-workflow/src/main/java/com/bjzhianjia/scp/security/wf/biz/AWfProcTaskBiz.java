package com.bjzhianjia.scp.security.wf.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Task;

import com.bjzhianjia.scp.security.common.util.DateTools;
import com.bjzhianjia.scp.security.wf.base.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.constant.Attr.DictKeyConst;
import com.bjzhianjia.scp.security.wf.constant.Constants.FlowStatus;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfDataValid;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcDataPermissionType;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcParallStatus;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcTaskProperty;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcVotePower;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcVoteRole;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.entity.WfProcBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskPropertiesBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskPropertyBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.service.IWfProcTaskCallBackBaseService;
import com.bjzhianjia.scp.security.wf.utils.JSONUtil;
import com.bjzhianjia.scp.security.wf.utils.SpringBeanUtil;
import com.bjzhianjia.scp.security.wf.utils.StringUtil;
import com.bjzhianjia.scp.security.wf.vo.WfProcAuthDataBean;
import com.bjzhianjia.scp.security.wf.vo.WfProcVariableDataBean;

public abstract class AWfProcTaskBiz extends WfBaseBiz {
	private static List<String> WFPROCTASKRESERVEDPROPERTIES = new ArrayList<String>();
	
	static {
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_URL);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_DISPLAYURL);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_DETAILURL);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_REFUSETASK);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_CALLBACK);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_DATAPERMISSION);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_RETRIEVE);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_VOTETASK);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_VOTEPOWER);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_VOTERULE);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_VOTEWEIGHT);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_VOTETHRESHOLD);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_VOTEQUICKLY);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_ISNOTIFY);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_NOTIFYKEY);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_NOTIFYUSERSBYSMS);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_NOTIFYUSERSBYMAIL);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_NOTIFYUSERSBYINNMSG);
		WFPROCTASKRESERVEDPROPERTIES.add(WfProcTaskProperty.PROC_NOTIFYUSERSBYWECHAT);
	}
	
    /**
     * 创建业务流程数据
     * 
     * @param procVarData
     *            流程参数
     * @param procInstId
     *            流程实例ID
     * @param procDef
     *            流程定义数据
     * @return
     */
	protected WfProcBean creatProcessData(
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			String procInstId, ProcessDefinitionEntity procDef) {
		WfProcBean wfProcBean = new WfProcBean();
		wfProcBean.setProcInstId(procInstId); // 流程实例id
		wfProcBean.setProcId(procDef.getId()); // 流程定义ID
		wfProcBean.setProcKey(procDef.getKey());// 流程定义编号
		wfProcBean.setProcName(procDef.getName()); // 流程定义名称
		wfProcBean.setProcVersion(procDef.getVersion()); // 流程版本号
		wfProcBean.setProcCreator(authData.getProcTaskUser());
		wfProcBean.setProcCreatetime(DateTools.getCurrTime());
		wfProcBean.setProcStatus(FlowStatus.PROC10.getRetCode());
		
		return wfProcBean;
    }

    /**
     * 创建流程启动节点流程任务数据
     * 
     * @param wfProcBean
     * @param task
     * @param properties
     * @return
     */
    protected WfProcTaskBean createProcStartTaskData(WfProcBean wfProcBean, Task task,
        WfProcTaskPropertiesBean properties) throws WorkflowException {
        WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
        wfProcTaskBean.setProcInstId(wfProcBean.getProcInstId()); // 流程实例ID
        wfProcTaskBean.setProcId(wfProcBean.getProcId());// 流程定义ID
        wfProcTaskBean.setProcKey(wfProcBean.getProcKey());// 流程定义代码
        wfProcTaskBean.setProcTaskCommitter(wfProcBean.getProcCreator()); // 流程任务提交人
        wfProcTaskBean.setProcTaskCommittime(wfProcBean.getProcCreatetime()); // 流程任务提交时间
        wfProcTaskBean.setProcCtaskid(task.getId()); // 当前流程任务ID
        wfProcTaskBean.setProcCtaskcode(task.getTaskDefinitionKey()); // 当前流程任务代码
        wfProcTaskBean.setProcCtaskname(task.getName());// 当前流程任务名称
        wfProcTaskBean.setProcExecutionid(task.getExecutionId());
        wfProcTaskBean.setProcAppointUsers(wfProcBean.getProcCreator()); // 流程第一个任务指定处理人默认为申请人
        wfProcTaskBean.setProcTaskAssignee(wfProcBean.getProcCreator()); // 流程第一个任务指定签收人默认为指定处理人
        wfProcTaskBean.setProcTaskAssigntime(wfProcBean.getProcCreatetime()); // 流程任务签收时间
        wfProcTaskBean.setProcRefusetask(DictKeyConst.YESORNO_NO);
        wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK02.getRetCode()); // 启动流程不做提交操作，流程任务状态默认为待处理

        // 设置流程任务属性数据
        String taskProperties = JSONUtil.objToJson(properties);
        wfProcTaskBean.setProcTaskProperties(taskProperties);

        // 设置流程任务数据权限类型
        wfProcTaskBean.setProcDatapermission(getProcTaskDataPermission(properties));
        wfProcTaskBean.setProcParallel(DictKeyConst.YESORNO_NO);
        wfProcTaskBean.setProcParallelStatus(WfProcParallStatus.APPROVED.getRetCode());
        // 设置流程任务参与决策标识
        wfProcTaskBean.setProcVotetask(getProcVoteTask(properties));
        // 设置流程任务特殊决策权
        wfProcTaskBean.setProcVotepower(getProcVotePower(properties));
        // 设置流程任务参与投票规则
        wfProcTaskBean.setProcVoterule(getProcVoteRule(properties));
        // 设置流程任务投票权重
        wfProcTaskBean.setProcVoteweight(getProcVoteWeight(properties));
        // 设置流程任务投票阈值
        wfProcTaskBean.setProcVotethreshold(getProcVoteThreshold(properties));
        // 设置流程任务速决标识
        wfProcTaskBean.setProcVotequickly(getProcVoteQuickly(properties));
        // 设置流程任务审批页面URL
        wfProcTaskBean.setProcApproveurl(getProcTaskUrl(properties));

        return wfProcTaskBean;
    }

    /**
     * 获取流程任务节点定义的业务回调类
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcTaskCallback(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_CALLBACK)) {
            return properties.getValue(WfProcTaskProperty.PROC_CALLBACK);
        }
        
        return null;
    }

    /**
     * 获取流程任务节点定义的数据权限类型，如果没有定义则默认返回不进行数据权限控制
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcTaskDataPermission(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_DATAPERMISSION)) {
            return properties.getValue(WfProcTaskProperty.PROC_DATAPERMISSION);
        } else {
            return WfProcDataPermissionType.SELF.getRetCode();
        }
    }

    /**
     * 获取流程任务节点定义的任务审批页面URL
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcTaskUrl(WfProcTaskPropertiesBean properties) throws WorkflowException {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_URL)) {
            return properties.getValue(WfProcTaskProperty.PROC_URL);
        } else {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020008);
        }
    }

    /**
     * 获取流程任务节点定义的任务查看页面URL
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcDisplayUrl(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_DISPLAYURL)) {
            return properties.getValue(WfProcTaskProperty.PROC_DISPLAYURL);
        }

        return null;
    }

    /**
     * 获取流程任务节点定义的流程详情页面URL
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcDetailUrl(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_DETAILURL)) {
            return properties.getValue(WfProcTaskProperty.PROC_DETAILURL);
        }

        return null;
    }
    
    /**
     * 获取流程任务节点定义的拒绝回退任务
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcRefuseTask(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_REFUSETASK)) {
            return properties.getValue(WfProcTaskProperty.PROC_REFUSETASK);
        }

        return null;
    }

    /**
     * 获取流程任务节点定义的可撤回标识，如果没有配置默认为允许撤回
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcRetrieve(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_RETRIEVE)) {
            return properties.getValue(WfProcTaskProperty.PROC_RETRIEVE);
        } else {
        	return DictKeyConst.YESORNO_YES;
        }
    }
    
    /**
     * 获取流程任务节点定义的参与决策标识
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcVoteTask(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_VOTETASK)) {
            return properties.getValue(WfProcTaskProperty.PROC_VOTETASK);
        } else {
            return DictKeyConst.YESORNO_NO;
        }
    }

    /**
     * 获取流程任务节点定义的特殊决策权
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcVotePower(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_VOTEPOWER)) {
            return properties.getValue(WfProcTaskProperty.PROC_VOTEPOWER);
        } else {
            return WfProcVotePower.NONE.getRetCode();
        }
    }

    /**
     * 获取流程任务节点定义的投票规则（决策规则）
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcVoteRule(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_VOTERULE)) {
            return properties.getValue(WfProcTaskProperty.PROC_VOTERULE);
        } else {
            return WfProcVoteRole.PERCENTAGE.getRetCode();
        }
    }

    /**
     * 获取流程任务节点定义的投票权重（决策权重）
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected float getProcVoteWeight(WfProcTaskPropertiesBean properties) throws WorkflowException {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_VOTEWEIGHT)) {
            try {
                return Float.parseFloat(properties.getValue(WfProcTaskProperty.PROC_VOTEWEIGHT));
            } catch (Exception e) {
                throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020009, e);
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取流程任务节点定义的投票阈值（决策阈值）
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected float getProcVoteThreshold(WfProcTaskPropertiesBean properties) throws WorkflowException {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_VOTETHRESHOLD)) {
            try {
                return Float.parseFloat(properties.getValue(WfProcTaskProperty.PROC_VOTETHRESHOLD));
            } catch (Exception e) {
                throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020010, e);
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取流程任务节点定义的速决标识
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcVoteQuickly(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_VOTEQUICKLY)) {
            return properties.getValue(WfProcTaskProperty.PROC_VOTEQUICKLY);
        } else {
            return WfDataValid.PROC_DATA_INVALID;
        }
    }

    /**
     * 获取流程任务节点定义的消息发送标识
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcNotify(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_ISNOTIFY)) {
            return properties.getValue(WfProcTaskProperty.PROC_ISNOTIFY);
        } else {
        	return DictKeyConst.YESORNO_NO;
        }
    }
    
    /**
     * 获取流程任务节点定义的流程消息代码
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcNotifyKey(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_NOTIFYKEY)) {
            return properties.getValue(WfProcTaskProperty.PROC_NOTIFYKEY);
        } else {
            return WfDataValid.PROC_DATA_INVALID;
        }
    }
    
    /**
     * 获取流程任务节点定义的指定短信接收人
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcNotifyUsersBySms(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_NOTIFYUSERSBYSMS)) {
            return properties.getValue(WfProcTaskProperty.PROC_NOTIFYUSERSBYSMS);
        } else {
            return WfDataValid.PROC_DATA_INVALID;
        }
    }
    
    /**
     * 获取流程任务节点定义的指定邮件接收人
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcNotifyUsersByMail(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_NOTIFYUSERSBYMAIL)) {
            return properties.getValue(WfProcTaskProperty.PROC_NOTIFYUSERSBYMAIL);
        } else {
            return WfDataValid.PROC_DATA_INVALID;
        }
    }
    
    /**
     * 获取流程任务节点定义的指定内部消息接收人
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcNotifyUsersByInnMsg(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_NOTIFYUSERSBYINNMSG)) {
            return properties.getValue(WfProcTaskProperty.PROC_NOTIFYUSERSBYINNMSG);
        } else {
            return WfDataValid.PROC_DATA_INVALID;
        }
    }
    
    /**
     * 获取流程任务节点定义的指定微信接收人
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected String getProcNotifyUsersByWechat(WfProcTaskPropertiesBean properties) {
        if (properties != null && properties.containsKey(WfProcTaskProperty.PROC_NOTIFYUSERSBYWECHAT)) {
            return properties.getValue(WfProcTaskProperty.PROC_NOTIFYUSERSBYWECHAT);
        } else {
            return WfDataValid.PROC_DATA_INVALID;
        }
    }
    
    /**
     * 获取流程任务节点自定义属性
     * 
     * @param properties
     *            流程任务属性
     * @return
     */
    protected Map<String, String> getProcTaskSelfProperties(WfProcTaskPropertiesBean properties) {
    	Map<String, String> selfProperties = new HashMap<String, String>();
        if (properties != null && properties.size() > 0) {
        	List<WfProcTaskPropertyBean> props = properties.getTaskProperties();
        	
        	for (WfProcTaskPropertyBean prop : props) {
        		if (WFPROCTASKRESERVEDPROPERTIES.contains(prop.getId())) {
        			continue;
        		} else {
        			selfProperties.put(prop.getId(), prop.getValue());
        		}
        	}
        }
        
        return selfProperties;
    }
    
    /**
     * 根据类名称实例化对象
     * 
     * @param className
     *            类名称
     * @return
     * @throws WorkFlowException
     */
    @SuppressWarnings("unchecked")
    protected IWfProcTaskCallBackBaseService createProcTaskCallBackService(String className)
        throws WorkflowException {
        try {
            if (!StringUtil.isNull(className)) {
                Class<IWfProcTaskCallBackBaseService> clz =
                    (Class<IWfProcTaskCallBackBaseService>) Class.forName(className);
                
                return SpringBeanUtil.getBean(clz);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020801, e);
        }
    }
    
    /**
     * 得到流程任务状态列表
     * @param type  流程任务获取类型
     *          1：可签收流程任务状态列表
     *          2：可取消签收流程任务状态列表
     *          3：可审批流程任务状态列表
     *          4：已处理的流程任务状态列表
     *          5：已结束的流程任务状态列表
     *          6：非终止的流程任务状态列表
     *          99：全部流程任务状态列表
     * @return
     */
    protected List<String> getProcTaskStatus(int type) {
        List<String> taskStatus = new ArrayList<String>();
        
        switch (type) {
            case 1: // 可签收流程任务状态列表
                taskStatus.add(FlowStatus.TASK01.getRetCode());
                break;
            case 2: // 可取消签收流程任务状态列表
                taskStatus.add(FlowStatus.TASK02.getRetCode());
                break;
            case 3: // 可审批流程任务状态列表
                taskStatus.add(FlowStatus.TASK01.getRetCode());
                taskStatus.add(FlowStatus.TASK02.getRetCode());
                break;
            case 4: // 已处理的流程任务状态列表
                taskStatus.add(FlowStatus.TASK03.getRetCode());
                break;
            case 5: // 已结束的流程任务状态列表
                taskStatus.add(FlowStatus.TASK03.getRetCode());
                taskStatus.add(FlowStatus.TASK04.getRetCode());
                break;
            case 6: // 非终止的流程任务状态列表
                taskStatus.add(FlowStatus.TASK01.getRetCode());
                taskStatus.add(FlowStatus.TASK02.getRetCode());
                taskStatus.add(FlowStatus.TASK03.getRetCode());
                break;
            case 99: // 全部流程任务状态列表
                taskStatus.add(FlowStatus.TASK01.getRetCode());
                taskStatus.add(FlowStatus.TASK02.getRetCode());
                taskStatus.add(FlowStatus.TASK03.getRetCode());
                taskStatus.add(FlowStatus.TASK04.getRetCode());
                break;
            default:
                break;
        }
        
        return taskStatus;
    }
}
