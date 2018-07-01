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

package com.bjzhianjia.scp.codingapi.tm.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.codingapi.tm.config.ConfigReader;
import com.bjzhianjia.scp.codingapi.tm.manager.service.LoadBalanceService;
import com.bjzhianjia.scp.codingapi.tm.model.LoadBalanceInfo;
import com.bjzhianjia.scp.codingapi.tm.redis.service.RedisServerService;

/**
 * create by lorne on 2017/12/5
 */
@Service
public class LoadBalanceServiceImpl implements LoadBalanceService {

    @Autowired
    private RedisServerService redisServerService;

    @Autowired
    private ConfigReader configReader;

    @Override
    public boolean put(LoadBalanceInfo loadBalanceInfo) {
        String groupName = getLoadBalanceGroupName(loadBalanceInfo.getGroupId());
        redisServerService.saveLoadBalance(groupName,loadBalanceInfo.getKey(),loadBalanceInfo.getData());
        return true;
    }

    @Override
    public LoadBalanceInfo get(String groupId, String key) {
        String groupName = getLoadBalanceGroupName(groupId);
        String bytes = redisServerService.getLoadBalance(groupName,key);
        if(bytes==null) {
            return null;
        }

        LoadBalanceInfo loadBalanceInfo = new LoadBalanceInfo();
        loadBalanceInfo.setGroupId(groupId);
        loadBalanceInfo.setKey(key);
        loadBalanceInfo.setData(bytes);
        return loadBalanceInfo;
    }

    @Override
    public boolean remove(String groupId) {
        redisServerService.deleteKey(getLoadBalanceGroupName(groupId));
        return true;
    }

    @Override
    public String getLoadBalanceGroupName(String groupId) {
        return configReader.getKeyPrefixLoadbalance()+groupId;
    }
}
