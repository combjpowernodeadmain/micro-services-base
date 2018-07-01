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

package com.bjzhianjia.scp.codingapi.ribbon.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * created by foxdd 2017-12-05
 */
public class LcnZoneAwareLoadBalancerProxy extends ZoneAwareLoadBalancer<Server> {
	
	private Logger logger = LoggerFactory.getLogger(LcnZoneAwareLoadBalancerProxy.class);
	
	LcnLoadBalancerRule lcnLoadBalancerRule = new LcnLoadBalancerRule();
	
	public LcnZoneAwareLoadBalancerProxy(IClientConfig clientConfig, IRule rule,
            IPing ping, ServerList<Server> serverList, ServerListFilter<Server> filter,
            ServerListUpdater serverListUpdater) {
		super(clientConfig, rule, ping, serverList, filter, serverListUpdater);
	}

	@Override
	public Server chooseServer(Object key){
		logger.info("enter chooseServer method, key:" + key);
        List<Server> serverList = super.getReachableServers();
        if(null == serverList || serverList.isEmpty()){
		    return super.chooseServer(key);
        }
		return lcnLoadBalancerRule.proxy(serverList, super.chooseServer(key));

	}

}
