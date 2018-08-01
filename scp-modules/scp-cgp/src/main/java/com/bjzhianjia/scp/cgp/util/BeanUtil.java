package com.bjzhianjia.scp.cgp.util;

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
}
