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

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

/**
 * 
 * MybatisDataConfig 多数据源和事务配置
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月3日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@Configuration
@MapperScan(basePackages = {
    "com.bjzhianjia.scp.cgp.mapper" }, sqlSessionFactoryRef = "cgpsqlSessionFactory")
public class MybatisDataConfig {

    /*
     * @Autowired private SqlSessionFactory sqlSessionFactory;
     * 
     *//**
        * 该方法主要是为了让当前用户可以获取授权的数据权限部门
        */

    /*
     * @Autowired private IUserDepartDataService userDepartDataService;
     * 
     * @PostConstruct public void init(){
     *//**
        * 有些mapper的某些方法不需要进行隔离，则可以在配置忽略，按逗号隔开.
        * 如:"com.bjzhianjia.scp.security.admin.mapper.UserMapper.selectOne",表示该mapper下不进行租户隔离
        *//*
           * sqlSessionFactory.getConfiguration().addInterceptor(new
           * MybatisDataInterceptor( userDepartDataService,
           * "com.bjzhianjia.scp.security.admin.mapper.UserMapper.selectOne",
           * "com.bjzhianjia.scp.security.admin.mapper.UserMapper.selectByPrimaryKey"
           * )); }
           */
    @Autowired
    private CgpDataSourceProperties cgpDataSourceProperties;

    /**
     * 配置DataSource
     * 
     * @return
     * @throws SQLException
     */
    @Bean
    @Primary
    public DataSource cgpDataSource() throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(cgpDataSourceProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(cgpDataSourceProperties.getPassword());
        mysqlXaDataSource.setUser(cgpDataSourceProperties.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("cgpDataSource");

        xaDataSource.setMinPoolSize(cgpDataSourceProperties.getMinPoolSize());
        xaDataSource.setMaxPoolSize(cgpDataSourceProperties.getMaxPoolSize());
        xaDataSource.setMaxLifetime(cgpDataSourceProperties.getMaxLifetime());
        xaDataSource
            .setBorrowConnectionTimeout(cgpDataSourceProperties.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(cgpDataSourceProperties.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(cgpDataSourceProperties.getMaintenanceInterval());
        xaDataSource.setMaxIdleTime(cgpDataSourceProperties.getMaxIdleTime());
        // xaDataSource.setTestQuery(cgpDataSourceProperties.getTestQuery());
        return xaDataSource;
    }

    /**
     * 配置SqlSessionFactory
     * 
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public SqlSessionFactory cgpsqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(cgpDataSource());
        factoryBean.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return factoryBean.getObject();
    }

    /**
     * 配置 SqlSessionTemplate
     * 
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public SqlSessionTemplate cgpSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(cgpsqlSessionFactory());
        return template;
    }
}
