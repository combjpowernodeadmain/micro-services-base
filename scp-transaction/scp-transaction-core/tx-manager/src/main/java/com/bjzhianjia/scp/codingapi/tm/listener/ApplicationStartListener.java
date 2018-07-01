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

package com.bjzhianjia.scp.codingapi.tm.listener;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bjzhianjia.scp.codingapi.tm.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * create by lorne on 2017/8/7
 */
@Component
public class ApplicationStartListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {


    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        int serverPort = event.getEmbeddedServletContainer().getPort();
        String ip = getIp();
        Constants.address = ip+":"+serverPort;
    }



    private String getIp(){
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return host;
    }
}
