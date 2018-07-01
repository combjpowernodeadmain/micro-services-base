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

package com.bjzhianjia.scp.codingapi.tx.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tx.control.service.TransactionControlService;
import com.bjzhianjia.scp.codingapi.tx.framework.utils.IpAddressUtils;
import com.bjzhianjia.scp.codingapi.tx.framework.utils.SocketManager;
import com.bjzhianjia.scp.codingapi.tx.listener.service.ModelNameService;
import com.bjzhianjia.scp.codingapi.tx.netty.service.MQTxManagerService;
import com.bjzhianjia.scp.codingapi.tx.netty.service.NettyControlService;
import com.bjzhianjia.scp.codingapi.tx.netty.service.NettyService;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class NettyControlServiceImpl implements NettyControlService {



    @Autowired
    private NettyService nettyService;


    @Autowired
    private TransactionControlService transactionControlService;


    @Autowired
    private MQTxManagerService mqTxManagerService;

    @Autowired
    private ModelNameService modelNameService;


    private Executor threadPool = Executors.newFixedThreadPool(100);


    @Override
    public void restart() {
        nettyService.close();
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nettyService.start();
    }

    @Override
    public void uploadModelInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!SocketManager.getInstance().isNetState()|| !IpAddressUtils.isIpAddress(modelNameService.getIpAddress())) {
                    try {
                        Thread.sleep(1000 * 5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mqTxManagerService.uploadModelInfo();
            }
        }).start();
    }

    @Override
    public void executeService(final ChannelHandlerContext ctx,final String json) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isNotEmpty(json)) {
                    JSONObject resObj = JSONObject.parseObject(json);
                    if (resObj.containsKey("a")) {
                        // tm发送数据给tx模块的处理指令

                        transactionControlService.notifyTransactionMsg(ctx,resObj,json);
                    }else{
                        //tx发送数据给tm的响应返回数据

                        String key = resObj.getString("k");
                        responseMsg(key,resObj);
                    }
                }
            }
        });
    }


    private void responseMsg(String key, JSONObject resObj) {
        if (!"h".equals(key)) {
            final String data = resObj.getString("d");
            Task task = ConditionUtils.getInstance().getTask(key);
            if (task != null) {
                if (task.isAwait()) {
                    task.setBack(new IBack() {
                        @Override
                        public Object doing(Object... objs) throws Throwable {
                            return data;
                        }
                    });
                    task.signalTask();
                }
            }
        } else {
            //心跳数据
            final String data = resObj.getString("d");
            SocketManager.getInstance().setNetState(true);
            if (StringUtils.isNotEmpty(data)) {
                try {
                    SocketManager.getInstance().setDelay(Integer.parseInt(data));
                } catch (Exception e) {
                    SocketManager.getInstance().setDelay(1);
                }
            }
        }
    }
}
