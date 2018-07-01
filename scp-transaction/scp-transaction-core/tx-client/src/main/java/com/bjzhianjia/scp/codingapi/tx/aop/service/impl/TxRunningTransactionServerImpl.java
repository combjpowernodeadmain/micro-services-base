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

package com.bjzhianjia.scp.codingapi.tx.aop.service.impl;

import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxTransactionInfo;
import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxTransactionLocal;
import com.bjzhianjia.scp.codingapi.tx.aop.service.TransactionServer;
import com.bjzhianjia.scp.codingapi.tx.datasource.ILCNTransactionControl;
import com.bjzhianjia.scp.codingapi.tx.framework.task.TaskGroupManager;
import com.bjzhianjia.scp.codingapi.tx.framework.task.TxTask;
import com.bjzhianjia.scp.codingapi.tx.model.TxGroup;
import com.bjzhianjia.scp.codingapi.tx.netty.service.MQTxManagerService;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.KidUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式事务启动参与事务中的业务处理
 * Created by lorne on 2017/6/8.
 */
@Service(value = "txRunningTransactionServer")
public class TxRunningTransactionServerImpl implements TransactionServer {


    @Autowired
    private MQTxManagerService txManagerService;


    @Autowired
    private ILCNTransactionControl transactionControl;


    private Logger logger = LoggerFactory.getLogger(TxRunningTransactionServerImpl.class);

    @Override
    public Object execute(final ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {

        String kid = KidUtils.generateShortUuid();
        String txGroupId = info.getTxGroupId();
        logger.info("--->begin running transaction,groupId:" + txGroupId);
        long t1 = System.currentTimeMillis();

        boolean isHasIsGroup =  transactionControl.hasGroup(txGroupId);


        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(txGroupId);
        txTransactionLocal.setHasStart(false);
        txTransactionLocal.setKid(kid);
        txTransactionLocal.setHasIsGroup(isHasIsGroup);
        txTransactionLocal.setMaxTimeOut(info.getMaxTimeOut());
        TxTransactionLocal.setCurrent(txTransactionLocal);


        try {

            Object res = point.proceed();

            //写操作 处理
            if(!txTransactionLocal.isReadOnly()) {

                String methodStr = info.getInvocation().getMethodStr();

                TxGroup resTxGroup = txManagerService.addTransactionGroup(txGroupId, kid, isHasIsGroup, methodStr);

                //已经进入过该模块的，不再执行此方法
                if(!isHasIsGroup) {
                    String type = txTransactionLocal.getType();

                    TxTask waitTask = TaskGroupManager.getInstance().getTask(kid, type);

                    //lcn 连接已经开始等待时.
                    while (waitTask != null && !waitTask.isAwait()) {
                        TimeUnit.MILLISECONDS.sleep(1);
                    }

                    if (resTxGroup == null) {

                        //通知业务回滚事务
                        if (waitTask != null) {
                            //修改事务组状态异常
                            waitTask.setState(-1);
                            waitTask.signalTask();
                            throw new ServiceException("update TxGroup error, groupId:" + txGroupId);
                        }
                    }
                }
            }

            return res;
        } catch (Throwable e) {
            throw e;
        } finally {
            TxTransactionLocal.setCurrent(null);
            long t2 = System.currentTimeMillis();
            logger.info("<---end running transaction,groupId:" + txGroupId+",execute time:"+(t2-t1));

        }
    }

}
