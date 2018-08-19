package com.bjzhianjia.scp.security.wf.base.constant;

/**
 * 
 * Description: 常量类
 * @author scp
 * @version 1.0
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年8月15日    scp       1.0        1.0 Version 
 * </pre>
 */
public class Attr {

	public class RequestAttr{
		/**登录提示消息*/
		public static final String LoginMsg = "LoginMsg";
		
	}
	
	public class SessionAttr{
		/**登录图形验证码*/
		public static final String LoginCaptcha = "LoginCaptcha";
		
		public static final String LoginUserId="LoginUserId";
		
		/**后台登录用户*/
		public static final String LoginUser="LoginUser";
		/** 后台登录用户有效时长  **/
		public static final String LoginUserMaxInactiveInterval="1800";
		/** 未登录返回给前台信息  **/
		public static final String UnLoginReturn="unlogin";
	}
	
	public class StatusAttr{
	    /** 用户状态冻结  **/
	    public static final String userStatusFrezz = "2";
	}
	
	public class RedisIdeAttr{
	    /** 用户登录菜单redis缓存key标识  **/
	    public static final String SysMenu = "sys_menu_";
	    /** 用户登录菜单redis缓存时间  **/
	    public static final String SysMenuContinue = "1800";
	    /** 用户日志记录的系统菜单redis中的key值  **/
	    public static final String SysMenuForLog = "sys_menu_for_log";
	    
	}
	
	/** mongo相关配置 **/
	public class MongoCollections{
	    /** 系统用户登录  **/
	    public static final String SysUserLogin = "log_sys_login";
	    /** 系统用户操作 **/
	    public static final String SysUserOper = "log_sys_oper";
	    /** 系统日志  **/
	    public static final String SysLog = "1";
	    /** 业务日志  **/
	    public static final String BizLog = "0";
	}
	
	/** 用户相关  **/
	public class UserRelevant{
	    /** 默认密码  **/
	    public static final String DefaultPassword = "Iqb.com";
	    /** hq总部  **/
	    public static final String HQ = "1";
	}
	
	/** 公用常量  **/
	public class CommonConst{
	    /** 版本号  **/
	    public static final String Version = "1";
	}

	/** redis锁相关常量  **/
	public class RedisLockConst{
	    /** 密码输入错误允许次数  **/
	    public static final String LoginFailPermTimes = "5";
	    /** 密码输入错误用户锁前缀  **/
	    public static final String LoginFailLockPrex = "lock_etep_login_fail_";
	    /** 密码输入错误，锁定的时间  **/
	    public static final String LoginFailLockInterval = "1800";
	}
	
	/** 邮件相关常量  **/
	public class MailConst{
	    /** 邮件host地址  **/
	    public static final String MailHost = "smtp.exmail.qq.com";
	    /** 用户名  **/
	    public static final String MailUserName = "scp@bjzhianjia.com";
	    /** 密码  **/
	    public static final String MailPwd = "******";
	    /** 发件人  **/
	    public static final String MailPersonal = "Bjzhianjia";
	}
	
	/** 字key典常量  **/
	public class DictKeyConst{
	    /** 字典常量key：是  **/
	    public static final String YESORNO_YES = "1";
	    /** 字典常量key：否  **/
	    public static final String YESORNO_NO = "0";
	}
	
}
