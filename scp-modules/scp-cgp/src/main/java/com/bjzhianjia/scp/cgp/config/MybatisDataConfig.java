/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.cgp.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 租户\部门数据隔离
 * 
 * @author scp
 * @create 2018/2/11.
 */
@Configuration
@MapperScan(basePackages = { "com.bjzhianjia.scp.cgp.mapper" }, sqlSessionFactoryRef = "cgpsqlSessionFactory")
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
			 * "com.bjzhianjia.scp.security.admin.mapper.UserMapper.selectByPrimaryKey")); }
			 */
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource") // application.yml中对应属性的前缀
	public DataSource cgpDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	public SqlSessionFactory cgpsqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(cgpDataSource()); // 使用workflow数据源, 连接workflow库
		return factoryBean.getObject();

	}

	@Bean
	@Primary
	public SqlSessionTemplate cgpSqlSessionTemplate() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(cgpsqlSessionFactory()); // 使用上面配置的Factory
		return template;
	}

}
