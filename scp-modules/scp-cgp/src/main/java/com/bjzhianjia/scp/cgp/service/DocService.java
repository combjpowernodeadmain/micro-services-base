package com.bjzhianjia.scp.cgp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.constances.WritsConstances;
import com.bjzhianjia.scp.cgp.util.DocUtil;

/**
 * @author 尚
 */
@Component
public class DocService {
    @Autowired
    private PropertiesConfig propertiesConfig;

    @Scheduled(cron = "0 0 2 * * ?")
    public void deletePrefix() {
        DocUtil.deletePrefix(WritsConstances.WRITS_PREFFIX, WritsConstances.WRITS_SUFFIX,
            propertiesConfig.getDestFilePath());
    }
}
