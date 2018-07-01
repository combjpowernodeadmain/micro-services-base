/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.security.admin.service;

import com.bjzhianjia.scp.merge.facade.IMergeResultParser;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author scp
 * @create 2018/2/4.
 */
@Component
public class TableResultParser implements IMergeResultParser {
    @Override
    public List parser(Object o) {
        TableResultResponse response = (TableResultResponse) o;
        TableResultResponse.TableData data = (TableResultResponse.TableData) response.getData();
        List result = data.getRows();
        return result;
    }
}
