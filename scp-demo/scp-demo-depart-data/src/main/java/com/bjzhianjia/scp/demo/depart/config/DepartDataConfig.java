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

package com.bjzhianjia.scp.demo.depart.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.bjzhianjia.scp.security.common.data.IUserDepartDataService;
import com.bjzhianjia.scp.security.common.data.MybatisDataInterceptor;

import javax.annotation.PostConstruct;

/**
 * @author scp
 * @create 2018/2/11.
 */
@Configuration
public class DepartDataConfig {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private IUserDepartDataService userDepartDataService;

    @PostConstruct
    public void init(){
        sqlSessionFactory.getConfiguration().addInterceptor(new MybatisDataInterceptor(userDepartDataService));
//        sqlSessionFactory.getConfiguration().addInterceptor(new TenantMybatisInterceptor());
    }
}
