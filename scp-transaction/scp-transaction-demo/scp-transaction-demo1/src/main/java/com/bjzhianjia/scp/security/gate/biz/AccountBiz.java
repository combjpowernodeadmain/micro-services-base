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

package com.bjzhianjia.scp.security.gate.biz;

import com.bjzhianjia.scp.codingapi.tx.annotation.TxTransaction;
import com.bjzhianjia.scp.security.common.biz.BaseBiz;
import com.bjzhianjia.scp.security.gate.entity.Account;
import com.bjzhianjia.scp.security.gate.feign.IProdFeign;
import com.bjzhianjia.scp.security.gate.mapper.AccountMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
@Service
public class AccountBiz extends BaseBiz<AccountMapper,Account> {
    @Autowired
    private IProdFeign prodFeign;
    @TxTransaction
    @Transactional
    public void test(){
        prodFeign.test();
        Account account = mapper.selectByPrimaryKey(1);
        Integer balance = account.getBalance();
        account.setBalance(balance-100);
        mapper.updateByPrimaryKey(account);
        int i =  1/0;
    }
}
