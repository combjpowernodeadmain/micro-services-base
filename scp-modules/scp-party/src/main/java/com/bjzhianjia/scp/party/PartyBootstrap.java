package com.bjzhianjia.scp.party;

import com.bjzhianjia.scp.merge.EnableAceMerge;
import com.bjzhianjia.scp.security.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author scp
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
// 开启事务
@EnableTransactionManagement
// 开启熔断监控
@EnableCircuitBreaker
// 开启服务鉴权
@EnableFeignClients({"com.bjzhianjia.scp.security.auth.client.feign", "com.bjzhianjia.scp.party.feign"})
@MapperScan("com.bjzhianjia.scp.party.mapper")
@EnableAceAuthClient
@EnableAceMerge
@EnableSwagger2Doc
@EnableScheduling
public class PartyBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(PartyBootstrap.class).web(true).run(args);
    }
}
