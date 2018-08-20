package com.bjzhianjia.scp.security.wf.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
/**
 *  工作流数据源属性
 * @author chenshuai
 */
@ConfigurationProperties(prefix = "workflow.datasource")
@Data
@Component
public class ActivitiDataSourceProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private Integer maxActive;

    private Integer initialSize;

    private Integer minIdle;

    private Integer maxWait;

    private Integer maxPoolPreparedStatementPerConnectionSize;

    private Integer timeBetweenEvictionRunsMillis;

    private Integer minEvictableIdleTimeMillis;

    private Boolean poolPreparedStatements;

}