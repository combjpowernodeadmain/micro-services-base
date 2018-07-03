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

package com.bjzhianjia.scp.security.auth.module.client.biz;

import com.bjzhianjia.scp.security.auth.module.client.entity.Client;
import com.bjzhianjia.scp.security.auth.module.client.entity.ClientService;
import com.bjzhianjia.scp.security.auth.module.client.mapper.ClientMapper;
import com.bjzhianjia.scp.security.auth.module.client.mapper.ClientServiceMapper;
import com.bjzhianjia.scp.security.common.biz.BaseBiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 
 *
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
@Service
public class ClientBiz extends BaseBiz<ClientMapper,Client> {
    @Autowired
    private ClientServiceMapper clientServiceMapper;
    @Autowired
    private ClientServiceBiz clientServiceBiz;

    public List<Client> getClientServices(String id) {
        return mapper.selectAuthorityServiceInfo(id);
    }

    public void modifyClientServices(String id, String clients) {
        clientServiceMapper.deleteByServiceId(id);
        if (!StringUtils.isEmpty(clients)) {
            String[] mem = clients.split(",");
            for (String m : mem) {
                ClientService clientService = new ClientService();
                clientService.setServiceId(m);
                clientService.setClientId(id+"");
                clientServiceBiz.insertSelective(clientService);
            }
        }
    }
}
