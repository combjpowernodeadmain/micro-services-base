package com.bjzhianjia.scp.cgp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;

/**
 * 公共工具类
 * 
 * @author 尚
 *
 */
public class CommonUtil {

    /**
     * 生成立案单编号
     * 
     * @author 尚
     */
    public static Result<String> generateCaseCode(String oldCode) {
        Result<String> result = new Result<>();
        result.setIsSuccess(false);

        String caseCode = "";
        boolean flag;
        int isLoop = 0;
        do {
            // 当生成事件编号过程出现错误时，则再次调用生成编号的方法，避免因生成编号异常造成程序中断
            flag = false;
            try {
                caseCode = generateCaseCode01(oldCode);
            } catch (Exception e) {
                flag = true;
            }
            isLoop++;
            if (isLoop >= 100) {
                // 如果连续100次生成编号都异常，则也退出程序，避免造成死循环
                result.setIsSuccess(false);
                result.setMessage("生成事件编号失败");
                return result;
            }
        } while (flag);

        result.setIsSuccess(true);
        result.setData(caseCode);
        return result;
    }

    private static String generateCaseCode01(String oldCode) throws Exception {
        // 前缀
        String codePrefix = DateUtil.dateFromDateToStr(new java.util.Date(), "yyyyMM");

        // 事件序号
        String seqMid = "";
        if (oldCode == null) {
            seqMid = "00001";
        } else {
            String seqStr = oldCode.substring(6, 11);
            seqMid = String.format("%05d", Integer.valueOf(seqStr) + 1);
        }

        // 后缀
        Random random = new Random();
        int suffix = random.nextInt(1000);
        String suffixStr = String.format("%03d", suffix);

        StringBuffer caseInfo = new StringBuffer();
        caseInfo.append(codePrefix).append(seqMid).append(suffixStr);

        return caseInfo.toString();
    }

    /**
     * 事件完成状态聚和数据时的帮助类
     * 
     * @author 尚
     * @param dictFeign
     * @param code
     * @return
     */
    public static String exeStatusUtil(DictFeign dictFeign, String code) {
        // Map<String, String> dictValueMap = dictFeign.getDictIdByCode(code,
        // false);

        // if (dictValueMap != null && !dictValueMap.isEmpty()) {
        // List<String> _idList = new ArrayList<>(dictValueMap.keySet());
        // // dictValueMap按code等值查询，得到的结果集为唯一
        // return _idList.get(0);
        // }
        return code;
    }

    /**
     * 从一个json格式字符串中获取key所对应的value<br/>
     * 如key为“name” 则从{"name":"尚","id":"123"}中获取到尚<br/>
     * 
     * @author 尚
     * @return
     */
    public static String getValueFromJObjStr(String jObjStr, String key) {
        if (StringUtils.isNotBlank(jObjStr)) {
            JSONObject jobj = JSONObject.parseObject(jObjStr);
            return jobj.getString(key) == null ? null : jobj.getString(key);
        }
        return null;
    }

    /**
     * 通过数据字典code查询
     * 
     * @author chenshuai
     * @param dictFeign
     *            数据字典接口
     * @param code
     *            数据字典code
     * @return
     *         数据字典标签名（label_default）
     */
    public static String getByCode(DictFeign dictFeign, String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        Map<String, String> dictValueMap = dictFeign.getByCode(code);

        if (dictValueMap != null && !dictValueMap.isEmpty()) {
            return dictValueMap.get(code);
        }
        return null;
    }

    /**
     * 将sourceMap中key值用"${" "}"符号进行包围起来<br/>
     * 如sourceMap==key:value,则返回resultMap==${key}:value
     * 
     * @param sourceMap
     * @return resultMap==${key}:value
     */
    @Deprecated
    public static Map<String, String> surroundKeyWith_$(Map<String, String> sourceMap) {
        Map<String, String> resultMap = new HashMap<>();

        if (sourceMap != null) {
            for (String key : sourceMap.keySet()) {
                resultMap.put("#{" + key + "}", sourceMap.get(key));
            }
            return resultMap;
        } else {
            return sourceMap;
        }
    }
}
