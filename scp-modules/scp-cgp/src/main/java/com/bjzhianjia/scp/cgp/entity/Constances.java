package com.bjzhianjia.scp.cgp.entity;

/**
 * 常量类
 * @author zzh
 *
 */
public class Constances {

	public static final String ROOT_BIZ_ENABLED = "ROOT_BIZ_ENABLED";
	
	public static final String ROOT_BIZ_TRML_T = "ROOT_BIZ_TRML_T";
	
	public static final String ROOT_BIZ_VHCL_T = "ROOT_BIZ_VHCL_T";
	
	/**
	 * @author By尚
	 * 企业类型
	 */
	public static final String ROOT_BIZ_ENTTYPE = "root_biz_entType";
	
	/**
	 * 业务线条
	 */
	public static final String ROOT_BIZ_TYPE = "root_biz_type";
	
	/**
	 * 证件类型
	 */
	public static final String ROOT_BIZ_CERT_T = "root_biz_credType";
//	public static final String ROOT_BIZ_CERT_T = "root_biz_cert_t";
	
	/**
	 * 监管对象类别
	 */
	public static final String ROOT_BIZ_OBJTYPE="root_biz_objType";
	
	/**
	 * 网格管理角色
	 */
	public static final String ROOT_BIZ_GRID_ROLE="root_biz_grid_role";
	
	/**
	 * 热线处理状态
	 */
	public static final String ROOT_BIZ_12345STATE="root_biz_12345state";
	
	/**
	 * 舆情处理状态
	 */
	public static final String ROOT_BIZ_CONSTATE="root_biz_conState";
	/**
	 * 领导交办处理状态
	 */
	public static final String ROOT_BIZ_LDSTATE="root_biz_ldState";
	
	/**
	 * 舆情来源类型
	 */
	public static final String ROOT_BIZ_CONSOURCET="root_biz_conSourceT";
	
	/**
	 * 舆情等级
	 */
	public static final String ROOT_BIZ_CONLEVEL="root_biz_conLevel";
	
	/**
	 * 专项管理处理状态
	 * @author 尚
	 *
	 */
	public static class SpecialEventStatus{
		/**
		 * 未启动
		 */
		public static final String NO_RUN="0";
		/**
		 * 进行中
		 */
		public static final String ON_RUN="1";
		/**
		 * 已终止
		 */
		public static final String TERMINAL="2";
		/**
		 * 已完成
		 */
		public static final String DONE="3";
	}
	
	/**
	 * 事件来源类型前缀
	 */
	public static final String ROOT_BIZ_EVENTTYPE="root_biz_eventType";
	
	/**
	 * 事件来源类型常量包装类(12345,舆情，领导交办，巡查上报)
	 * @author 尚
	 *
	 */
	public static class BizEventType{
		/**
		 * 12345
		 */
		public static final String ROOT_BIZ_EVENTTYPE_12345="root_biz_eventType_12345";
		/**
		 * 巡查上报
		 */
		public static final String ROOT_BIZ_EVENTTYPE_CHECK="root_biz_eventType_check";
		/**
		 * 领导交办
		 */
		public static final String ROOT_BIZ_EVENTTYPE_LEADER="root_biz_eventType_leader";
		/**
		 * 舆情
		 */
		public static final String ROOT_BIZ_EVENTTYPE_CONSENSUS="root_biz_eventType_consensus";
	}
	
	/**
	 * 热线处理状态
	 * @author 尚
	 *
	 */
	public static class MayorHotlineExeStatus{
		/**
		 * 未发起
		 */
		public static final String ROOT_BIZ_12345STATE_TODO="root_biz_12345state_todo";
		
		/**
		 * 处理中
		 */
		public static final String ROOT_BIZ_12345STATE_DOING="root_biz_12345state_doing";
		/**
		 * 已终止
		 */
		public static final String ROOT_BIZ_12345STATE_STOP="root_biz_12345state_stop";
		/**
		 * 已反馈
		 */
		public static final String ROOT_BIZ_12345STATE_FEEDBACK="root_biz_12345state_feedback";
		/**
		 * 已处理
		 */
		public static final String ROOT_BIZ_12345STATE_DONE="root_biz_12345state_done";
		
	}
	/**
	 * 舆情处理状态
	 * @author 尚
	 *
	 */
	public static class PublicOpinionExeStatus{
		/**
		 * 未发起
		 */
		public static final String ROOT_BIZ_CONSTATE_TODO="root_biz_conState_todo";
		
		/**
		 * 处理中
		 */
		public static final String ROOT_BIZ_CONSTATE_DOING="root_biz_conState_doing";
		/**
		 * 已终止
		 */
		public static final String ROOT_BIZ_CONSTATE_STOP="root_biz_conState_stop";
		/**
		 * 已完成
		 */
		public static final String ROOT_BIZ_CONSTATE_FINISH="root_biz_conState_finish";
	}
	/**
	 * 领导交办处理状态
	 * @author 尚
	 *
	 */
	public static class LeaderAssignExeStatus{
		/**
		 * 未发起
		 */
		public static final String ROOT_BIZ_LDSTATE_TODO="root_biz_ldState_todo";
		
		/**
		 * 处理中
		 */
		public static final String ROOT_BIZ_LDSTATE_DOING="root_biz_ldState_doing";
		/**
		 * 已终止
		 */
		public static final String ROOT_BIZ_LDSTATE_STOP="root_biz_ldState_stop";
		/**
		 * 已完成
		 */
		public static final String ROOT_BIZ_LDSTATE_FINISH="root_biz_ldState_finish";
	}
	
	/**
	 *   巡查任务状态
	 * @author chenshuai
	 *
	 */
	public static class PartolTaskStatus{
		
		/**
		 *  巡查任务状态
		 */
		public static final String ROOT_BIZ_PARTOLTASKT = "root_biz_partoltaskt";
		/**
		 *  处理中
		 */
		public static final String ROOT_BIZ_PARTOLTASKT_DOING = "root_biz_partoltaskt_doing";
		
		
		/**
		 *   已终止
		 */
		public static final String ROOT_BIZ_PARTOLTASKT_STOP = "root_biz_partolTaskT_stop";
		
		
		/**
		 *   已完成
		 */
		public static final String ROOT_BIZ_PARTOLTASKT_FINISH = "root_biz_partolTaskT_finish";
		
		
		/**
		 *   日常巡查
		 */
		public static final String ROOT_BIZ_PATROLTYPE_DAILY = "root_biz_patrolType_daily";
		
		/**
		 *   专项巡查
		 */
		public static final String ROOT_BIZ_PATROLTYPE_SPECIAL = "root_biz_patrolType_special";
		
	}
	
	/**
	 *  当事人相关状态
	 * @author chenshuai
	 *
	 */
	public static class ConcernedStatus{
		
		/**
		 *  当事人：个人
		 */
		public static final String ROOT_BIZ_CONCERNEDT_PERSON = "root_biz_concernedT_person";
		
		/**
		 *  当事人：单位
		 */
		public static final String ROOT_BIZ_CONCERNEDT_ORG = "root_biz_concernedT_org";
	}
}
