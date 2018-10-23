package com.bjzhianjia.scp.security.dict.rest;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

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

    @IgnoreClientToken
    @IgnoreUserToken
    @GetMapping("/webDefault")
    @ApiOperation("前端默认需要加载的数据")
    public ObjectRestResponse<String> webDefault(){
        ObjectRestResponse<String> restResult = new ObjectRestResponse<>();

        String codeValue = environment.getProperty("webDefault");
        
        try {
            codeValue=new String(codeValue.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        if (StringUtils.isBlank(codeValue)) {
            log.warn("未找到与webDefault对应的值");
            return restResult;
        }
        restResult.setStatus(200);
        restResult.setData(codeValue);
        return restResult;
    }
}
