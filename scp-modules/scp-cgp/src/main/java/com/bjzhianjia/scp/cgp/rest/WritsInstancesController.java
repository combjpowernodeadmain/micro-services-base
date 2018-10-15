package com.bjzhianjia.scp.cgp.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import com.bjzhianjia.scp.cgp.util.DocDownUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("writsInstances")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记文书记录")
@Log4j
public class WritsInstancesController extends BaseController<WritsInstancesBiz, WritsInstances, Integer> {

    @Autowired
    private WritsInstancesBiz writsInstancesBiz;

    @Autowired
    private DocDownUtil docDownUtil;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("查询文书列表")
    public TableResultResponse<JSONObject> getList(
        @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit,
        @RequestParam(value = "templatedId", required = false) @ApiParam(name = "文书模板") Integer templatedId,
        @RequestParam(value = "templateCodes", defaultValue = "") @ApiParam(name = "文书模板Code集合，多个用逗号隔开") String templateCodes,
        @RequestParam(value = "procTaskId", defaultValue = "") @ApiParam(name = "流程任务ID") String procTaskId,
        @RequestParam(value = "caseId", defaultValue = "") @ApiParam(name = "案件 ID") String caseId) {
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();

        JSONObject queryJObj = new JSONObject();
        queryJObj.put("templatedId", templatedId);
        queryJObj.put("templateCodes", templateCodes);
        queryJObj.put("procTaskId", procTaskId);
        queryJObj.put("caseId", caseId);

        restResult = writsInstancesBiz.getList(queryJObj, page, limit);

        return restResult;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("添加单对象")
    public ObjectRestResponse<WritsInstances> add(@RequestBody @ApiParam(name = "待添加对象实例") @Validated JSONObject jobs,
        BindingResult bindingResult) {

        ObjectRestResponse<WritsInstances> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        JSONObject bizData = jobs.getJSONObject("bizData");
        writsInstancesBiz.updateOrInsert(bizData);

        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation("获取文书路径")
    public ObjectRestResponse<String> getWritsInstance(
        @RequestParam(value = "writsInstanceId", defaultValue = "") Integer writsInstanceId) {
        ObjectRestResponse<String> restResult = this.baseBiz.getWritsInstance(writsInstanceId);
        return restResult;
    }

    @RequestMapping(value = "/list/by_template", method = RequestMethod.GET)
    @ApiOperation("获取文书模板及其下的文书")
    public TableResultResponse<JSONObject> getWithWritsInstance(
        @RequestParam(value = "templateCodes", defaultValue = "") @ApiParam(name = "文书模板Code集合，多个用逗号隔开") String templateCodes,
        @RequestParam(value = "procTaskId", defaultValue = "") @ApiParam(name = "流程任务ID") String procTaskId,
        @RequestParam(value = "caseId", defaultValue = "") @ApiParam(name = "案件 ID") String caseId) {
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();

        restResult = this.baseBiz.getWithWritsInstance(templateCodes, procTaskId, caseId);
        return restResult;
    }

    @RequestMapping(value = "/add/cache", method = RequestMethod.POST)
    @ApiOperation("添加文书暂存")
    public ObjectRestResponse<WritsInstances> addCache(
        @RequestBody @ApiParam("待暂存对象实例") @Validated JSONObject writsInstances, BindingResult bindingResult) {
        ObjectRestResponse<WritsInstances> restResult = new ObjectRestResponse<>();
        this.baseBiz.addCache(writsInstances);
        return restResult;
    }

    @RequestMapping(value = "/writs/history", method = RequestMethod.GET)
    @ApiOperation("过程文书与附件")
    public ObjectRestResponse<JSONArray> writsAndAttachmentHistory(
        @RequestParam(value = "caseId", defaultValue = "") String caseId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        return this.baseBiz.writsAndAttachmentHistory(caseId);
    }

    /**
     * @param writsInstancesList
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/add/list", method = RequestMethod.POST)
    @ApiOperation("批量添加记录")
    @Deprecated
    public ObjectRestResponse<WritsInstances> addList(
        @RequestBody @Validated @ApiParam("待添加对象实例集合") JSONArray writsInstancesList, BindingResult bindingResult) {
        ObjectRestResponse<WritsInstances> resetResult = this.baseBiz.addList(writsInstancesList);
        return resetResult;
    }

    @RequestMapping(value = "/true/instance", method = RequestMethod.GET)
    @ApiOperation("生成文书实例，并返回文书实例名称")
    public ResponseEntity<?> getWritsInstances(@RequestParam(value = "fileName") String fileName,
        HttpServletResponse response) {

        log.info("请求文书实例，文书名：" + fileName);

        // response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setContentType("application/msword");

        ResponseEntity<?> file = docDownUtil.getFile(propertiesConfig.getDestFilePath(), fileName);
        return file;

    }

    /*
     *  在gate服务里添加放行路径前缀后，需要在该接口添加@IgnoreClientToken，@IgnoreUserToken<br/>
     *  在gate服务里添加旅行路径指gate不进行路径拦截，但在auth里会进行token验证
     */
    @IgnoreClientToken
    @IgnoreUserToken
    @RequestMapping(value = "/true/instance/writsId", method = RequestMethod.GET)
    @ApiOperation("生成文书实例，并返回文书实例对应的word文档")
    public ResponseEntity<?> getTrueWritsInstancesById(@RequestParam(value = "writsId") Integer writsId,
        HttpServletResponse response) {
        /*
         * 采用com.bjzhianjia.scp.cgp.rest.WritsInstancesController.
         * getWritsInstances(String,
         * HttpServletResponse)进行文书下载时，会出现下载一个空word文档情况
         * 所以提供该方法，直接提供下载
         * 
         * 该方法处理两个逻辑<br/>
         * 1 生成文书实例<br/>
         * 2 将文书实例对应的word文档返回到前端，以提供下载
         */
        log.info("请求文书实例，文书ID：" + writsId);
        // 生成文书实例
        ObjectRestResponse<String> _fileNameRest = this.baseBiz.getWritsInstance(writsId);
        // 设置响应头为响应一个word文档--doc
        // response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setContentType("application/msword");

        response.setHeader("Content-Disposition", "attachment; filename=" + _fileNameRest.getData());
        ResponseEntity<?> file = docDownUtil.getFile(propertiesConfig.getDestFilePath(), _fileNameRest.getData());
        return file;
    }

    /**
     * 根据文书ID获取PDF格式的文书
     * 
     * @param writsId
     *            待获取的文书ID
     * @param response
     * @return
     */
    @IgnoreClientToken
    @IgnoreUserToken
    @RequestMapping(value = "/true/instance/pdf/writsId", method = RequestMethod.GET)
    @ApiOperation("生成文书实例，并返回文书实例对应的word文档")
    public ResponseEntity<?> getTruePDFWritsInstancesById(@RequestParam(value = "writsId") Integer writsId,
        HttpServletResponse response) {
        log.info("请求文书PDF实例，文书ID：" + writsId);
        // 生成文书实例
        String fullPDFFileName = this.baseBiz.getTruePDFWritsInstancesById(writsId);

        // 设置响应头为响应一个word文档
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + fullPDFFileName);

        ResponseEntity<?> file = docDownUtil.getFile(propertiesConfig.getDestFilePath(), fullPDFFileName);
        return file;
    }
}