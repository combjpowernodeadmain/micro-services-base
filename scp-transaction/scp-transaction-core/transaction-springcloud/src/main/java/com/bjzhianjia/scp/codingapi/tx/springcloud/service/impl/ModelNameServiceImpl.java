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

package com.bjzhianjia.scp.codingapi.tx.springcloud.service.impl;

import com.bjzhianjia.scp.codingapi.tx.listener.service.ModelNameService;
import com.bjzhianjia.scp.codingapi.tx.springcloud.listener.ServerListener;
import com.lorne.core.framework.utils.encode.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lorne on 2017/7/12.
 */
@Service
@Configuration
public class ModelNameServiceImpl implements ModelNameService {

    @Value("${spring.application.name}")
    private String modelName;

    @Autowired
    private ServerListener serverListener;


    private String host = null;

    @Override
    public String getModelName() {
        return modelName;
    }

    private String getIp() {
        if (host == null) {
            try {
                host = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return host;
    }

    private int getPort() {
        int port = serverListener.getPort();
        int count = 0;
        while (port == 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            port = serverListener.getPort();
            count++;

            if(count==2000){
                throw new RuntimeException("get server port error.");
            }
        }

        return port;
    }

    @Override
    public String getUniqueKey() {
        String address = getIp() + getPort();
        return MD5Util.md5(address.getBytes());
    }


    @Override
    public String getIpAddress() {
        String address = getIp() + ":" + getPort();
        return address;
    }
}
