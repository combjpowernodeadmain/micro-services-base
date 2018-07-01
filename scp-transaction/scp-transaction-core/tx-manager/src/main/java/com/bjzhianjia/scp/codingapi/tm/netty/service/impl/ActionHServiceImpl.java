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
import com.bjzhianjia.scp.codingapi.tm.config.ConfigReader;
import com.bjzhianjia.scp.codingapi.tm.manager.service.TxManagerService;
import com.bjzhianjia.scp.codingapi.tm.netty.service.IActionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 心跳包
 * create by lorne on 2017/11/11
 */
@Service(value = "h")
public class ActionHServiceImpl implements IActionService{


    @Autowired
    private ConfigReader configReader;

    @Override
    public String execute(String modelName,String key,JSONObject params ) {
        return String.valueOf(configReader.getTransactionNettyDelayTime());
    }
}
