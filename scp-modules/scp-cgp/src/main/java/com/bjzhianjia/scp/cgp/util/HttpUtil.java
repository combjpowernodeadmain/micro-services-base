package com.bjzhianjia.scp.cgp.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ConnectException;
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
     * 默认连接超时
     */
    private static final int DEFAULT_MAX_CONNECT_TIMEOUT = 1000;
    /**
     * 默认读取超时
     */
    private static final int DEFAULT_MAX_READ_TIMEOUT = 5000;

    /**
     * POST请求获取JSONObject结果集
     *
     * @param uri    请求地址（携带http/https）
     * @param params 请求参数
     * @return 结果集 json对象
     * @throws IOException 访问异常
     */
    public static JSONObject post(String uri, JSONObject params) throws IOException {
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //设置请求参数
        HttpEntity<String> formEntity = new HttpEntity<>(params.toJSONString(), headers);

        RestTemplate rest = HttpUtil.getRestTemplate(HttpUtil.DEFAULT_MAX_CONNECT_TIMEOUT,
                HttpUtil.DEFAULT_MAX_READ_TIMEOUT);
        JSONObject response = rest.postForObject(uri, formEntity, JSONObject.class);
        //获取返回结果集
        return response == null ? new JSONObject() : response;
    }

    /**
     * GET请求获取JSONObject结果集
     *
     * @param uri 请求地址（携带http/https）
     * @return 结果集 json对象
     * @throws IOException 访问异常
     */
    public static JSONObject get(String uri) throws IOException {
        RestTemplate rest = HttpUtil.getRestTemplate(HttpUtil.DEFAULT_MAX_CONNECT_TIMEOUT,
                HttpUtil.DEFAULT_MAX_READ_TIMEOUT);
        JSONObject response = rest.getForObject(uri, JSONObject.class);
        //获取返回结果集
        return response == null ? new JSONObject() : response;
    }

    /**
     * 获取请求对象RestTemplate
     *
     * @param connectTimeout 连接超时时间：秒
     * @param readTimeout    读取内容超时时间：秒
     * @return
     */
    private static RestTemplate getRestTemplate(int connectTimeout, int readTimeout) {
        // 设置超时为一秒
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        // ConnectTimeout设置超时
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        return new RestTemplate(requestFactory);
    }
}
