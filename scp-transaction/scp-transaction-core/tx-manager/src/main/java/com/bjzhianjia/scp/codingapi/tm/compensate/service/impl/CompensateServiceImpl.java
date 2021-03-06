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

package com.bjzhianjia.scp.codingapi.tm.compensate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tm.compensate.dao.CompensateDao;
import com.bjzhianjia.scp.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.bjzhianjia.scp.codingapi.tm.compensate.model.TxModel;
import com.bjzhianjia.scp.codingapi.tm.compensate.service.CompensateService;
import com.bjzhianjia.scp.codingapi.tm.config.ConfigReader;
import com.bjzhianjia.scp.codingapi.tm.manager.ModelInfoManager;
import com.bjzhianjia.scp.codingapi.tm.manager.service.TxManagerSenderService;
import com.bjzhianjia.scp.codingapi.tm.manager.service.TxManagerService;
import com.bjzhianjia.scp.codingapi.tm.model.ModelInfo;
import com.bjzhianjia.scp.codingapi.tm.model.ModelName;
import com.bjzhianjia.scp.codingapi.tm.netty.model.TxGroup;
import com.bjzhianjia.scp.codingapi.tm.netty.model.TxInfo;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.DateUtil;
import com.lorne.core.framework.utils.encode.Base64Utils;
import com.lorne.core.framework.utils.http.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    private Logger logger = LoggerFactory.getLogger(CompensateServiceImpl.class);

    @Autowired
    private CompensateDao compensateDao;

    @Autowired
    private ConfigReader configReader;

    @Autowired
    private TxManagerSenderService managerSenderService;

    @Autowired
    private TxManagerService managerService;


    private Executor threadPool = Executors.newFixedThreadPool(20);

    @Override
    public boolean saveCompensateMsg(final TransactionCompensateMsg transactionCompensateMsg) {

        TxGroup txGroup =managerService.getTxGroup(transactionCompensateMsg.getGroupId());
        if (txGroup == null) {
            return false;
        }

        managerService.deleteTxGroup(txGroup);

        //会出现启动模块失败的情况，因此不能校验其他模块是否通知成功，因为只是发起方失败时。也需要提交补偿

//        //已经全部通知的模块不做补偿处理
//        boolean hasNoNotify = false;
//
//        for(TxInfo txInfo:txGroup.getList()){
//            if(txInfo.getNotify()==0){
//                hasNoNotify = true;
//            }
//        }
//
//        if(!hasNoNotify){
//            //事务已经执行完毕的
//            logger.info("TxGroup had notify ! ");
//            return true;
//        }


        transactionCompensateMsg.setTxGroup(txGroup);

        final String json = JSON.toJSONString(transactionCompensateMsg);

        logger.info("Compensate->" + json);

        final String compensateKey = compensateDao.saveCompensateMsg(transactionCompensateMsg);

        //调整自动补偿机制，若开启了自动补偿，需要通知业务返回success，方可执行自动补偿
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String groupId = transactionCompensateMsg.getGroupId();
                    JSONObject requestJson = new JSONObject();
                    requestJson.put("action", "compensate");
                    requestJson.put("groupId", groupId);
                    requestJson.put("json", json);

                    String url = configReader.getCompensateNotifyUrl();
                    logger.error("Compensate Callback Address->" + url);
                    String res = HttpUtils.postJson(url, requestJson.toJSONString());
                    logger.error("Compensate Callback Result->" + res);
                    if (configReader.isCompensateAuto()) {
                        //自动补偿,是否自动执行补偿
                        if (res.contains("success")||res.contains("SUCCESS")) {
                            //自动补偿
                            autoCompensate(compensateKey, transactionCompensateMsg);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Compensate Callback Fails->" + e.getMessage());
                }
            }
        });

        return StringUtils.isNotEmpty(compensateKey);


    }


    public void autoCompensate(final String compensateKey, TransactionCompensateMsg transactionCompensateMsg) {
        final String json = JSON.toJSONString(transactionCompensateMsg);
        logger.info("Auto Compensate->" + json);
        //自动补偿业务执行...
        final int tryTime = configReader.getCompensateTryTime();
        boolean autoExecuteRes = false;
        try {
            int executeCount = 0;
            autoExecuteRes = _executeCompensate(json);
            logger.info("Automatic Compensate Result->" + autoExecuteRes + ",json->" + json);
            while (!autoExecuteRes) {
                logger.info("Compensate Failure, Entering Compensate Queue->" + autoExecuteRes + ",json->" + json);
                executeCount++;
                if(executeCount==3){
                    autoExecuteRes = false;
                    break;
                }
                try {
                    Thread.sleep(tryTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                autoExecuteRes = _executeCompensate(json);
            }

            //执行成功删除数据
            if(autoExecuteRes) {
                compensateDao.deleteCompensateByKey(compensateKey);
            }

        }catch (Exception e){
            logger.error("Auto Compensate Fails,msg:"+e.getLocalizedMessage());
            //推送数据给第三方通知
            autoExecuteRes = false;
        }

        //执行补偿以后通知给业务方
        String groupId = transactionCompensateMsg.getGroupId();
        JSONObject requestJson = new JSONObject();
        requestJson.put("action","notify");
        requestJson.put("groupId",groupId);
        requestJson.put("resState",autoExecuteRes);

        String url = configReader.getCompensateNotifyUrl();
        logger.error("Compensate Result Callback Address->" + url);
        String res = HttpUtils.postJson(url, requestJson.toJSONString());
        logger.error("Compensate Result Callback Result->" + res);

    }



    @Override
    public List<ModelName> loadModelList() {
        List<String> keys =  compensateDao.loadCompensateKeys();

        Map<String,Integer> models = new HashMap<>();

        for(String key:keys){
            if(key.length()>36){
                String name =  key.substring(11,key.length()-25);
                int v = 1;
                if(models.containsKey(name)){
                    v =  models.get(name)+1;
                }
                models.put(name,v);
            }
        }
        List<ModelName> names = new ArrayList<>();

        for(String key:models.keySet()){
            int v = models.get(key);
            ModelName modelName = new ModelName();
            modelName.setName(key);
            modelName.setCount(v);
            names.add(modelName);
        }
        return names;
    }

    @Override
    public List<String> loadCompensateTimes(String model) {
        return compensateDao.loadCompensateTimes(model);
    }

    @Override
    public List<TxModel> loadCompensateByModelAndTime(String path) {
        List<String> logs = compensateDao.loadCompensateByModelAndTime(path);

        List<TxModel> models = new ArrayList<>();
        for (String json : logs) {
            JSONObject jsonObject = JSON.parseObject(json);
            TxModel model = new TxModel();
            long currentTime = jsonObject.getLong("currentTime");
            model.setTime(DateUtil.formatDate(new Date(currentTime), DateUtil.FULL_DATE_TIME_FORMAT));
            model.setClassName(jsonObject.getString("className"));
            model.setMethod(jsonObject.getString("methodStr"));
            model.setExecuteTime(jsonObject.getInteger("time"));
            model.setBase64(Base64Utils.encode(json.getBytes()));
            model.setState(jsonObject.getInteger("state"));
            model.setOrder(currentTime);

            String groupId = jsonObject.getString("groupId");

            String key = path + "_" + groupId;
            model.setKey(key);

            models.add(model);
        }
        Collections.sort(models, new Comparator<TxModel>() {
            @Override
            public int compare(TxModel o1, TxModel o2) {
                if (o2.getOrder() > o1.getOrder()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return models;
    }

    @Override
    public boolean hasCompensate() {
        return compensateDao.hasCompensate();
    }

    @Override
    public boolean delCompensate(String path) {
        compensateDao.deleteCompensateByPath(path);
        return true;
    }

    @Override
    public void reloadCompensate(TxGroup txGroup) {
        TxGroup compensateGroup = getCompensateByGroupId(txGroup.getGroupId());
        if (compensateGroup != null) {
            for (TxInfo txInfo : txGroup.getList()) {

                for (TxInfo cinfo : compensateGroup.getList()) {
                    if (cinfo.getModel().equals(txInfo.getModel()) && cinfo.getMethodStr().equals(txInfo.getMethodStr())) {

                        //根据之前的数据补偿现在的事务

                        int oldNotify = cinfo.getNotify();

                        if (oldNotify == 1) {
                            txInfo.setIsCommit(0);
                        } else {
                            txInfo.setIsCommit(1);
                        }
                    }
                }

            }
        }

        logger.info("Compensate Loaded->"+JSON.toJSONString(txGroup));
    }

    private TxGroup getCompensateByGroupId(String groupId) {
        String json = compensateDao.getCompensateByGroupId(groupId);
        if (json == null) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);
        String txGroup = jsonObject.getString("txGroup");
        return JSON.parseObject(txGroup, TxGroup.class);
    }


    @Override
    public boolean executeCompensate(String path) throws ServiceException {

        String json = compensateDao.getCompensate(path);
        if (json == null) {
            throw new ServiceException("no data existing");
        }

        boolean hasOk = _executeCompensate(json);
        if (hasOk) {
            // 删除本地补偿数据
            compensateDao.deleteCompensateByPath(path);

            return true;
        }
        return false;
    }


    private boolean _executeCompensate(String json) throws ServiceException {
        JSONObject jsonObject = JSON.parseObject(json);

        String model = jsonObject.getString("model");

        int startError = jsonObject.getInteger("startError");

        ModelInfo modelInfo = ModelInfoManager.getInstance().getModelByModel(model);
        if (modelInfo == null) {
            throw new ServiceException("current model offline.");
        }

        String data = jsonObject.getString("data");

        String groupId = jsonObject.getString("groupId");

        String res = managerSenderService.sendCompensateMsg(modelInfo.getChannelName(), groupId, data,startError);

        return "1".equals(res);
    }
}
