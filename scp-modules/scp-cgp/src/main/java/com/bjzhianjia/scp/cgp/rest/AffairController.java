package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.util.HttpUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * AffairController  获取第三方事件信息相关接口.
 * <p>
 * 当前controller通过http调用第三方接口
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
@RestController
@CheckClientToken
@CheckUserToken
public class AffairController {

    @Autowired
    private Environment environment;


    /**
     * 根据查询条件模糊查询，获取业务信息列表
     *
     * @return
     */
    @ApiOperation("根据查询条件模糊查询，获取业务信息列表")
    @GetMapping("/affair/list")
    public JSONObject getList(
            @RequestParam(value = "currAffairCode", defaultValue = "") @ApiParam(value = "业务流水号") String currAffairCode,
            @RequestParam(value = "affairName", defaultValue = "") @ApiParam(value = "事项名称") String affairName,
            @RequestParam(value = "beginDate", defaultValue = "") @ApiParam(value = "开始日期") String beginDate,
            @RequestParam(value = "endDate", defaultValue = "") @ApiParam(value = "截止日期") String endDate,
            @RequestParam(value = "classifyId", defaultValue = "0") @ApiParam(value = "业务部门") Integer classifyId,
            @RequestParam(value = "personName", defaultValue = "") @ApiParam(value = "办事人") String personName,
            @RequestParam(value = "cardNum", defaultValue = "") @ApiParam(value = "证件号码") String cardNum,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam(value = "页码") String page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam(value = "页容量") String limit) {

        JSONObject response;
        //没有日期筛选范围，则默认获取当年数据
        if (StringUtils.isBlank(beginDate)) {
            beginDate = DateUtil.dateFromDateToStr(DateUtil.getYearStart(), DateUtil.DATE_FORMAT_DF);
        }
        if (StringUtils.isBlank(endDate)) {
            endDate = DateUtil.dateFromDateToStr(DateUtil.getYearEnd(), DateUtil.DATE_FORMAT_DF);
        }
        //封装请求参数
        JSONObject params = new JSONObject();
        params.put("CurrAffairCode", currAffairCode);
        params.put("AffairName", affairName);
        params.put("BeginDate", beginDate);
        params.put("EndDate", endDate);
        params.put("ClassifyId", classifyId);
        params.put("PersonName", personName);
        params.put("CardNum", cardNum);
        //TODO 页码和页容量

        try {
            String url = environment.getProperty("common.external.url");
            url += "/XXKAffairInfo/GetAffairInfo";
            response = HttpUtil.post(url, params);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response = new JSONObject();
        response.put("Result", false);
        response.put("Msg", "系统内部错误，请联系管理员！");
        response.put("Data", null);
        return response;
    }

    /**
     * 根据业务流水号获取业务详细信息
     *
     * @param affairCode 事项编码
     * @return
     */
    @ApiOperation("根据业务流水号获取业务详细信息")
    @GetMapping("/affair/affairCode/{affairCode}")
    public JSONObject getInfoByAffairCode(
            @PathVariable("affairCode") @ApiParam("事项编码") String affairCode) {

        //初始化结果集
        JSONObject response;
        try {
            String url = environment.getProperty("common.external.url");
            url += "/XXKAffairInfo/GetAffairInfoBYCurrAffairCode?currAffairCode=" + affairCode;
            response = HttpUtil.post(url,new JSONObject());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response = new JSONObject();
        response.put("Result", false);
        response.put("Msg", "系统内部错误，请联系管理员！");
        response.put("Data", null);
        return response;
    }

    /**
     * 根据业务流水号获取业务详细信息
     *
     * @param filePath 文件路径
     * @return
     */
    @ApiOperation("根据查询条件模糊查询，获取业务信息列表")
    @GetMapping("/tool/checkFilePath")
    public JSONObject checkFilePath(@RequestParam("filePath") @ApiParam("文件路径") String filePath) {

        //初始化结果集
        JSONObject response;
        try {
            String url = environment.getProperty("common.external.url");
            url += "/Tool/checkFilePath?filePath=" + filePath;
            response = HttpUtil.get(url);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response = new JSONObject();
        response.put("Result", false);
        response.put("Msg", "系统内部错误，请联系管理员！");
        response.put("Data", null);
        return response;
    }


    /**
     * 获取文件Base64
     *
     * @param filePath 文件路径
     * @return
     */
    @ApiOperation("获取文件Base64")
    @GetMapping("/tool/getFileBase64")
    public JSONObject getFileBase64(@RequestParam("filePath") @ApiParam("文件路径") String filePath) {

        //初始化结果集
        JSONObject response;
        try {
            String url = environment.getProperty("common.external.url");
            url += "/Tool/getFileBase64?filePath=" + filePath;
            response = HttpUtil.get(url);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response = new JSONObject();
        response.put("Result", false);
        response.put("Msg", "系统内部错误，请联系管理员！");
        response.put("Data", null);
        return response;
    }
}
