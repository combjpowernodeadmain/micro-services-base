package com.bjzhianjia.scp.merge.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *
 * @author scp
 * @date 2017/7/28
 */
@EnableEurekaClient
@SpringBootApplication
public class ProviderBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ProviderBootstrap.class, args);
    }
}
