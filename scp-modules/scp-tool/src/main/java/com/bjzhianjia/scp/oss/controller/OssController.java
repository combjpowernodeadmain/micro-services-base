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

package com.bjzhianjia.scp.oss.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bjzhianjia.scp.oss.cloud.CloudStorageService;
import com.bjzhianjia.scp.oss.cloud.LocalStorageService;
import com.bjzhianjia.scp.oss.cloud.OSSFactory;
import com.bjzhianjia.scp.security.common.exception.base.BusinessException;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;


/**
 * 文件上传
 * 
 * @author scp
 */
@RestController
@RequestMapping("/oss")
public class OssController{
	@Autowired
	private OSSFactory ossFactory;
	/**
	 * 上传文件
	 */
	@RequestMapping("/upload")
	public ObjectRestResponse<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new BusinessException("上传文件不能为空");
		}
		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = "";
		CloudStorageService storageService = ossFactory.build();
		if(storageService instanceof LocalStorageService) {
			url = ""; //TODO 添加本地文件存储
		}else {
			url = storageService.uploadSuffix(file.getBytes(), suffix);
			
		}
		return new ObjectRestResponse<String>().data(url);
	}

	/**
	 * 多文件上传
	 */
	@RequestMapping("/uploads")
	public ObjectRestResponse<String> uploads(@RequestParam("files") MultipartFile[] files) throws Exception {
		if (files == null || files.length < 0) {
			throw new BusinessException("上传文件不能为空");
		}
		//上传文件
		CloudStorageService storageService = ossFactory.build();
		List<String>  urls = new ArrayList<>() ;
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String url = storageService.uploadSuffix(file.getBytes(), suffix);
			urls.add(url);
		}
			
		return new ObjectRestResponse<String>().data(urls.toString());
	}


}
