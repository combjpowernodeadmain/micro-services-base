package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.StaffInfoCollectionBiz;
import com.bjzhianjia.scp.cgp.entity.StaffInfoCollection;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.Map;

@RestController
@RequestMapping("staffInfoCollection")
//@CheckClientToken
//@CheckUserToken
public class StaffInfoCollectionController extends BaseController<StaffInfoCollectionBiz, StaffInfoCollection, Integer> {
    /**
     * 人员信息采集
     * 该接口可在不进行登录情况下进行调用
     *
     * @param user
     * @return
     */
    @PostMapping("/i/info")
    @ApiOperation("人员信息采集")
    public ObjectRestResponse<Void> userInfoCollect(@RequestBody StaffInfoCollection staffInfoCollection) {
        return this.baseBiz.userInfoCollect(staffInfoCollection);
    }


    @GetMapping("/i/list")
    @ApiOperation("/获取信息采集列表")
    public TableResultResponse<StaffInfoCollection> getStaffInfoCollectionList(
            @RequestParam(value = "staffName",defaultValue = "")String staffName,
            @RequestParam(value = "mibilePhone",defaultValue = "")String mibilePhone,
            @RequestParam(value = "terminalPhone",defaultValue = "")String terminalPhone,
            @RequestParam(value = "departId",defaultValue = "")String departId,
            @RequestParam(value = "phoneCode",defaultValue = "")String phoneCode,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "limit",defaultValue = "10")Integer limit
    ) {
        StaffInfoCollection info=new StaffInfoCollection();
        info.setStaffName(staffName);
        info.setMibilePhone(mibilePhone);
        info.setTerminalPhone(terminalPhone);
        info.setDepartId(departId);
        info.setPhoneCode(phoneCode);

        return this.baseBiz.getStaffInfoCollectionList(info,page,limit);
    }
}