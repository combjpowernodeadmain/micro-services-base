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

package com.bjzhianjia.scp.codingapi.tx.compensate.service.impl;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxCompensateLocal;
import com.bjzhianjia.scp.codingapi.tx.compensate.model.CompensateInfo;
import com.bjzhianjia.scp.codingapi.tx.compensate.service.CompensateService;
import com.bjzhianjia.scp.codingapi.tx.framework.utils.MethodUtils;
import com.bjzhianjia.scp.codingapi.tx.model.TransactionInvocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    @Autowired
    private ApplicationContext spring;


    private Logger logger = LoggerFactory.getLogger(CompensateServiceImpl.class);

    @Override
    public void saveLocal(CompensateInfo compensateInfo) {
        String json = JSON.toJSONString(compensateInfo);
        logger.info("补偿本地记录->" + json);
    }

    @Override
    public boolean invoke(TransactionInvocation invocation, String groupId, int startState) {

        TxCompensateLocal compensateLocal = new TxCompensateLocal();
        compensateLocal.setGroupId(groupId);
        compensateLocal.setStartState(startState);

        TxCompensateLocal.setCurrent(compensateLocal);

        boolean res = MethodUtils.invoke(spring, invocation);

        TxCompensateLocal.setCurrent(null);

        return res;
    }
}
