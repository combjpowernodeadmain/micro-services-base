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

package com.bjzhianjia.scp.codingapi.tx.framework.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.bjzhianjia.scp.codingapi.tx.model.TransactionInvocation;

/**
 * create by lorne on 2017/11/13
 */
public class MethodUtils {

    private static final Logger logger = LoggerFactory.getLogger(MethodUtils.class);

    public static boolean invoke(ApplicationContext spring, TransactionInvocation invocation) {
        try {
            Object bean = spring.getBean(invocation.getTargetClazz());
            Object res = org.apache.commons.lang.reflect.MethodUtils.invokeMethod(bean, invocation.getMethod(), invocation.getArgumentValues(), invocation.getParameterTypes());
            logger.info("事务补偿执行---> className:" + invocation.getTargetClazz() + ",methodName::" + invocation.getMethod() + ",args:" + invocation.getArgumentValues() + ",res:" + res);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
