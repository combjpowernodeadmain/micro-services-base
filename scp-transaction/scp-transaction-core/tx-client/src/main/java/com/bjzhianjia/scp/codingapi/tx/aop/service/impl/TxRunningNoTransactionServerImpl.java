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
import com.lorne.core.framework.utils.KidUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 分布式事务启动参与事务中的业务处理（无事务模块）
 * Created by lorne on 2017/6/8.
 */
@Service(value = "txRunningNoTransactionServer")
public class TxRunningNoTransactionServerImpl implements TransactionServer {


    private Logger logger = LoggerFactory.getLogger(TxRunningNoTransactionServerImpl.class);

    @Override
    public Object execute(final ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {

        String kid = KidUtils.generateShortUuid();
        String txGroupId = info.getTxGroupId();
        logger.info("--->begin no db transaction, groupId: " + txGroupId);
        long t1 = System.currentTimeMillis();


        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(txGroupId);
        txTransactionLocal.setHasStart(false);
        txTransactionLocal.setKid(kid);
        txTransactionLocal.setMaxTimeOut(info.getMaxTimeOut());
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            return point.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            TxTransactionLocal.setCurrent(null);
            long t2 = System.currentTimeMillis();
            logger.info("<---end no db transaction,groupId:" + txGroupId+",execute time:"+(t2-t1));
        }
    }

}
