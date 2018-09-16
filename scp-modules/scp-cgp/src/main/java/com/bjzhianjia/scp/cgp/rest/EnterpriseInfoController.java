package com.bjzhianjia.scp.cgp.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.EnterpriseInfoBiz;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("enterpriseInfo")
@CheckClientToken
@CheckUserToken
public class EnterpriseInfoController extends BaseController<EnterpriseInfoBiz,EnterpriseInfo,Integer> {

    @RequestMapping(value="/instance/regObjId",method=RequestMethod.GET)
    @ApiOperation("按监管对象获取单个记录")
    public ObjectRestResponse<EnterpriseInfo> getEnterpriseInfo(@RequestParam("regObjId") @ApiParam(value="待查询对象ID") Integer regObjId){
        ObjectRestResponse<EnterpriseInfo> restResult=new ObjectRestResponse<>();
        
        Map<String, Object> conditions=new HashMap<>();
        conditions.put("regulaObjId", regObjId);
        List<EnterpriseInfo> mapResult = this.baseBiz.getByMap(conditions);
        
        if(BeanUtil.isNotEmpty(mapResult)) {
            if(mapResult.size()>1) {
                restResult.setStatus(400);
                restResult.setMessage("某特定监管对象下只能对应一个企业信息");
                return restResult;
            }
            restResult.setData(mapResult.get(0));
            restResult.setMessage("成功");
            restResult.setStatus(200);
            return restResult;
        }
        return restResult;
    }
}