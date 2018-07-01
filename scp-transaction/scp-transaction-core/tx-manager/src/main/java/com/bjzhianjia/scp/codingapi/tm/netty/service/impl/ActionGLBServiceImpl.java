/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.bjzhianjia.scp.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tm.manager.service.LoadBalanceService;
import com.bjzhianjia.scp.codingapi.tm.model.LoadBalanceInfo;
import com.bjzhianjia.scp.codingapi.tm.netty.service.IActionService;
import com.lorne.core.framework.utils.encode.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取负载模块信息
 * create by lorne on 2017/11/11
 */
@Service(value = "glb")
public class ActionGLBServiceImpl implements IActionService{


    @Autowired
    private LoadBalanceService loadBalanceService;


    @Override
    public String execute(String modelName,String key,JSONObject params ) {
        String res;
        String groupId = params.getString("g");
        String k = params.getString("k");

        LoadBalanceInfo loadBalanceInfo =  loadBalanceService.get(groupId,k);
        if(loadBalanceInfo==null){
            res = "";
        }else{
            res = loadBalanceInfo.getData();
        }
        return res;
    }
}
