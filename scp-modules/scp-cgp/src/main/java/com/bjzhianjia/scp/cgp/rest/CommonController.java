package com.bjzhianjia.scp.cgp.rest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.CaseInfoService;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * CommonController 类描述. 用于处理通用的请求
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年10月14日          can      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author can
 *
 */
@RestController
@RequestMapping("common")
@CheckClientToken
@CheckUserToken
@Api(tags = "通用请求")
@Slf4j
public class CommonController {

    @Autowired
    private Environment environment;

    @Autowired
    private CaseInfoService caserInfoService;

    @Autowired
    private CaseRegistrationBiz caseRegistrationBiz;

    @GetMapping("/dictValue")
    @ApiOperation("用于请求dict库中code的值")
    public ObjectRestResponse<String> getDictCode(
        @RequestParam(value = "codeKey") @ApiParam(value = "与后台约定的值") String codeKey) {
        ObjectRestResponse<String> restResult = new ObjectRestResponse<>();

        String codeValue = environment.getProperty(codeKey);
        if (StringUtils.isBlank(codeValue)) {
            restResult.setStatus(400);
            restResult.setMessage("未找到与" + codeKey + "对应的值");
            return restResult;
        }
        restResult.setStatus(200);
        restResult.setData(codeValue);
        return restResult;
    }
    
