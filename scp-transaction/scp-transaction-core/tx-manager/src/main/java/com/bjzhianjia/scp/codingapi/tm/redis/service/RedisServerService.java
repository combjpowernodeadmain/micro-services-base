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

package com.bjzhianjia.scp.codingapi.tm.redis.service;

import java.util.List;

import com.bjzhianjia.scp.codingapi.tm.netty.model.TxGroup;

/**
 * create by lorne on 2017/11/11
 */
public interface RedisServerService {

    String loadNotifyJson();

    void saveTransaction(String key, String json);

    TxGroup getTxGroupByKey(String key);

    void saveCompensateMsg(String name, String json);

    List<String> getKeys(String key);

    List<String> getValuesByKeys(List<String> keys);

    String getValueByKey(String key);

    void deleteKey(String key);

    void saveLoadBalance(String groupName,String key,String data);


    String getLoadBalance(String groupName,String key);


}
