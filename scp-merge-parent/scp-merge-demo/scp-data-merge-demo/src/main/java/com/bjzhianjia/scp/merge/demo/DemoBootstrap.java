package com.bjzhianjia.scp.merge.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.bjzhianjia.scp.merge.EnableAceMerge;

/**
 *
 * @author scp
 * @date 2017/7/28
 */
@EnableEurekaClient
@EnableFeignClients("com.bjzhianjia.scp.ace.merge.demo.feign")
@EnableAceMerge
@SpringBootApplication
public class DemoBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(DemoBootstrap.class, args);
    }
}
