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

package com.bjzhianjia.scp.codingapi.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tx.compensate.service.CompensateService;
import com.bjzhianjia.scp.codingapi.tx.control.service.IActionService;
import com.bjzhianjia.scp.codingapi.tx.framework.utils.SerializerUtils;
import com.bjzhianjia.scp.codingapi.tx.model.TransactionInvocation;
import com.lorne.core.framework.utils.encode.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知补偿
 * create by lorne on 2017/11/13
 */
@Service(value = "c")
public class ActionCServiceImpl implements IActionService {


    private Logger logger = LoggerFactory.getLogger(ActionCServiceImpl.class);


    @Autowired
    private CompensateService compensateService;

    @Override
    public String execute(JSONObject resObj, String json) {

        String cmd = resObj.toJSONString();

        logger.info("accept compensate data ->" + cmd);


        String data = resObj.getString("d");

        String groupId = resObj.getString("g");

        int startState = resObj.getInteger("ss");

        byte[] bytes = Base64Utils.decode(data);

        TransactionInvocation invocation = SerializerUtils.parserTransactionInvocation(bytes);

        if (invocation != null) {
            logger.info("compensate method ->" + invocation.getMethodStr());

            boolean res = compensateService.invoke(invocation, groupId,startState);

            logger.info("compensate res ->" + res);

            if (res) {
                return "1";
            } else {
                return "0";
            }
        }

        return "-1";
    }


}
