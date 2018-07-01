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

package com.bjzhianjia.scp.codingapi.tm.manager.service.impl;


import com.bjzhianjia.scp.codingapi.tm.Constants;
import com.bjzhianjia.scp.codingapi.tm.config.ConfigReader;
import com.bjzhianjia.scp.codingapi.tm.manager.ModelInfoManager;
import com.bjzhianjia.scp.codingapi.tm.manager.service.LoadBalanceService;
import com.bjzhianjia.scp.codingapi.tm.manager.service.TxManagerSenderService;
import com.bjzhianjia.scp.codingapi.tm.manager.service.TxManagerService;
import com.bjzhianjia.scp.codingapi.tm.model.ModelInfo;
import com.bjzhianjia.scp.codingapi.tm.netty.model.TxGroup;
import com.bjzhianjia.scp.codingapi.tm.netty.model.TxInfo;
import com.bjzhianjia.scp.codingapi.tm.redis.service.RedisServerService;
import com.lorne.core.framework.utils.KidUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/6/7.
 */
@Service
public class TxManagerServiceImpl implements TxManagerService {



    @Autowired
    private ConfigReader configReader;

    @Autowired
    private RedisServerService redisServerService;


    @Autowired
    private TxManagerSenderService transactionConfirmService;


    @Autowired
    private LoadBalanceService loadBalanceService;


    private Logger logger = LoggerFactory.getLogger(TxManagerServiceImpl.class);


    @Override
    public TxGroup createTransactionGroup(String groupId) {
        TxGroup txGroup = new TxGroup();
        if (StringUtils.isNotEmpty(groupId)) {
            txGroup.setIsCommit(1);
        } else {
            groupId = KidUtils.generateShortUuid();
        }

        txGroup.setStartTime(System.currentTimeMillis());
        txGroup.setGroupId(groupId);

        String key = configReader.getKeyPrefix() + groupId;
        redisServerService.saveTransaction(key, txGroup.toJsonString());

        return txGroup;
    }


    @Override
    public TxGroup addTransactionGroup(String groupId, String taskId, int isGroup, String modelName, String methodStr) {
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        if (txGroup==null) {
            return null;
        }
        TxInfo txInfo = new TxInfo();
        txInfo.setModelName(modelName);
        txInfo.setKid(taskId);
        txInfo.setAddress(Constants.address);
        txInfo.setIsGroup(isGroup);
        txInfo.setMethodStr(methodStr);


        ModelInfo modelInfo =  ModelInfoManager.getInstance().getModelByChannelName(modelName);
        if(modelInfo!=null) {
            txInfo.setUniqueKey(modelInfo.getUniqueKey());
            txInfo.setModelIpAddress(modelInfo.getIpAddress());
            txInfo.setModel(modelInfo.getModel());
        }

        txGroup.addTransactionInfo(txInfo);

        redisServerService.saveTransaction(key, txGroup.toJsonString());

        return txGroup;
    }

    @Override
    public boolean rollbackTransactionGroup(String groupId) {
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        if (txGroup==null) {
            return false;
        }
        txGroup.setRollback(1);
        redisServerService.saveTransaction(key, txGroup.toJsonString());
        return true;
    }

    @Override
    public int cleanNotifyTransaction(String groupId, String taskId) {
        int res = 0;
        logger.info("start-cleanNotifyTransaction->groupId:"+groupId+",taskId:"+taskId);
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        if (txGroup==null) {
            logger.info("cleanNotifyTransaction - > txGroup is null ");
            return res;
        }

        if(txGroup.getHasOver()==0){
            logger.info("cleanNotifyTransaction - > groupId "+groupId+" not over !");
            return 0;
        }

        if(txGroup.getRollback()==1){
            logger.info("cleanNotifyTransaction - > groupId "+groupId+" only rollback !");
            return 0;
        }

        //更新数据
        boolean hasSet = false;
        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                if(info.getNotify()==0&&info.getIsGroup()==0) {
                    info.setNotify(1);
                    hasSet = true;
                    res = 1;

                    break;
                }
            }
        }

        //判断是否都结束
        boolean isOver = true;
        for (TxInfo info : txGroup.getList()) {
            if (info.getIsGroup() == 0 && info.getNotify() == 0) {
                isOver = false;
                break;
            }
        }

        if (isOver) {
            deleteTxGroup(txGroup);
        }

        //有更新的数据，需要修改记录
        if(!isOver&&hasSet) {
            redisServerService.saveTransaction(key, txGroup.toJsonString());
        }

        logger.info("end-cleanNotifyTransaction->groupId:"+groupId+",taskId:"+taskId+",res(1:commit,0:rollback):"+res);
        return res;
    }


    @Override
    public int closeTransactionGroup(String groupId,int state) {
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        if(txGroup==null){
            return 0;
        }
        txGroup.setState(state);
        txGroup.setHasOver(1);
        redisServerService.saveTransaction(key,txGroup.toJsonString());
        return transactionConfirmService.confirm(txGroup);
    }


    @Override
    public void dealTxGroup(TxGroup txGroup, boolean hasOk) {
        if(hasOk) {
            deleteTxGroup(txGroup);
        }
    }


    @Override
    public void deleteTxGroup(TxGroup txGroup) {
        String groupId = txGroup.getGroupId();

        String key = getTxGroupKey(groupId);
        redisServerService.deleteKey(key);

        loadBalanceService.remove(groupId);
    }


    @Override
    public TxGroup getTxGroup(String groupId) {
        String key = getTxGroupKey(groupId);
        return redisServerService.getTxGroupByKey(key);
    }

    @Override
    public String getTxGroupKey(String groupId) {
        return configReader.getKeyPrefix() + groupId;
    }
}
