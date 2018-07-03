/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.security.auth.module.client.biz;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.security.auth.module.client.entity.GatewayRoute;
import com.bjzhianjia.scp.security.auth.module.client.mapper.GatewayRouteMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.constant.RedisKeyConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.AG
 * @version 1.0 
 * @email 576866311@qq.com
 */
@Service
public class GatewayRouteBiz extends BusinessBiz<GatewayRouteMapper, GatewayRoute> {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void updateSelectiveById(GatewayRoute entity) {
        super.updateSelectiveById(entity);
        updateGatewayRoute();
    }

    @Override
    public void insertSelective(GatewayRoute entity) {
        super.insertSelective(entity);
        updateGatewayRoute();
    }

    @Override
    public void deleteById(Object id) {
        GatewayRoute gatewayRoute = this.selectById(id);
        gatewayRoute.setEnabled(false);
//        super.deleteById(id);
        this.updateSelectiveById(gatewayRoute);
    }

    @Override
    public void updateById(GatewayRoute entity) {
        super.updateById(entity);
        updateGatewayRoute();
    }

    public void updateGatewayRoute() {
        List<GatewayRoute> gatewayRoutes = this.selectListAll();
        redisTemplate.opsForValue().set(RedisKeyConstants.ZUUL_ROUTE_KEY, JSON.toJSONString(gatewayRoutes));
    }


}