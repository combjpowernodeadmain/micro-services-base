package com.bjzhianjia.scp.cgp.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * PropertiesProxy 类描述.
 * 该类将instance对象中，将指定的属性封装进JSONObject对象中
 * 可用于从数据库中查出对象后 ，选择性向前端返回属性，可在该类中进行处理
 * 
 * 如：User类中包含属性"id","name","password","addr","sex"<br/>
 * 在前端只需要"id","name"两个属性，即可使用该方法进行转换<br/>
 * 调用方式为swapProperties(user, "id","name")<br/>
 * <br/>
 * 如果在调用时，方法中参数有instance对象不包含的属性，如user对象中不包含"role",则调用swapProperties(user,
 * "id","name","role")，"role"将被忽略
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月29日          can      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author can
 *
 */
@Component
public class PropertiesProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }

    /**
     * 将instance对象中，在properties里指定的属性封装进JSONObject对象中，并返回
     * 
     * @param instance
     *            待转换的对象
     * @param properties
     *            instance中待转换的属性
     * @return
     * @throws Throwable
     */
    public JSONObject swapProperties(Object instance, String... properties) throws Throwable {
        String[] _properties = properties;

        JSONObject result = new JSONObject();
        Method[] declaredMethods = instance.getClass().getDeclaredMethods();
        Map<String, Method> method_PRO_INSTANCE_Map = new HashMap<>();

        for (Method method : declaredMethods) {
            method_PRO_INSTANCE_Map.put(method.getName(), method);
        }

        String get_FullProperty = "";
        String is_FullProperty = "";
        for (String property : _properties) {
            if (Character.isUpperCase(property.charAt(0))) {
                get_FullProperty = "get" + property;
                is_FullProperty = "is" + property;
            } else {
                get_FullProperty = "get" + captureName(property);
                is_FullProperty = "is" + captureName(property);
            }

            if (method_PRO_INSTANCE_Map.get(get_FullProperty) != null) {
                Method method = method_PRO_INSTANCE_Map.get(get_FullProperty);
                Object invoke = invoke(instance, method, null);
                result.put(property, invoke);
            } else if (method_PRO_INSTANCE_Map.get(is_FullProperty) != null) {
                Method method = method_PRO_INSTANCE_Map.get(is_FullProperty);
                Object invoke = invoke(instance, method, null);
                result.put(property, invoke);
            }
        }
        return result;
    }

    /**
     * 将name表示的字符串首字符大写并返回
     * 
     * @param name
     * @return
     */
    private String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

}
