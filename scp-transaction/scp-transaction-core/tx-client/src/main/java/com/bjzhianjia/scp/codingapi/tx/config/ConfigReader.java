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

package com.bjzhianjia.scp.codingapi.tx.config;

import com.bjzhianjia.scp.codingapi.tx.config.service.TxManagerTxUrlService;
import com.lorne.core.framework.utils.config.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/13
 */
@Component
public class ConfigReader {


    private Logger logger = LoggerFactory.getLogger(ConfigReader.class);


    private TxManagerTxUrlService txManagerTxUrlService;

    @Autowired
    private ApplicationContext spring;


    public String getTxUrl() {

        try {
            txManagerTxUrlService =  spring.getBean(TxManagerTxUrlService.class);
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }

        if(txManagerTxUrlService == null){
            txManagerTxUrlService = new TxManagerTxUrlService() {

                private final String configName = "tx.properties";

                private final String configKey = "url";

                @Override
                public String getTxUrl() {
                    return ConfigUtils.getString(configName,configKey);
                }
            };

            logger.info("load default txManagerTxUrlService");
        }else{
            logger.info("load txManagerTxUrlService");
        }

        return txManagerTxUrlService.getTxUrl();
    }


}
