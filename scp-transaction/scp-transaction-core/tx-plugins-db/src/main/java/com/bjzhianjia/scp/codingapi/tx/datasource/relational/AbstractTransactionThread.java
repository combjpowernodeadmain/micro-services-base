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

package com.bjzhianjia.scp.codingapi.tx.datasource.relational;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bjzhianjia.scp.codingapi.tx.framework.thread.HookRunnable;

import java.sql.SQLException;

/**
 * create by lorne on 2017/12/1
 */
public abstract class AbstractTransactionThread {

    private volatile boolean hasStartTransaction = false;

    private Logger logger = LoggerFactory.getLogger(AbstractTransactionThread.class);

    protected void startRunnable(){
        if(hasStartTransaction){
            logger.info("start connection is wait ! ");
            return;
        }
        hasStartTransaction = true;
        Runnable runnable = new HookRunnable() {
            @Override
            public void run0() {
                try {
                    transaction();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    try {
                        rollbackConnection();
                    } catch (SQLException e1) {
                        logger.error(e1.getMessage());
                    }
                } finally {
                    try {
                        closeConnection();
                    } catch (SQLException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    protected abstract void transaction() throws SQLException;

    protected abstract void closeConnection() throws SQLException;

    protected abstract void rollbackConnection() throws SQLException;
}
