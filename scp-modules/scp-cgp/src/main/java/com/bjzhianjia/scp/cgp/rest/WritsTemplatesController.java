package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.cgp.biz.WritsTemplatesBiz;
import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.util.DocDownUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("writsTemplates")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记  文书模板")
@Slf4j
public class WritsTemplatesController extends BaseController<WritsTemplatesBiz, WritsTemplates, Integer> {

    @Autowired
    private DocDownUtil docDownUtil;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<WritsTemplates> add(
        @RequestBody @ApiParam(name = "待添加对象实例") @Validated WritsTemplates writsTemplates,
        BindingResult bindingResult) {
        ObjectRestResponse<WritsTemplates> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            restResult.setStatus(400);
            return restResult;
        }
        Result<Void> result = this.baseBiz.created(writsTemplates);
        if (!result.getIsSuccess()) {
            restResult.setMessage(result.getMessage());
            restResult.setStatus(400);
            return restResult;
        }

        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/list/node", method = RequestMethod.GET)
    @ApiOperation("按ID集合获取记录")
    public TableResultResponse<WritsTemplates> getList(
        @RequestParam(value = "node", defaultValue = "") @ApiParam(name = "待查询结点") String node,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam("頁容量") Integer limit) {
        TableResultResponse<WritsTemplates> list = this.baseBiz.getListByNode(node, page, limit);
        return list;
    }

    @RequestMapping(value = "/templates/tcode")
    @ApiOperation("按模板tcode获取列表")
    public List<WritsTemplates> getByCodes(
        @RequestParam(value = "tcode", defaultValue = "") @ApiParam(name = "模板tcode") String tcode,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam("頁容量") Integer limit) {
        List<WritsTemplates> byTcodes = this.baseBiz.getByTcodes(tcode);
        return byTcodes;
    }

    @RequestMapping(value = "/downLoad/{templateId}", method = RequestMethod.GET)
    @ApiOperation("生成文书实例，并返回文书实例对应的word文档")
    public ResponseEntity<?> getTrueWritsInstancesById(
        @PathVariable(value = "templateId") @ApiParam(value = "待下载文书模板的ID") String templateId,
        HttpServletResponse response) {
        log.info("文书模板下载，模板ID：" + templateId);

        Integer _templateId = -1;
        if (StringUtils.isNotBlank(templateId)) {
            _templateId = Integer.parseInt(templateId);
        } else {
            throw new RuntimeException("请输入模板ID");
        }

        WritsTemplates template = this.baseBiz.selectById(_templateId);
        // 设置响应头为响应一个word文档
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        ResponseEntity<?> file =
            docDownUtil.getFile(propertiesConfig.getTemplateSrcPath(), template.getName() + ".docx");
        return file;
    }

    @ApiOperation(("分页获取列表"))
    @GetMapping("/list")
    public TableResultResponse<WritsTemplates> getList(
            @RequestParam(defaultValue ="1") @ApiParam("起始页") Integer page
            ,@RequestParam(defaultValue = "10") @ApiParam("页容量") Integer limit
            ,@RequestParam(defaultValue = "") @ApiParam("模板名称") String name
    ){
        TableResultResponse<WritsTemplates> tableResult=new TableResultResponse<>();

        WritsTemplates writsTemplates=new WritsTemplates();
        writsTemplates.setName(name);

        TableResultResponse<WritsTemplates> list = this.baseBiz.getList(writsTemplates, page, limit);

        return list;
    }

    @PutMapping("/remove/{ids}")
    @ApiOperation("批量删除对象")
    public ObjectRestResponse<WritsTemplates> remove(@PathVariable("ids")Integer[] ids){
        return this.baseBiz.remove(ids);
    }

    @PostMapping("/upload/writsTempalte")
    @ApiOperation("上传文书模板")
    @Deprecated
    public ObjectRestResponse<WritsTemplates> uploadWritsTemplate(
            @RequestParam("file") MultipartFile file
            ,@RequestParam(value="id",required = false)Integer id
    ){
        ObjectRestResponse<WritsTemplates> restResult=new ObjectRestResponse<>();
        if (file.isEmpty()) {
            restResult.setStatus(400);
            restResult.setMessage("请指定待上传的文件");
            return restResult;
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            System.out.println(file.getOriginalFilename());
            Path path = Paths.get(file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @IgnoreClientToken
    @IgnoreUserToken
    @RequestMapping(value = "/downLoads", method = RequestMethod.GET)
    @ApiOperation("生成文书实例，并返回文书实例对应的word文档")
    public ResponseEntity<?> downLoadTemplate(
        @RequestParam(value = "id", required = false) @ApiParam(value = "待下载文书模板的ID") Integer id,
        @RequestParam(value = "tcode", required = false) @ApiParam(value = "待下载文书模板的code") String tcode,
        @RequestParam(value = "isOrigin", defaultValue = "false") @ApiParam(value = "为true表示下载的为原始模板") Boolean isOrigin,
        HttpServletResponse response) {
        log.debug("文书模板下载");

        WritsTemplates writsTemplates = new WritsTemplates();
        writsTemplates.setId(id);
        writsTemplates.setTcode(tcode);

        Result<WritsTemplates> result = null;
        if (isOrigin) {
            result = this.baseBiz.downLoadTemplate(writsTemplates);
        } else {
        }

        if (!result.getIsSuccess()) {
            throw new RuntimeException(result.getMessage());
        }

        WritsTemplates templates = result.getData();
        String originDocUrl = templates.getOriginDocUrl();

        // 中文乱码问题尚未解决，为使前端不报错，暂时使用UUID
        String templatesName =
            UUIDUtils.generateUuid() + originDocUrl.substring(originDocUrl.lastIndexOf("."));
//        String templatesName =
//            originDocUrl.substring(originDocUrl.lastIndexOf("/")+1);

        log.debug("文书模板路径为："+templatesName);
        // 设置响应头为响应一个word文档
        response.setContentType(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=" + templatesName);

        ResponseEntity<?> file = docDownUtil.getFile(originDocUrl);
        return file;
    }
}