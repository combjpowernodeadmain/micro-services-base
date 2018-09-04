/*
 *
 * Copyright 2018 by lutuo.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * lutuo ("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with lutuo.
 *
 */

package com.bjzhianjia.scp.cgp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


/**
 * 
 * CgpDataSourceProperties cgp数据源属性
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月3日        chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
@ConfigurationProperties(prefix = "spring.datasource")
@Data
@Component
public class CgpDataSourceProperties {

    private String url;

    private String username;

    private String password;

//    private String driverClassName;
//
//    private Integer maxActive;
//
//    private Integer initialSize;
//
//    private Integer minIdle;
//
//    private Integer maxWait;
//
//    private Integer maxPoolPreparedStatementPerConnectionSize;
//
//    private Integer timeBetweenEvictionRunsMillis;
//
//    private Integer minEvictableIdleTimeMillis;

    private Boolean poolPreparedStatements;

    /** min-pool-size 最小连接数 **/
    private Integer minPoolSize;

    /** max-pool-size 最大连接数 **/
    private Integer maxPoolSize;

    /** max-lifetime 连接最大存活时间 **/
    private Integer maxLifetime;

    /** borrow-connection-timeout 获取连接失败重新获等待最大时间，在这个时间内如果有可用连接，将返回 **/
    private Integer borrowConnectionTimeout;

    /** login-timeout java数据库连接池，最大可等待获取datasouce的时间 **/
    private Integer loginTimeout;

    /** maintenance-interval 连接回收时间 **/
    private Integer maintenanceInterval;

    /** max-idle-time 最大闲置时间，超过最小连接池连接的连接将将关闭 **/
    private Integer maxIdleTime;

}