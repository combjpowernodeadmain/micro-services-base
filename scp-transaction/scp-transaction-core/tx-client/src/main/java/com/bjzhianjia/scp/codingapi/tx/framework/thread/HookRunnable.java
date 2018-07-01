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

package com.bjzhianjia.scp.codingapi.tx.framework.thread;

import java.util.concurrent.TimeUnit;

import com.bjzhianjia.scp.codingapi.tx.Constants;

/**
 * create by lorne on 2017/8/9
 */
public abstract class HookRunnable implements Runnable {

    private volatile boolean hasOver;

    @Override
    public void run() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Constants.hasExit = true;
                while (!hasOver) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if (!Constants.hasExit) {
            Runtime.getRuntime().addShutdownHook(thread);
        } else {
            System.out.println("jvm has exit..");
            return;
        }
        try {
            run0();
        } finally {

            hasOver = true;

            if (!thread.isAlive()) {
                Runtime.getRuntime().removeShutdownHook(thread);
            }
        }
    }

    public abstract void run0();

}
