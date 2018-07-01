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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.codingapi.tx.annotation.TxTransaction;
import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxTransactionInfo;
import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxTransactionLocal;
import com.bjzhianjia.scp.codingapi.tx.aop.service.AspectBeforeService;
import com.bjzhianjia.scp.codingapi.tx.aop.service.TransactionServer;
import com.bjzhianjia.scp.codingapi.tx.aop.service.TransactionServerFactoryService;
import com.bjzhianjia.scp.codingapi.tx.model.TransactionInvocation;

import java.lang.reflect.Method;

/**
 * Created by lorne on 2017/7/1.
 */
@Service
public class AspectBeforeServiceImpl implements AspectBeforeService {

    @Autowired
    private TransactionServerFactoryService transactionServerFactoryService;


    private Logger logger = LoggerFactory.getLogger(AspectBeforeServiceImpl.class);


    public Object around(String groupId,int maxTimeOut, ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        Object[] args = point.getArgs();
        Method thisMethod = clazz.getMethod(method.getName(), method.getParameterTypes());

        TxTransaction transaction = thisMethod.getAnnotation(TxTransaction.class);

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        logger.info("around--> groupId-> " +groupId+",txTransactionLocal->"+txTransactionLocal);

        TransactionInvocation invocation = new TransactionInvocation(clazz, thisMethod.getName(), thisMethod.toString(), args, method.getParameterTypes());

        TxTransactionInfo info = new TxTransactionInfo(transaction,txTransactionLocal,invocation,groupId,maxTimeOut);

        TransactionServer server = transactionServerFactoryService.createTransactionServer(info);

        return server.execute(point, info);
    }
}
