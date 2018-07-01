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

package com.bjzhianjia.scp.codingapi.tm.manager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bjzhianjia.scp.codingapi.tm.model.ModelInfo;

/**
 * create by lorne on 2017/11/13
 */
public class ModelInfoManager {


    private List<ModelInfo> modelInfos = new CopyOnWriteArrayList<>();

    private static ModelInfoManager manager = null;


    public static ModelInfoManager getInstance() {
        if (manager == null) {
            synchronized (ModelInfoManager.class) {
                if (manager == null) {
                    manager = new ModelInfoManager();
                }
            }
        }
        return manager;
    }

    public void removeModelInfo(String channelName) {
        for (ModelInfo modelInfo : modelInfos) {
            if (channelName.equalsIgnoreCase(modelInfo.getChannelName())) {
                modelInfos.remove(modelInfo);
            }
        }
    }


    public void addModelInfo(ModelInfo minfo) {
        for (ModelInfo modelInfo : modelInfos) {
            if (minfo.getChannelName().equalsIgnoreCase(modelInfo.getChannelName())) {
                return;
            }

            if (minfo.getIpAddress().equalsIgnoreCase(modelInfo.getIpAddress())) {
                return;
            }
        }
        modelInfos.add(minfo);
    }

    public List<ModelInfo> getOnlines() {
        return modelInfos;
    }

    public ModelInfo getModelByChannelName(String channelName) {
        for (ModelInfo modelInfo : modelInfos) {
            if (channelName.equalsIgnoreCase(modelInfo.getChannelName())) {
                return modelInfo;
            }
        }
        return null;
    }

    public ModelInfo getModelByModel(String model) {
        for (ModelInfo modelInfo : modelInfos) {
            if (model.equalsIgnoreCase(modelInfo.getModel())) {
                return modelInfo;
            }
        }
        return null;
    }
}
