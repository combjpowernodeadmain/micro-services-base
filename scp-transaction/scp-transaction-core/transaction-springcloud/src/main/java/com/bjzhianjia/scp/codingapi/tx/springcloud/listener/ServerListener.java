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

package com.bjzhianjia.scp.codingapi.tx.springcloud.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bjzhianjia.scp.codingapi.tx.listener.service.InitService;


@Component
public class ServerListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private Logger logger = LoggerFactory.getLogger(ServerListener.class);

    private int serverPort;

    @Autowired
    private InitService initService;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        logger.info("onApplicationEvent -> onApplicationEvent. "+event.getEmbeddedServletContainer());
        this.serverPort = event.getEmbeddedServletContainer().getPort();

        initService.start();
    }

    public int getPort() {
        return this.serverPort;
    }
}
