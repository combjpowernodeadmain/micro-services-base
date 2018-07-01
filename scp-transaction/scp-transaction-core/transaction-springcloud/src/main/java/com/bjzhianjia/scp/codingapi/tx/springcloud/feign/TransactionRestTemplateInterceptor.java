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

package com.bjzhianjia.scp.codingapi.tx.springcloud.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxTransactionLocal;

/**
 * Created by lorne on 2017/6/26.
 */
public class TransactionRestTemplateInterceptor implements RequestInterceptor {


    private Logger logger = LoggerFactory.getLogger(TransactionRestTemplateInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        String groupId = txTransactionLocal == null ? null : txTransactionLocal.getGroupId();
        int maxTimeOut = txTransactionLocal == null ? 0 : txTransactionLocal.getMaxTimeOut();

        logger.info("LCN-SpringCloud TxGroup info -> groupId:"+groupId+",maxTimeOut:"+maxTimeOut);

        if (txTransactionLocal != null) {
            requestTemplate.header("tx-group", groupId);
            requestTemplate.header("tx-maxTimeOut", String.valueOf(maxTimeOut));
        }
    }

}
