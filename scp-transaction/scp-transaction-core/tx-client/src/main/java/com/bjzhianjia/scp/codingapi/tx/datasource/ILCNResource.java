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

import com.bjzhianjia.scp.codingapi.tx.framework.task.TxTask;

/**
 *
 * LCN 资源连接对象
 *
 *
 * create by lorne on 2017/8/22
 */
public interface ILCNResource<T> {


    /**
     * 用于关闭时检查是否未删除
     * @return TxTask任务对象
     */
    TxTask getWaitTask();

    /**
     * 事务组id
     * @return  事务组Id
     */
    String getGroupId();



}
