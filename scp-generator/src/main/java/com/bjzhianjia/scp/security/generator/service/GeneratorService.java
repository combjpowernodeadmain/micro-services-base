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

package com.bjzhianjia.scp.security.generator.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.generator.mapper.GeneratorMapper;
import com.bjzhianjia.scp.security.generator.utils.GeneratorUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 * 
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
@Service
public class GeneratorService {
	@Autowired
	private GeneratorMapper generatorMapper;

	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		int offset = Integer.parseInt(map.get("offset").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		map.put("offset", offset);
		map.put("limit", limit);
		return generatorMapper.queryList(map);
	}

	public int queryTotal(Map<String, Object> map) {
		return generatorMapper.queryTotal(map);
	}

	public Map<String, String> queryTable(String tableName) {
		return generatorMapper.queryTable(tableName);
	}

	public List<Map<String, String>> queryColumns(String tableName) {
		return generatorMapper.queryColumns(tableName);
	}

	public byte[] generatorCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for(String tableName : tableNames){
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			GeneratorUtils.generatorCode(table, columns, zip);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}
}
