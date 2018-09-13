package com.bjzhianjia.scp.oss.cloud;

import java.io.InputStream;

import com.bjzhianjia.scp.config.CloudStorageConfig;

/**
 * 本地存储
 * 
 * @author lutuo001
 *
 */
public class LocalStorageService extends CloudStorageService {

	
	
	public LocalStorageService(CloudStorageConfig config) {
		this.config = config;
	}
	
	@Override
	public String upload(byte[] data, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadSuffix(byte[] data, String suffix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadSuffix(InputStream inputStream, String suffix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return true;
	}

}
