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

package com.bjzhianjia.scp.codingapi.tx.aop.bean;

import com.bjzhianjia.scp.codingapi.tx.annotation.TxTransaction;
import com.bjzhianjia.scp.codingapi.tx.model.TransactionInvocation;


/**
 * 切面控制对象
 * Created by lorne on 2017/6/8.
 */
public class TxTransactionInfo {


    private TxTransaction transaction;


    private TxTransactionLocal txTransactionLocal;

    /**
     * 事务组Id
     */
    private String txGroupId;

    /**
     * 最大超时时间（发起方模块的）
     */
    private int maxTimeOut;


    private TransactionInvocation invocation;


    public TxTransactionInfo(TxTransaction transaction, TxTransactionLocal txTransactionLocal, TransactionInvocation invocation, String txGroupId, int maxTimeOut) {
        this.transaction = transaction;
        this.txTransactionLocal = txTransactionLocal;
        this.txGroupId = txGroupId;
        this.maxTimeOut = maxTimeOut;
        this.invocation = invocation;
    }

    public int getMaxTimeOut() {
        return maxTimeOut;
    }



    public TxTransaction getTransaction() {
        return transaction;
    }

    public TxTransactionLocal getTxTransactionLocal() {
        return txTransactionLocal;
    }

    public String getTxGroupId() {
        return txGroupId;
    }

    public TransactionInvocation getInvocation() {
        return invocation;
    }

}
