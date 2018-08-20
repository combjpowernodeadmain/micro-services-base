package com.bjzhianjia.scp.security.wf.base.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
/**
 * activiti数据源配置
 * @author chenshuai
 *
 */
@Configuration
public class ActivitiDatasourceConfig extends AbstractProcessEngineAutoConfiguration{
	
	@Autowired
	ActivitiDataSourceProperties activitiDataSourceProperties;
	
	@Bean
    @ConfigurationProperties(prefix = "workflow.datasource")
    public DataSource activitiDataSource() {
		DataSource dataSource =  DataSourceBuilder.create()
                .url(activitiDataSourceProperties.getUrl())
                .username(activitiDataSourceProperties.getUsername())
                .password(activitiDataSourceProperties.getPassword())
                .driverClassName(activitiDataSourceProperties.getDriverClassName())
                .build();
		 return dataSource; 
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor) throws IOException {
    	
    	DataSource dataSource = activitiDataSource();
    	
        return baseSpringProcessEngineConfiguration(
        		dataSource,
                transactionManager,
                springAsyncExecutor);
    }
}
