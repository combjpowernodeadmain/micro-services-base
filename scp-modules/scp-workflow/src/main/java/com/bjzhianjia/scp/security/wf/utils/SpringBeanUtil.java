package com.bjzhianjia.scp.security.wf.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringBean工具类
 * @author baiyanbing
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware{
	private static ApplicationContext ctx;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}
	/**
	 * 通过bean名称获取bean实例
	 * @param id
	 * @return
	 */
	public static Object getBean(String id){
		try{
			return ctx.getBean(id);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("SpringBeanUtil 初始化失败");
			return null;
		}
	}
	
	/**
	 * 通过类名获取bean实例
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz){
		try{
			return ctx.getBean(clazz);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("SpringBeanUtil 初始化失败");
			return null;
		}
	}
}
