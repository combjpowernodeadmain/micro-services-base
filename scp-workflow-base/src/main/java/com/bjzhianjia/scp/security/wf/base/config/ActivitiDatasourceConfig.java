package com.bjzhianjia.scp.security.wf.base.config;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;


/**
 * ActivitiDatasourceConfig activiti数据源配置.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月4日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
@Configuration
public class ActivitiDatasourceConfig extends AbstractProcessEngineAutoConfiguration{
	
	@Autowired
	ActivitiDataSourceProperties activitiDataSourceProperties;
	
	@Bean
    public DataSource activitiDataSource() throws SQLException {
	      
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(activitiDataSourceProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(activitiDataSourceProperties.getPassword());
        mysqlXaDataSource.setUser(activitiDataSourceProperties.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("acDataSource");
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
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor) throws IOException, SQLException {
    	
    	DataSource dataSource = activitiDataSource();
    	
        return baseSpringProcessEngineConfiguration(
        		dataSource,
                transactionManager,
                springAsyncExecutor);
    }
}
