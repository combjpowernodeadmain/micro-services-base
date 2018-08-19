package com.bjzhianjia.scp.security.wf.base.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * scp-workflow-base项目 数据配置
 * @author chenshuai
 *
 */
@Configuration
@MapperScan(basePackages = {"com.bjzhianjia.scp.security.wf.base.auth.mapper",
		"com.bjzhianjia.scp.security.wf.base.design.mapper",
		"com.bjzhianjia.scp.security.wf.base.monitor.mapper",
	"com.bjzhianjia.scp.security.wf.base.task.mapper"}, sqlSessionFactoryRef = "wfsqlSessionFactory")
public class MybatisDbAConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "workflow.datasource") // application.yml中对应属性的前缀
	public DataSource wfDataSource() {
		return DataSourceBuilder.create().build();
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
