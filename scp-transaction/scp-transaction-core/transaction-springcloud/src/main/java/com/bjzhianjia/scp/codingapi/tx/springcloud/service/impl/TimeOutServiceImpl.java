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

package com.bjzhianjia.scp.codingapi.tx.springcloud.service.impl;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.codingapi.tx.Constants;
import com.bjzhianjia.scp.codingapi.tx.listener.service.TimeOutService;

/**
 * create by lorne on 2017/8/7
 */
@Service
public class TimeOutServiceImpl implements TimeOutService {


    @Override
    public void loadOutTime(int timeOut) {
    	//从txManager取
    	if(timeOut <= 0){
    		Constants.maxOutTime = 20*1000;
    	} else {
    		Constants.maxOutTime = timeOut*1000;
    	}
    }
}
