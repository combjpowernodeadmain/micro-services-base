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

package com.bjzhianjia.scp.security.wf.base.config;

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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

/**
 * 
 * MybatisDbAConfig  数据配置.
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
@MapperScan(basePackages = {"com.bjzhianjia.scp.security.wf.base.auth.mapper",
		"com.bjzhianjia.scp.security.wf.base.design.mapper",
		"com.bjzhianjia.scp.security.wf.base.monitor.mapper",
	"com.bjzhianjia.scp.security.wf.base.task.mapper"}, sqlSessionFactoryRef = "wfsqlSessionFactory")
public class MybatisDbAConfig {
    
	@Autowired
    private ActivitiDataSourceProperties activitiDataSourceProperties;
	
	@Bean
	public DataSource wfDataSource() throws SQLException {
        
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(activitiDataSourceProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(activitiDataSourceProperties.getPassword());
        mysqlXaDataSource.setUser(activitiDataSourceProperties.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("wfDataSource");

        xaDataSource.setMinPoolSize(activitiDataSourceProperties.getMinPoolSize());
        xaDataSource.setMaxPoolSize(activitiDataSourceProperties.getMaxPoolSize());
        xaDataSource.setMaxLifetime(activitiDataSourceProperties.getMaxLifetime());
        xaDataSource.setBorrowConnectionTimeout(activitiDataSourceProperties.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(activitiDataSourceProperties.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(activitiDataSourceProperties.getMaintenanceInterval());
        xaDataSource.setMaxIdleTime(activitiDataSourceProperties.getMaxIdleTime());
//        xaDataSource.setTestQuery(activitiDataSourceProperties.getTestQuery());
        return xaDataSource;
	}
    
	@Bean
	public SqlSessionFactory wfsqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(wfDataSource()); // 使用workflow数据源, 连接workflow库
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/workflow/*.xml"));
		return factoryBean.getObject();

	}

	@Bean
	public SqlSessionTemplate wfSqlSessionTemplate() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(wfsqlSessionFactory()); // 使用上面配置的Factory
		return template;
	}
}
