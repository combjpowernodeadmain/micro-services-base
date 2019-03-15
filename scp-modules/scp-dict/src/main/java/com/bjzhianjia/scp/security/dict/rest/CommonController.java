package com.bjzhianjia.scp.security.dict.rest;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.dict.biz.CommonBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
    private CommonBiz commonBiz;

    @Autowired
    private Environment environment;

    @IgnoreClientToken
    @IgnoreUserToken
    @GetMapping("/webDefault")
    @ApiOperation("前端默认需要加载的数据")
    public ObjectRestResponse<String> webDefault() throws Exception {
        ObjectRestResponse<String> restResult = new ObjectRestResponse<>();

        String codeValue = commonBiz.getWebDefault();

        restResult.setStatus(200);
        restResult.setData(codeValue);
        return restResult;
    }

    @IgnoreClientToken
    @IgnoreUserToken
    @RequestMapping(value = "/commandCenterTitle", method = RequestMethod.GET)
    @ApiOperation("生成指挥中心主页标题图片")
    public void getCommandCenterTitle(HttpServletResponse response) throws Exception {
        log.debug("生成图片标题");

//        final String imgPath="D:\\test.png";
        String imgPath=environment.getProperty("commandCenterTitle.imgPath");

        File file=null;
        InputStream is=null;

        try {
            file=new File(imgPath);
            is=new FileInputStream(file);

            byte[] bytes = new byte[(int) file.length()];
            is.read(bytes);

            response.setContentType("image/png");
            ServletOutputStream outputStream = response.getOutputStream();

            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is!=null){
                is.close();
            }
        }
    }
}
