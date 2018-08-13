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
	 * 事件来源类型(12345,舆情，领导交办，巡查上报)
	 * @author 尚
	 *
	 */
	public static class BizEventType{
		/**
		 * 12345
		 */
		public static final String ROOT_BIZ_EVENTTYPE_12345="586c6ce8a6034d998793f0282697f0e1";
		/**
		 * 巡查上报
		 */
		public static final String ROOT_BIZ_EVENTTYPE_CHECK="1cc6e1ad298e4e56b3744c741645ff4f";
		/**
		 * 领导交办
		 */
		public static final String ROOT_BIZ_EVENTTYPE_LEADER="70d3be12fb244630965624e965e4dddd";
		/**
		 * 舆情
		 */
		public static final String ROOT_BIZ_EVENTTYPE_CONSENSUS="ea584f4088024e1ba62dea13d9632068";
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
		public static final String ROOT_BIZ_12345STATE_TODO="9d22db93f3fe47e2bc745eaa375b4c91";
		
		/**
		 * 处理中
		 */
		public static final String ROOT_BIZ_12345STATE_DOING="48df69cd7d08411b9fba0c6b692cd21c";
		/**
		 * 已终止
		 */
		public static final String ROOT_BIZ_12345STATE_STOP="0f4c42611fdc4758b85abdcfd6a2fd3c";
		/**
		 * 已反馈
		 */
		public static final String ROOT_BIZ_12345STATE_FEEDBACK="be9bad2901c14b719feb039ef269a188";
		/**
		 * 已处理
		 */
		public static final String ROOT_BIZ_12345STATE_DONE="e48f8d4f22794f7f828addbca8d6bfd3";
		
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
		public static final String ROOT_BIZ_CONSTATE_TODO="a1dccb296d5c4af08a98ea17fde4a4e1";
		
		/**
		 * 处理中
		 */
		public static final String ROOT_BIZ_CONSTATE_DOING="7911df65493e4fd491a3d4cb8098aab2";
		/**
		 * 已终止
		 */
		public static final String ROOT_BIZ_CONSTATE_STOP="ac1a55785fe04afc8da3856c42fa611f";
		/**
		 * 已完成
		 */
		public static final String ROOT_BIZ_CONSTATE_FINISH="9bee20c238094baebee1490d2bc392a9";
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
		public static final String ROOT_BIZ_LDSTATE_TODO="4e202c44eca042e6923ba241a330723d";
		
		/**
		 * 处理中
		 */
		public static final String ROOT_BIZ_LDSTATE_DOING="f8f70463793140928bfa55772a171343";
		/**
		 * 已终止
		 */
		public static final String ROOT_BIZ_LDSTATE_STOP="61aaf9d63fc24ea394e922f7d55243fb";
		/**
		 * 已完成
		 */
		public static final String ROOT_BIZ_LDSTATE_FINISH="f081aca084674577b6d6ab3596a7d86e";
	}
}
