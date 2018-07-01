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

package com.bjzhianjia.scp.codingapi.tx.framework.task;

/**
 * create by lorne on 2017/12/21
 */
public enum TaskState {

    rollback(0),commit(1),networkError(-1),networkTimeOut(-2),connectionError(-3);


    /**
     *  数据状态：
     *  1:commit
     *  0:rollback
     *  -1:network error
     *  -2:network time out
     *  -3:execute Connection error
     * @return state
     */

    private int code;

    TaskState(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }
}
