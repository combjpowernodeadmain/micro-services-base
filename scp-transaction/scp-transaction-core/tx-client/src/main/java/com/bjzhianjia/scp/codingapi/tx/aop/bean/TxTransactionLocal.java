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

package com.bjzhianjia.scp.codingapi.tx.aop.bean;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tx.framework.utils.SocketManager;
import com.bjzhianjia.scp.codingapi.tx.model.Request;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式事务远程调用控制对象
 * Created by lorne on 2017/6/5.
 */
public class TxTransactionLocal {

    private Logger logger = LoggerFactory.getLogger(TxTransactionLocal.class);

    private final static ThreadLocal<TxTransactionLocal> currentLocal = new ThreadLocal<TxTransactionLocal>();

    private String groupId;

    private int maxTimeOut;

    private Map<String,String> cacheModelInfo = new ConcurrentHashMap<>();

    /**
     * 是否同一个模块被多次请求
     */
    private boolean hasIsGroup = false;

    /**
     * 是否是发起方模块
     */
    private boolean hasStart = false;

    /**
     * 时候已经获取到连接对象
     */
    private boolean hasConnection = false;


    private String kid;

    private String type;

    private boolean readOnly = false;

    public boolean isHasIsGroup() {
        return hasIsGroup;
    }

    public void setHasIsGroup(boolean hasIsGroup) {
        this.hasIsGroup = hasIsGroup;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public boolean isHasStart() {
        return hasStart;
    }

    public void setHasStart(boolean hasStart) {
        this.hasStart = hasStart;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isHasConnection() {
        return hasConnection;
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }


    public int getMaxTimeOut() {
        return maxTimeOut;
    }

    public void setMaxTimeOut(int maxTimeOut) {
        this.maxTimeOut = maxTimeOut;
    }


    public static TxTransactionLocal current() {
        return currentLocal.get();
    }

    public static void setCurrent(TxTransactionLocal current) {
        currentLocal.set(current);
    }


    public void putLoadBalance(String key, String data){
        cacheModelInfo.put(key,data);
        //与TxManager通讯
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", getGroupId());
        jsonObject.put("k", key);
        jsonObject.put("d", data);
        logger.info("putLoadBalance--> start ");
        Request request = new Request("plb", jsonObject.toString());
        String json =  SocketManager.getInstance().sendMsg(request);
        logger.info("putLoadBalance--> end ,res ->"+json);
    }


    public String getLoadBalance(String key){
        String old =  cacheModelInfo.get(key);
        logger.info("cacheModelInfo->"+old);
        if(old==null){
            //与TxManager通讯
            logger.info("getLoadBalance--> start");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("g", getGroupId());
            jsonObject.put("k", key);
            Request request = new Request("glb", jsonObject.toString());
            String json =  SocketManager.getInstance().sendMsg(request);
            logger.info("getLoadBalance--> end ,res - >" + json);
            if(StringUtils.isNotEmpty(json)){
                return json;
            }
        }
        return old;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
