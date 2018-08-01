package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.cgp.biz.RegulaObjectTypeBiz;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.service.RegulaObjectTypeService;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mengfanguang  on 2018-07-31 14:19:12
 */
@RestController
@RequestMapping("/regula_object_type")
@Api(tags = "监管对象类型表")
public class RegulaObjectTypeController extends BaseController<RegulaObjectTypeBiz, RegulaObjectType, Integer> {

	@Autowired
    private RegulaObjectTypeService regulaObjectTypeService;


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个检测对象类别")
    public ObjectRestResponse<RegulaObjectType> add(@RequestBody @Validated RegulaObjectType regulaObjectType, BindingResult bindingResult){

        ObjectRestResponse<RegulaObjectType> restResult = new ObjectRestResponse<>();

        if(bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }
        regulaObjectTypeService.createRegulaObjectType(regulaObjectType);
        return restResult.data(regulaObjectType);
    }


    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("修改单检测个对象类别")
    public ObjectRestResponse<RegulaObjectType> update(@RequestBody @Validated RegulaObjectType regulaObjectType, BindingResult bindingResult){

        ObjectRestResponse<RegulaObjectType> restResult = new ObjectRestResponse<>();

        if(bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }
        int result = regulaObjectTypeService.updateRegulaObject(regulaObjectType);
        if(result>0){
            return restResult.data(regulaObjectType);
        }else{
            restResult.setStatus(400);
            restResult.setMessage("系统异常");
            return  restResult;
        }

    }


}

