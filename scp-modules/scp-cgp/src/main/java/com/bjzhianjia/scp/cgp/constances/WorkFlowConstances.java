package com.bjzhianjia.scp.cgp.constances;
/**
 * 存放工作流相关常量属性<br/>
 * 在此之前有关工作流常量属性放在com.bjzhianjia.scp.cgp.entity.Constances不进行修改
 * @author 尚
 */
public class WorkFlowConstances {

	public static class ProcessNode{
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
}
