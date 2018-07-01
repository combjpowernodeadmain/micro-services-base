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

package com.bjzhianjia.scp.codingapi.tx.datasource.service.impl;

import com.bjzhianjia.scp.codingapi.tx.datasource.service.DataSourceService;
import com.bjzhianjia.scp.codingapi.tx.netty.service.MQTxManagerService;
import com.lorne.core.framework.utils.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/7/29
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {


    @Autowired
    private MQTxManagerService txManagerService;


    @Override
    public void schedule(String groupId, Task waitTask) {


        String waitTaskId = waitTask.getKey();
        int rs = txManagerService.cleanNotifyTransaction(groupId, waitTaskId);
        if (rs == 1 || rs == 0) {
            waitTask.setState(rs);
            waitTask.signalTask();

            return;
        }
        rs = txManagerService.cleanNotifyTransactionHttp(groupId, waitTaskId);
        if (rs == 1 || rs == 0) {
            waitTask.setState(rs);
            waitTask.signalTask();

            return;
        }

        //添加到补偿队列
        waitTask.setState(-100);
        waitTask.signalTask();

    }
}