    @GetMapping("/propertyValue")
    @ApiOperation("用于请求后台配置文件中的属性值")
    public ObjectRestResponse<String> getProperty(
        @RequestParam(value = "propertyKey") @ApiParam("与后台约定的值") String propertyKey) {

        String property = environment.getProperty(propertyKey);
        // 处理配置文件中的中文,如果属性不存在，将会导致空指针
        if (StringUtils.isNotBlank(property)) {
            property =
                new String(property.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
        return new ObjectRestResponse<String>().data(property);
    }

    @PostMapping("/webDefault/caseInfo/allTasks")
    @ApiOperation("事件综合查询")
    @IgnoreClientToken
    @IgnoreUserToken
    public TableResultResponse<JSONObject> caseInfoAllTasks(@RequestBody JSONObject objs, HttpServletRequest request){
        TableResultResponse<JSONObject> tableresult=new TableResultResponse<>();
        try {

            Result<Void> voidResult = _checkAuth(objs);

            if(!voidResult.getIsSuccess()){
                tableresult.setMessage(voidResult.getMessage());
                tableresult.setStatus(401);
                return tableresult;
            }

            TableResultResponse<JSONObject> tasks = caserInfoService.pushOfTwoWays(objs);
            return tasks;
        } catch (Exception e) {
            tableresult.setMessage("Server error!");
            tableresult.setStatus(500);
            return tableresult;
        }
    }

    @PostMapping("/webDefault/caseInfo/userTaskDetail")
    @ApiOperation("事件详情")
    @IgnoreClientToken
    @IgnoreUserToken
    public ObjectRestResponse<JSONObject> userCaseInfoTaskDetail(@RequestBody JSONObject objs, HttpServletRequest request){
        ObjectRestResponse<JSONObject> objectResult=new ObjectRestResponse<>();

        try {
            Result<Void> voidResult = _checkAuth(objs);
            if(!voidResult.getIsSuccess()){
                objectResult.setMessage(voidResult.getMessage());
                objectResult.setStatus(401);
                return objectResult;
            }

            ObjectRestResponse<JSONObject> userToDoTask = caserInfoService.pushOfTwoWaysDetail(objs);
            return userToDoTask;
        } catch (Exception e) {
            objectResult.setMessage("Server error!");
            objectResult.setStatus(500);
            return objectResult;
        }
    }

    @PostMapping("/webDefault/caseRegistration/allTasks")
    @ApiOperation("案件综合查询")
    @IgnoreClientToken
    @IgnoreUserToken
    public TableResultResponse<JSONObject> caseRegistrationAllTasks(@RequestBody JSONObject objs, HttpServletRequest request){
        TableResultResponse<JSONObject> tableresult=new TableResultResponse<>();

        try {
            Result<Void> voidResult = _checkAuth(objs);

            if(!voidResult.getIsSuccess()){
                tableresult.setMessage(voidResult.getMessage());
                tableresult.setStatus(401);
                return tableresult;
            }

            TableResultResponse<JSONObject> jsonObjectTableResultResponse = caseRegistrationBiz.pushOfTwoWays(objs);
            return jsonObjectTableResultResponse;
        } catch (Exception e) {
            tableresult.setMessage("Server error!");
            tableresult.setStatus(500);
            return tableresult;
        }
    }

    @PostMapping("/webDefault/caseRegistration/userTaskDetail")
    @ApiOperation("案件详情接口")
    @IgnoreClientToken
    @IgnoreUserToken
    public ObjectRestResponse<JSONObject> getUserCaseRegistrationToDoTask(@RequestBody JSONObject objs, HttpServletRequest request){
        ObjectRestResponse<JSONObject> objectResult=new ObjectRestResponse<>();

        try {
            Result<Void> voidResult = _checkAuth(objs);
            if(!voidResult.getIsSuccess()){
                objectResult.setMessage(voidResult.getMessage());
                objectResult.setStatus(401);
                return objectResult;
            }

            if (objs != null) {
                objectResult.setData(caseRegistrationBiz.pushOfTwoWaysDetail(objs));
            } else {
                objectResult.setStatus(400);
                objectResult.setMessage("非法参数");
            }

            return objectResult;
        } catch (Exception e) {
            objectResult.setMessage("Server error!");
            objectResult.setStatus(500);
            return objectResult;
        }
    }

    private Result<Void> _checkAuth(JSONObject objs) {
        Result<Void> result = new Result<>();
        String msg = "Authentication failed. Please Get the correct key.";

        String clientStr = environment.getProperty("common.external.client");
        JSONObject clientJObj = JSONObject.parseObject(clientStr);

        // 对appKey做验证
        String appKey = objs.getString("appKey");
        if(!clientJObj.keySet().contains(appKey)){
            result.setMessage("'appKey' does not exist or error.");
            result.setIsSuccess(false);
            return result;
        }

        try {
            String sign = objs.getString("sign");
            objs.remove("sign");

            List<String> authData2 = _getJObjSortStrings(objs.getJSONObject("authData"));
            List<String> bizData2 = _getJObjSortStrings(objs.getJSONObject("bizData"));
            List<String> procData2 = _getJObjSortStrings(objs.getJSONObject("procData"));
            List<String> queryData2 = _getJObjSortStrings(objs.getJSONObject("queryData"));
            List<String> variableData2 = _getJObjSortStrings(objs.getJSONObject("variableData"));

            JSONObject objs02 = new JSONObject();
            objs02.put("authData", String.join("", authData2));
            objs02.put("bizData", String.join("", bizData2));
            objs02.put("procData", String.join("", procData2));
            objs02.put("queryData", String.join("", queryData2));
            objs02.put("variableData", String.join("", variableData2));
            String secret = clientJObj.getJSONObject(appKey).getString("secret");
            // String secret =
            // environment.getProperty("common.external.secret","");
            objs02.put("secret", secret);
            objs02.put("appKey", appKey);

            List<String> sortObjsList = _getJObjSortStrings(objs02);

            System.out.println(String.join("", sortObjsList));

            String signOfServer =
                DigestUtils.md5DigestAsHex(String.join("", sortObjsList).getBytes());
            signOfServer = signOfServer.toUpperCase();

            //验证签名是否正确
            if (!sign.equals(signOfServer)) {
                result.setMessage("'sign' is error,please try again.");
                result.setIsSuccess(false);
                return result;
            }

            //验证时间的有效性
            JSONObject authData = objs.getJSONObject("authData");
            if(authData != null ) {
                Long timestamp = authData.getLong("timestamp");
                //不存在时间戳，则认证失败
                if(timestamp == null || timestamp == 0){
                    result.setMessage("'timestamp' is not present,please check the query data.");
                    result.setIsSuccess(false);
                    return result;
                }
                //判断当前请求的时间戳是否超过1分钟，超时则失效
                Date timestampDate = DateUtil.dateFromLongToDate(timestamp);
                // 将超时时间从配置文件中读取
                long endTime = DateUtil.addMinute(timestampDate,Integer.valueOf(environment.getProperty("common.external.expi"))).getTime();
                long nowTime = new Date().getTime();
                if(endTime < nowTime ){
                    result.setMessage("'sign' expired,please try to generate another one.");
                    result.setIsSuccess(false);
                    return result;
                }
            }

            result.setIsSuccess(true);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(msg);
            result.setIsSuccess(false);
            return result;
        }
    }

    private List<String> _getJObjSortStrings(JSONObject jObj) {
        List<String> jObjStrList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(jObj)) {
            for (String queryKey : jObj.keySet()) {
                jObjStrList.add(queryKey + "=" + jObj.get(queryKey));
            }
        }

        jObjStrList.sort(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return jObjStrList;
    }
}
