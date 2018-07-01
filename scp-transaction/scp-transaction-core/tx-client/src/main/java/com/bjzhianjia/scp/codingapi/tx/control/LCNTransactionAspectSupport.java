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

package com.bjzhianjia.scp.codingapi.tx.control;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tx.aop.bean.TxTransactionLocal;
import com.bjzhianjia.scp.codingapi.tx.framework.utils.SocketManager;
import com.bjzhianjia.scp.codingapi.tx.model.Request;

import org.apache.commons.lang.StringUtils;

/**
 * create by lorne on 2017/12/5
 */
public class LCNTransactionAspectSupport {



    private static LCNTransactionAspectSupport instance = null;

    private LCNTransactionAspectSupport(){}

    public static LCNTransactionAspectSupport currentTransactionStatus() {
        if (instance == null) {
            synchronized (LCNTransactionAspectSupport.class) {
                if(instance==null){
                    instance = new LCNTransactionAspectSupport();
                }
            }
        }
        return instance;
    }


    public boolean setRollbackOnly(){
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal==null){
            return false;
        }

        if(StringUtils.isEmpty(txTransactionLocal.getGroupId())){
            return false;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", txTransactionLocal.getGroupId());
        Request request = new Request("rg", jsonObject.toString());
        String json =  SocketManager.getInstance().sendMsg(request);
        try {
            return Integer.parseInt(json)==1;
        }catch (Exception e){
            return false;
        }
    }

}
