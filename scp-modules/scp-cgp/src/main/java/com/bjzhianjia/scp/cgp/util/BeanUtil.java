package com.bjzhianjia.scp.cgp.util;

import com.alibaba.fastjson.JSON;

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
}
