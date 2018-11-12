package com.bjzhianjia.scp.cgp.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BeanUtil {

    /**
     * 如果source对象中与target对象中具有相同的属性，则将source对象的该属性值复制到target对象中<br/>
     * 对于不相同的属性则进行忽略<br/>
     * 
     * @author By尚
     * @param source
     *            源对象
     * @param target
     *            目标对象实例
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T copyBean_New(Object source, T target) {
        String sourceJSON = JSON.toJSONString(source);

        target = (T) JSON.parseObject(sourceJSON, target.getClass());
        return target;
    }

    /**
     * 如果source对象中与target对象中具有相同的属性，则将source对象的该属性值复制到target对象中<br/>
     * 对于不相同的属性则进行忽略<br/>
     * 
     * @author By尚
     * @param source
     *            源对象
     * @return 目标对象
     */
    public static <T> List<T> copyBeanList_New(Object source, Class<?> clazz) {
        String jsonArrayStr = JSON.toJSONString(source);
        @SuppressWarnings("unchecked")
        List<T> parseArray = (List<T>) JSONArray.parseArray(jsonArrayStr, clazz);
        return parseArray;
    }

    /**
     * 将j1,j2中属性合并并返回合并后的JSONObject对象
     * 
     * @author 尚
     * @param j1
     * @param j2
     * @return
     */
    public static JSONObject jsonObjectMergeOther(JSONObject j1, JSONObject j2) {
        String j = j1.toJSONString() + j2.toJSONString();
        j = j.replace("}{", ",");
        return JSONObject.parseObject(j);
    }

    /**
     * 去掉bean中所有属性为字符串的前后空格
     * 
     * @param bean
     *            实体类
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
                    if (value == null) continue;
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
    private static Object getFieldValue(Object bean, String fieldName) {
        StringBuffer result = new StringBuffer();
        String methodName =
            result.append("get").append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1)).toString();

        Object rObject = null;
        Method method = null;

        Class[] classArr = new Class[0];
        try {
            method = bean.getClass().getMethod(methodName, classArr);
            rObject = method.invoke(bean, new Object[0]);
        } catch (Exception e) {
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
    private static void setFieldValue(Object bean, String fieldName, Object value) {
        StringBuffer result = new StringBuffer();
        String methodName =
            result.append("set").append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1)).toString();

        /**
         * 利用发射调用bean.set方法将value设置到字段
         */
        Class[] classArr = new Class[1];
        classArr[0] = "java.lang.String".getClass();
        try {
            Method method = bean.getClass().getMethod(methodName, classArr);
            method.invoke(bean, value);
        } catch (Exception e) {
        }
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

    /**
     * 将clazz类中，包含skipFields内的属性去除掉
     * 该方法通常用于：
     * 在进行查询数据库中指定字段时，如果不被查询的字段较少，可使用该方法将不进行查询的字段去除掉
     * 
     * @author By尚
     * @param clazz
     * @param skipFields
     * @return clazz中去除skipFields中属性后的属性数组
     */
    public static String[] skipFields(Class<?> clazz, String... skipFields) {
        Field[] declaredFields = clazz.getDeclaredFields();

        List<String> originFieldsList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                originFieldsList.add(field.getName());
            }
        }

        List<String> skipFieldsList;
        if (skipFields == null) {
            skipFieldsList = Arrays.asList("");
        } else {
            skipFieldsList = Arrays.asList(skipFields);
        }

        originFieldsList.removeAll(skipFieldsList);

        return originFieldsList.toArray(new String[originFieldsList.size()]);
    }

    /**
     * 在对象o中，如果某一属性为NULL，则将其填充为空字符串
     * @param o
     * @return
     */
    public static JSONObject fillNullToEmpty(Object o) {
        String[] validataFields = skipFields(o.getClass(), null);
        return fillNullToEmptyInFields(o, validataFields);
    }

    /**
     * 在对象o中，将排除fields属性的值返回<br/>
     * 在返回过程中，如果o中某一属性值为NULL，则将其填充为空字符串<br/>
     * @param o
     * @param fields
     * @return
     */
    public static JSONObject fillNullToEmptyExcludeFields(Object o, String... fields) {
        String[] validateFields = skipFields(o.getClass(), fields);
        return fillNullToEmptyInFields(o, validateFields);
    }

    /**
     * 在对象o中，如果将包含fields的属性返回<br/>
     * 在返回的过程中，如果与fields中某一字段对应的属性值为NULL，则将其填充为空字符串<br/>
     * 该方法通常用在需要将数据返回前端页面时调用<br/>
     * @param o
     * @param fields
     * @return
     */
    public static JSONObject fillNullToEmptyInFields(Object o,String... fields){
        List<String> fieldList = Arrays.asList(fields);

        Field[] declaredFields = o.getClass().getDeclaredFields();
        Method[] declaredMethods = o.getClass().getDeclaredMethods();

        Map<String,Method> getMethodMap=new HashMap<>();
        if(BeanUtil.isNotEmpty(declaredFields)) {
            for (Method method : declaredMethods) {
                boolean isGet = method.getName().startsWith("get") && _isListContainIgnoreCase(fieldList, method.getName().replace("get", ""));
                if(isGet){
                    getMethodMap.put(_captureName(method.getName().replace("get", "")), method);
                }
            }
        }

        JSONObject resultJObj = new JSONObject();
        if (BeanUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                if (fieldList.contains(field.getName())) {
                    try {
                        if (BeanUtil.isEmpty(getMethodMap.get(field.getName()).invoke(o, null))) {
                            resultJObj.put(field.getName(), "");
                        } else {
                            resultJObj.put(field.getName(),
                                getMethodMap.get(field.getName()).invoke(o, null));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        // 如果发生空指针异常，是因为方法代理时没找到导致的，使程序不中断，在此做处理
                    }
                }
            }
        }

        return resultJObj;
    }

    /**
     * 判断fieldList中是否包含suffixMethod字符串
     * 在进行比较时，忽略suffixMethod首字符
     * @param fieldList
     * @param suffixMethod
     * @return
     */
    private static boolean _isListContainIgnoreCase(List<String> fieldList, String suffixMethod) {
        if (fieldList.contains(_captureName(suffixMethod))) {
            return true;
        }
        return false;
    }

    /**
     * 如果name以大写字母开头，则将其首字母小写后，将name返回
     *
     * @param name
     * @return
     */
    private static String _captureName(String name) {
        char[] cs = name.toCharArray();
        if(cs[0]>='A'&&cs[0]<='Z'){
            cs[0] += 32;
        }
        return String.valueOf(cs);
    }

    /**
     * 在进行选择性查询数据库字段或是填充空字符串时，默认在排除的字段
     */
    public static List<String> defaultSkipFields=new ArrayList<>();

    static{
        defaultSkipFields.add("crtTime");
        defaultSkipFields.add("crtUserId");
        defaultSkipFields.add("crtUserName");
        defaultSkipFields.add("updTime");
        defaultSkipFields.add("updUserId");
        defaultSkipFields.add("updUserName");
        defaultSkipFields.add("tenantId");
    }
}
