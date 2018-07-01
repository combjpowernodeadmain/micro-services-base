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

package com.bjzhianjia.scp.security.auth.module.client.service;


import java.util.List;

/**
 *
 * @author scp
 * @version 1.0
 */
public interface AuthClientService {
    /**
     * 获取服务鉴权token
     * @param clientId
     * @param secret
     * @return
     * @throws Exception
     */
    public String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     * @param serviceId
     * @param secret
     * @return
     */
    public List<String> getAllowedClient(String serviceId, String secret);

    /**
     * 获取服务授权的客户端列表
     * @param serviceId
     * @return
     */
    public List<String> getAllowedClient(String serviceId);

    /**
     * 校验合法性
     * @param clientId
     * @param secret
     * @throws Exception
     */
    public void validate(String clientId, String secret) throws Exception;
}
