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

package com.bjzhianjia.scp.codingapi.tm.netty.model;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tm.model.ChannelSender;
import com.lorne.core.framework.model.JsonModel;

/**
 * Created by lorne on 2017/6/7.
 */
public class TxInfo extends JsonModel {

    /**
     * 任务唯一标示
     */
    private String kid;

    /**
     * 模块管道名称（netty管道名称）
     */
    private String modelName;

    /**
     * 是否通知成功
     */
    private int notify;

    /**
     * 0 不组合
     * 1 组合
     */
    private int isGroup;

    /**
     * tm识别标示
     */
    private String address;

    /**
     * tx识别标示
     */
    private String uniqueKey;


    /**
     * 管道发送数据
     */
    private ChannelSender channel;


    /**
     * 业务方法名称
     */
    private String methodStr;

    /**
     * 模块名称
     */
    private String model;

    /**
     * 模块地址
     */
    private String modelIpAddress;

    /**
     * 是否提交（临时数据）
     */
    private int isCommit;

    public int getIsCommit() {
        return isCommit;
    }

    public void setIsCommit(int isCommit) {
        this.isCommit = isCommit;
    }

    public String getMethodStr() {
        return methodStr;
    }

    public void setMethodStr(String methodStr) {
        this.methodStr = methodStr;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelIpAddress() {
        return modelIpAddress;
    }

    public void setModelIpAddress(String modelIpAddress) {
        this.modelIpAddress = modelIpAddress;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public ChannelSender getChannel() {
        return channel;
    }

    public void setChannel(ChannelSender channel) {
        this.channel = channel;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("kid",getKid());
        jsonObject.put("modelName",getModelName());
        jsonObject.put("notify",getNotify());
        jsonObject.put("isGroup",getIsGroup());
        jsonObject.put("address",getAddress());
        jsonObject.put("uniqueKey",getUniqueKey());

        jsonObject.put("model", getModel());
        jsonObject.put("modelIpAddress", getModelIpAddress());
        jsonObject.put("methodStr", getMethodStr());

        return jsonObject.toString();
    }
}
