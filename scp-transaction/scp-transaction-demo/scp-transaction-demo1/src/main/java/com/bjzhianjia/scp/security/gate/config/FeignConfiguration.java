/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.bjzhianjia.scp.security.gate.config;

import com.bjzhianjia.scp.codingapi.tx.springcloud.feign.TransactionRestTemplateInterceptor;
import com.bjzhianjia.scp.security.auth.client.interceptor.ServiceFeignInterceptor;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @author scp
 * @version 1.0
 */
public class FeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new TransactionRestTemplateInterceptor();
    }

    @Bean
    public ServiceFeignInterceptor serviceFeignInterceptor(){
        return new ServiceFeignInterceptor();
    }
}
