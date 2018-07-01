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

package com.bjzhianjia.scp.codingapi.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.codingapi.tx.control.service.IActionService;
import com.bjzhianjia.scp.codingapi.tx.framework.task.TaskGroup;
import com.bjzhianjia.scp.codingapi.tx.framework.task.TaskGroupManager;
import com.bjzhianjia.scp.codingapi.tx.framework.task.TaskState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 通知提交
 * create by lorne on 2017/11/13
 */
@Service(value = "t")
public class ActionTServiceImpl implements IActionService {


    private Logger logger = LoggerFactory.getLogger(ActionTServiceImpl.class);

    @Override
    public String execute(JSONObject resObj, String json) {
        String res;
        //通知提醒
        final int state = resObj.getInteger("c");
        String taskId = resObj.getString("t");
        TaskGroup task = TaskGroupManager.getInstance().getTaskGroup(taskId);
        logger.info("accept notify data ->" + json);
        if (task != null) {
            if (task.isAwait()) {   //已经等待
                res = notifyWaitTask(task, state);
            } else {
                int index = 0;
                while (true) {
                    if (index > 500) {
                        res = "0";
                        break;
                    }
                    if (task.isAwait()) {   //已经等待
                        res = notifyWaitTask(task, state);
                        break;
                    }
                    index++;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            res = "0";
        }
        logger.info("accept notify response res ->" + res);
        return res;
    }

    private String notifyWaitTask(TaskGroup task, int state) {
        String res;
        task.setState(state);
        task.signalTask();
        int count = 0;

        while (true) {
            if (task.isRemove()) {

                if (task.getState() == TaskState.rollback.getCode()
                    || task.getState() == TaskState.commit.getCode()) {

                    res = "1";
                } else {
                    res = "0";
                }
                break;
            }
            if (count > 1000) {
                //已经通知了，有可能失败.
                res = "2";
                break;
            }

            count++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }
}
