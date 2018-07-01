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

package com.bjzhianjia.scp.codingapi.tm.api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.codingapi.tm.api.service.ApiTxManagerService;
import com.bjzhianjia.scp.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.bjzhianjia.scp.codingapi.tm.compensate.service.CompensateService;
import com.bjzhianjia.scp.codingapi.tm.config.ConfigReader;
import com.bjzhianjia.scp.codingapi.tm.manager.service.MicroService;
import com.bjzhianjia.scp.codingapi.tm.manager.service.TxManagerSenderService;
import com.bjzhianjia.scp.codingapi.tm.manager.service.TxManagerService;
import com.bjzhianjia.scp.codingapi.tm.model.TxServer;
import com.bjzhianjia.scp.codingapi.tm.model.TxState;

/**
 * Created by lorne on 2017/7/1.
 */
@Service
public class ApiTxManagerServiceImpl implements ApiTxManagerService {


    @Autowired
    private TxManagerService managerService;

    @Autowired
    private MicroService eurekaService;

    @Autowired
    private CompensateService compensateService;


    @Autowired
    private TxManagerSenderService txManagerSenderService;

    @Autowired
    private ConfigReader configReader;


    @Override
    public TxServer getServer() {
        return eurekaService.getServer();
    }


    @Override
    public int cleanNotifyTransaction(String groupId, String taskId) {
        return managerService.cleanNotifyTransaction(groupId,taskId);
    }


    @Override
    public boolean sendCompensateMsg(long currentTime, String groupId, String model, String address, String uniqueKey, String className, String methodStr, String data, int time,int startError) {
        TransactionCompensateMsg transactionCompensateMsg = new TransactionCompensateMsg(currentTime, groupId, model, address, uniqueKey, className, methodStr, data, time, 0,startError);
        return compensateService.saveCompensateMsg(transactionCompensateMsg);
    }

    @Override
    public String sendMsg(String model,String msg) {
        return txManagerSenderService.sendMsg(model, msg, configReader.getTransactionNettyDelayTime());
    }


    @Override
    public TxState getState() {
        return eurekaService.getState();
    }
}
