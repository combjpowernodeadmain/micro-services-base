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

package com.bjzhianjia.scp.codingapi.tx.datasource;


import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxTransactionLocal;
import com.bjzhianjia.scp.codingapi.tx.datasource.service.DataSourceService;
import com.lorne.core.framework.utils.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by lorne on 2017/8/22
 */

public abstract class AbstractResourceProxy<C,T extends ILCNResource> implements ILCNTransactionControl {


    protected Map<String, ILCNResource> pools = new ConcurrentHashMap<>();


    private Logger logger = LoggerFactory.getLogger(AbstractResourceProxy.class);


    @Autowired
    protected DataSourceService dataSourceService;


    //default size
    protected volatile int maxCount = 5;

    //default time (seconds)
    protected int maxWaitTime = 30;

    protected volatile int nowCount = 0;





    // not thread
    protected ICallClose<ILCNResource> subNowCount = new ICallClose<ILCNResource>() {

        @Override
        public void close(ILCNResource connection) {
            Task waitTask = connection.getWaitTask();
            if (waitTask != null) {
                if (!waitTask.isRemove()) {
                    waitTask.remove();
                }
            }

            pools.remove(connection.getGroupId());
            nowCount--;
        }
    };


    protected abstract C createLcnConnection(C connection, TxTransactionLocal txTransactionLocal);

    protected abstract void initDbType();

//    protected abstract C getCompensateConnection(C connection,TxCompensateLocal txCompensateLocal);
//


    protected ILCNResource loadConnection(){
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        if(txTransactionLocal==null){
            logger.info("loadConnection -> null !");
            return null;
        }

        //是否获取旧连接的条件：同一个模块下被多次调用时第一次的事务操作
        ILCNResource old = pools.get(txTransactionLocal.getGroupId());
        if (old != null) {

            if(txTransactionLocal.isHasConnection()){
                logger.info("connection is had , transaction get a new connection .");
                return null;
            }

            logger.info("loadConnection -> old !");
            txTransactionLocal.setHasConnection(true);
            return old;
        }
        return null;
    }


    private C createConnection(TxTransactionLocal txTransactionLocal, C connection){
        if (nowCount == maxCount) {
            for (int i = 0; i < maxWaitTime; i++) {
                for(int j=0;j<100;j++){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (nowCount < maxCount) {
                        return createLcnConnection(connection, txTransactionLocal);
                    }
                }
            }
        } else if (nowCount < maxCount) {
            return createLcnConnection(connection, txTransactionLocal);
        } else {
            logger.info("connection was overload");
            return null;
        }
        return connection;
    }



    protected C initLCNConnection(C connection) {
        logger.info("initLCNConnection");
        C lcnConnection = connection;
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        if (txTransactionLocal != null&&!txTransactionLocal.isHasConnection()) {

            logger.info("lcn datasource transaction control ");

            //补偿的情况的
//            if (TxCompensateLocal.current() != null) {
//                logger.info("rollback transaction ");
//                return getCompensateConnection(connection,TxCompensateLocal.current());
//            }

            if(StringUtils.isNotEmpty(txTransactionLocal.getGroupId())){

                logger.info("lcn transaction ");
                return createConnection(txTransactionLocal, connection);
            }
        }
        logger.info("load default connection !");
        return lcnConnection;
    }


    @Override
    public boolean hasGroup(String group){
        return pools.containsKey(group);
    }


    @Override
    public boolean hasTransaction() {
        return true;
    }


    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

}
