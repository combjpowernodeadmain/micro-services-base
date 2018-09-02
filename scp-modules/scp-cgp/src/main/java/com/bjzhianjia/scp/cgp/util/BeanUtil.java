package com.bjzhianjia.scp.cgp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BeanUtil {
	/**
	 * 如果source对象中与target对象中具有相同的属性，则将source对象的该属性值复制到target对象中<br/>
	 * 对于不相同的属性则进行忽略<br/>
	 * @author By尚
	 * @param source 源对象
	 * @param target 目标对象实例
	 * @return 目标对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyBean_New(Object source,T target){
		String sourceJSON=JSON.toJSONString(source);
		
		target=(T) JSON.parseObject(sourceJSON, target.getClass());
		return target;
	}
	
	/**
	 * 如果source对象中与target对象中具有相同的属性，则将source对象的该属性值复制到target对象中<br/>
	 * 对于不相同的属性则进行忽略<br/>
	 * @author By尚
	 * @param source 源对象
	 * @param target 目标对象实例
	 * @return 目标对象
	 */
	public static <T> List<T> copyBeanList_New(Object source,Class<?> clazz){
		String jsonArrayStr = JSON.toJSONString(source);
		@SuppressWarnings("unchecked")
		List<T> parseArray = (List<T>) JSONArray.parseArray(jsonArrayStr, clazz);
		return parseArray;
	}
	
	/**
	 * 将j1,j2中属性合并并返回合并后的JSONObject对象
	 * @author 尚
	 * @param j1
	 * @param j2
	 * @return
	 */
	public static JSONObject jsonObjectMergeOther(JSONObject j1,JSONObject j2) {
		String j=j1.toJSONString()+j2.toJSONString();
		j=j.replace("}{", ",");
		return JSONObject.parseObject(j);
	}

	/**
	 * 去掉bean中所有属性为字符串的前后空格
	 * 
	 * @param bean
	 * 		  实体类
	 * @throws Exception
	 */
	public static void beanAttributeValueTrim(Object bean) {
		if (bean != null) {
			// 获取所有的字段包括public,private,protected,private
			Field[] fields = bean.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				if (f.getType().getName().equals("java.lang.String")) {
					String key = f.getName();// 获取字段名
					Object value = getFieldValue(bean, key);
					if (value == null)
						continue;
					setFieldValue(bean, key, value.toString().trim());
				}
			}
		}
	}

	/**
	 * 利用反射通过get方法获取bean中字段fieldName的值
	 * 
	 * @param bean
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static Object getFieldValue(Object bean, String fieldName)  {
		StringBuffer result = new StringBuffer();
		String methodName = result.append("get").append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1)).toString();

		Object rObject = null;
		Method method = null;

		Class[] classArr = new Class[0];
		try {
			method = bean.getClass().getMethod(methodName, classArr);
			rObject = method.invoke(bean, new Object[0]);
		}catch (Exception e) {
		}
		return rObject;
	}

	/**
	 * 利用发射调用bean.set方法将value设置到字段
	 * 
	 * @param bean
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static void setFieldValue(Object bean, String fieldName, Object value)  {
		StringBuffer result = new StringBuffer();
		String methodName = result.append("set").append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1)).toString();

		/**
		 * 利用发射调用bean.set方法将value设置到字段
		 */
		Class[] classArr = new Class[1];
		classArr[0] = "java.lang.String".getClass();
		try {
			Method method = bean.getClass().getMethod(methodName, classArr);
			method.invoke(bean, value);
		}catch (Exception e) {
		}
	}

}
