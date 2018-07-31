package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.cgp.biz.RegulaObjectTypeBiz;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.VhclManagement;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectTypeMapper;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

	private RegulaObjectTypeMapper regulaObjectTypeMapper;


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<RegulaObjectType> add(@RequestBody @Validated RegulaObjectType regulaObjectType, BindingResult bindingResult){

        ObjectRestResponse<RegulaObjectType> restResult = new ObjectRestResponse<>();

        if(bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        regulaObjectTypeMapper.insertSelective(regulaObjectType);

        return restResult.data(regulaObjectType);
    }

}

