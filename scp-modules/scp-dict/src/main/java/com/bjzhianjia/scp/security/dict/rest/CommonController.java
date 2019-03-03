package com.bjzhianjia.scp.security.dict.rest;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.dict.utils.ImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    @RequestMapping(value = "/commandCenterTitle", method = RequestMethod.GET)
    @ApiOperation("生成指挥中心主页标题图片")
    public void getCommandCenterTitle(
        @RequestParam(value = "width",defaultValue = "578") @ApiParam(value = "图片宽度，默认578") Integer width,
        @RequestParam(value = "heigth",defaultValue = "67") @ApiParam(value = "图片高度，默认67") Integer heigth,
        @RequestParam(value = "frontType",defaultValue = "方正综艺简体") @ApiParam(value = "图片中字体，默认'方正综艺简体'") String frontType,
        @RequestParam(value = "isBlod",defaultValue = "true") @ApiParam(value = "是否加粗，默认为'是'") boolean isBlod,
        @RequestParam(value = "frontSize",defaultValue = "45") @ApiParam(value = "图片中字号，默认为45") Integer frontSize,
        @RequestParam(value = "text") @ApiParam(value = "图片中内容，必传") String text,
        @RequestParam(value = "r",defaultValue = "167") @ApiParam(value = "图片中文字颜色，默认167") Integer r,
        @RequestParam(value = "b",defaultValue = "230") @ApiParam(value = "图片中文字颜色，默认230") Integer b,
        @RequestParam(value = "g",defaultValue = "243") @ApiParam(value = "图片中文字颜色，默认243") Integer g,
        @RequestParam(value = "imgFormatName",defaultValue = "png") @ApiParam(value = "图片格式，默认为'png'") String imgFormatName,
        HttpServletResponse response) {
        log.debug("生成图片标题");
        try {
            Font font = new Font(frontType, isBlod?Font.BOLD:Font.PLAIN, frontSize);//字体

            BufferedImage bi1 = ImageUtil
                .generateImg(width, heigth, text, new Color(r, b, g), font, 0d, 1.0f);//给图片添加文字水印
            ImageIO.write(bi1, imgFormatName, response.getOutputStream());//写入文件 )
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
