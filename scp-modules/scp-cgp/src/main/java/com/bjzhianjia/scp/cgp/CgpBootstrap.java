package com.bjzhianjia.scp.cgp;

import com.bjzhianjia.scp.merge.EnableAceMerge;
import com.bjzhianjia.scp.security.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;

import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author scp
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication(exclude= {
		DataSourceAutoConfiguration.class, //关闭默认数据源
		//activiti中默认引入了spring security认证,这里不需要所以禁用掉
		org.activiti.spring.boot.SecurityAutoConfiguration.class, 
		org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class,
		})
// 开启事务
@EnableTransactionManagement
// 开启熔断监控
@EnableCircuitBreaker
// 开启服务鉴权
@EnableFeignClients({"com.bjzhianjia.scp.security.auth.client.feign","com.bjzhianjia.scp.cgp.feign"})
@ComponentScan({"com.bjzhianjia.scp.cgp","org.activiti.rest.diagram", "com.bjzhianjia.scp.security.wf.base"})
@EnableAceAuthClient
@EnableAceMerge
@EnableSwagger2Doc
public class CgpBootstrap {
	
    public static void main(String[] args) {
        new SpringApplicationBuilder(CgpBootstrap.class).web(true).run(args);
    }
}
