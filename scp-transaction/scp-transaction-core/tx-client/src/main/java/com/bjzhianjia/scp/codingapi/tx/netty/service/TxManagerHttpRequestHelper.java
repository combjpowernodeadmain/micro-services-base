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

package com.bjzhianjia.scp.codingapi.tx.netty.service;


import com.lorne.core.framework.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/17
 */
@Component
public class TxManagerHttpRequestHelper {


    private TxManagerHttpRequestService httpRequestService;

    @Autowired
    private ApplicationContext spring;

    private Logger logger = LoggerFactory.getLogger(TxManagerHttpRequestHelper.class);


    private void reloadHttpRequestService(){
        try {
            httpRequestService = spring.getBean(TxManagerHttpRequestService.class);
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }

        if(httpRequestService==null){
            httpRequestService = new TxManagerHttpRequestService() {
                @Override
                public String httpGet(String url) {
                    return HttpUtils.get(url);
                }

                @Override
                public String httpPost(String url, String params) {
                    return HttpUtils.post(url, params);
                }
            };
            logger.info("load default HttpRequestService .");
        }else {
            logger.info("load HttpRequestService .");
        }
    }

    public String httpGet(String url) {
        reloadHttpRequestService();
        return httpRequestService.httpGet(url);
    }

    public String httpPost(String url, String params) {
        reloadHttpRequestService();
        return httpRequestService.httpPost(url,params);
    }


}
