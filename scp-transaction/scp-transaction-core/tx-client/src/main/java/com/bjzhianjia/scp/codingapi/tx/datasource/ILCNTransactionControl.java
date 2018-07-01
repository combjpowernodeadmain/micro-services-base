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


/**
 * LCN 代理事务协调控制
 * create by lorne on 2017/9/6
 */
public interface ILCNTransactionControl {

    /**
     * 是否是同一个事务下
     * @param group 事务组id
     * @return  true是，false否
     */
    boolean hasGroup(String group);

    /**
     * 是否是 事务操作
     * @return true是，false否
     */
    boolean hasTransaction();
}
