package com.bjzhianjia.scp.cgp.constances;
/**
 * 存放工作流相关常量属性<br/>
 * 在此之前有关工作流常量属性放在com.bjzhianjia.scp.cgp.entity.Constances不进行修改
 * @author 尚
 */
public class WorkFlowConstances {
	/**
	 * 节点后缀名
	 * @author 尚
	 *
	 */
	public static class ProcessNodeSuffix{
		/**
		 * 与中队领导相关的结节ID后缀
		 */
		public static final String SQUADRONLEADER_SUFFIX="SquadronLeader";
		/**
		 * 与镇局领导相关的节点ID后缀
		 */
		public static final String TOWNLEADER_SUFFIX="TownLeader";
		/**
		 * 与法制科相关的节点ID后缀
		 */
		public static final String LEGAL_SUFFIX="Legal";
		/**
		 * 与执法队员相关的节点ID后缀
		 */
		public static final String LAWMEMBER_SUFFIX="LawMember";
	}
	
	/**
	 * 未在工作流中进行配置的文书模板<br/>
	 * 该常量类用于指导某一个不在工作流中的节点使用什么样的文书模板
	 * @author 尚
	 *
	 */
	@Deprecated
	public static class WritsTemplateIdsInNode{
		/**
		 * 现场检查
		 */
		public static final String SPOT_CHECK="spotCheck";
		/**
		 * 现场检查
		 */
		public static final String SPOT_CHECK_IDS="2,11,12,13";
		/**
		 * 现场处罚
		 */
		public static final String SPOT_PUNISHMENT="spotPunishment";
		/**
		 * 现场处罚
		 */
		public static final String SPOT_PUNISHMENT_IDS="14";
		/**
		 * 责令改正处理
		 */
		public static final String RECTIFICATION="rectification";
		/**
		 * 责令改正处理
		 */
		public static final String RECTIFICATION_IDS="3,15,16";
		/**
		 * 责令改正处理
		 */
		public static final String INFORM="inform";
		/**
		 * 责令改正处理
		 */
		public static final String INFORM_IDS="5,22";
	}
}
