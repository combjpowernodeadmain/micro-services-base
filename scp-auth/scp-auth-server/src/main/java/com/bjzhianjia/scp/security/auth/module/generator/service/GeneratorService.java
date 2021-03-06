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

package com.bjzhianjia.scp.security.auth.module.generator.service;

import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.auth.module.client.entity.Client;
import com.bjzhianjia.scp.security.auth.module.generator.util.GeneratorUtils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.zip.ZipOutputStream;

/**
 * @author scp
 * @version 1.0
 */
@Component
public class GeneratorService {
    /**
     * 生成工程
     * @return
     * @param client
     * @param packageName
     * @param zipkin
     * @param tx
     */
    public byte[] buildProject(Client client, String packageName, Boolean zipkin, Boolean tx){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        HashMap<String, Object> map = new HashMap<>();
        map.put("package", packageName);
        map.put("author", BaseContextHandler.getUsername());
        map.put("description", client.getDescription());
        // 决定服务名\maven模块名\主文件夹名
        map.put("clientId", client.getCode());
        map.put("clientSecret", client.getSecret());
        map.put("zipkin",zipkin);
        map.put("tx",tx);
        GeneratorUtils.buildProject(map,zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
