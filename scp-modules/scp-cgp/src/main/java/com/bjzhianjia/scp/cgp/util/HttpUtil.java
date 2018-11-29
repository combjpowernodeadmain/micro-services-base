package com.bjzhianjia.scp.cgp.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * HttpUtil 请求工作类.
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/11/28          chenshuai      1.0            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
public class HttpUtil {
    /**
     * POST请求获取JSONObject结果集
     *
     * @param uri    请求地址（携带http/https）
     * @param params 请求参数
     * @return 结果集 json对象
     */
    public static JSONObject post(String uri, JSONObject params) {
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //设置请求参数
        HttpEntity<String> formEntity = new HttpEntity<>(params.toJSONString(), headers);

        RestTemplate rest = new RestTemplate();
        JSONObject response = rest.postForObject(uri, formEntity, JSONObject.class);
        //获取返回结果集
        return response == null ? new JSONObject() : response;
    }

    /**
     * GET请求获取JSONObject结果集
     *
     * @param uri 请求地址（携带http/https）
     * @return 结果集 json对象
     */
    public static JSONObject get(String uri) {
        RestTemplate rest = new RestTemplate();
        JSONObject response = rest.getForObject(uri, JSONObject.class);
        //获取返回结果集
        return response == null ? new JSONObject() : response;
    }
}
