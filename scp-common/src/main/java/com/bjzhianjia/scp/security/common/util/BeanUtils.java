package com.bjzhianjia.scp.security.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 获取Bean实体工具类
 *
 * @author scp
 * @create 2017-12-26 20:16
 **/
@Component
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(null == BeanUtils.applicationContext) {
            BeanUtils.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取Bean
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * =判断obj对象是否为空<br/>
     * =字符串：null或空字符串返回true，空格字符串返回false<br/>
     * =java对象：null返回true<br/>
     * =list集合：null或空集合返回true<br/>
     * =map集合：null或空集合返回true<br/>
     *
     * @param obj
     *            = 待判断的对象
     * @return 为空则返回true
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        if (obj instanceof Collection) return ((Collection<?>) obj).isEmpty();
        if (obj instanceof Map) return ((Map<?, ?>) obj).isEmpty();
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * =判断obj对象是否为空<br/>
     * =字符串：null或空字符串返回false，空格字符串返回true<br/>
     * =java对象：null返回false<br/>
     * =list集合：null或空集合返回false<br/>
     * =map集合：null或空集合返回false<br/>
     *
     * @param obj
     *            = 待判断的对象
     * @return 为空则返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
