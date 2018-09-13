package com.bjzhianjia.scp.oss.cloud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.bjzhianjia.scp.config.CloudStorageConfig;

import lombok.extern.slf4j.Slf4j;


/**
 * fastdfs存储
 * 
 * @author lutuo001
 *
 */
@Slf4j
public class FastdfsStorageService extends CloudStorageService {

	private TrackerClient client;
	private TrackerServer trackerServer;
	private StorageServer storageServer;
	
	private StorageClient storageClient;

	public FastdfsStorageService(CloudStorageConfig config) {
		this.config = config;
		init();
	}
	
	
	private void init() {
		log.info("Init the fastdfs properties .");
		try {
			Properties fdfs = new Properties();
			
			String charset = this.config.getFdfsCharset();
			fdfs.put(ClientGlobal.CONF_KEY_CHARSET, StringUtils.isEmpty(charset) ? 
					"UTF-8": charset);
			
			String connectTimeout = this.config.getFdfsConnectTimeout();
			fdfs.put(ClientGlobal.CONF_KEY_CONNECT_TIMEOUT, 
					StringUtils.isEmpty(connectTimeout) ? "5" : connectTimeout);
			
			String httpAntiStealToken = this.config.getFdfsHttpAntiStealToken();
			fdfs.put(ClientGlobal.CONF_KEY_HTTP_ANTI_STEAL_TOKEN, 
					StringUtils.isEmpty(httpAntiStealToken) ? "no" : httpAntiStealToken);
			
			String httpSecretKey = this.config.getFdfsHttpSecretKey();
			fdfs.put(ClientGlobal.CONF_KEY_HTTP_SECRET_KEY, 
					StringUtils.isEmpty(httpSecretKey) ? "FastDFS1234567890": httpSecretKey);
			
			String trackerPort = this.config.getFdfsHttpTrackerHttpPort();
			fdfs.put(ClientGlobal.CONF_KEY_HTTP_TRACKER_HTTP_PORT, 
					StringUtils.isEmpty(trackerPort) ? "80" : trackerPort);
			
			String trackerServers = this.config.getFdfsTrackerServers();
			fdfs.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, 
					StringUtils.isEmpty(trackerServers) ? "127.0.0.1:22122" : trackerServers);
			
			String networkTimeout = this.config.getFdfsNetworkTimeout();
			fdfs.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, 
					StringUtils.isEmpty(networkTimeout) ? "30" : networkTimeout);
			
			ClientGlobal.initByProperties(fdfs);
			
			this.client = new TrackerClient();
			this.trackerServer = client.getConnection();
			this.storageServer = client.getStoreStorage(trackerServer);
		} catch (IOException | MyException e) {
			e.printStackTrace();
			log.error("Init the fastdfs properties error: {}", e);
		}
	}
	
	
	@Override
	public boolean remove(String key) {

		try {
			this.storageClient = new StorageClient(trackerServer, storageServer);
			String[] groupNameFromPath = getGroupNameFromPath(key);
			int deleteFileFlag = storageClient.delete_file(groupNameFromPath[0], groupNameFromPath[1]);
			if (deleteFileFlag == 0) {
				return true;
			}
			log.info("delete file result: {} ", deleteFileFlag);
		} catch (IOException | MyException e) {
			log.warn("删除文件出错{}", e);
			e.printStackTrace();
			return false;
		} 
		return false;
	}
	
	/**
	 *   移除文件前缀
	 * @param filePath
	 * @return
	 */
	private static String[] getGroupNameFromPath(String filePath) {
		if(StringUtils.isEmpty(filePath)) {
			// 如果为空，返回一个空字符串
			return new String[] {StringUtils.EMPTY,StringUtils.EMPTY};
		}
		
		int indexOf = StringUtils.indexOf(filePath, "/");
		if(indexOf == 0) {
			filePath = StringUtils.removeStart(filePath, "/");
			indexOf = StringUtils.indexOf(filePath, "/");
		}
		return new String[] {StringUtils.substring(filePath, 0, indexOf),
				StringUtils.substring(filePath, indexOf + 1, filePath.length())};
	}
	
	@Override
	public String upload(byte[] data, String path) {
		log.info("This upload method is not implemented");
		return null;
	}

	@Override
	public String uploadSuffix(byte[] data, String suffix) {
		log.info("File length: {}, File ext: {}", data.length, suffix);
		suffix = suffix.substring(1, suffix.length());
		String[] uploadResults = null;
		try {
			NameValuePair[] meta_list = new NameValuePair[1];
			meta_list[0] = new NameValuePair("author", "dev by bjzhianjia");
			
			this.storageClient = new StorageClient(trackerServer, storageServer);
			uploadResults = storageClient.upload_file(data, suffix, meta_list);
		} catch (IOException | MyException e) {
			e.printStackTrace();
			log.error("Upload file by fastdfs throw error.", e);
		}
		
		if(uploadResults == null) {
	        log.error("upload file fail, error code: {}", storageClient.getErrorCode());
	        return null ;
		}
		
		String groupName = uploadResults[0];
	    String remoteFileName = uploadResults[1];
		
	    log.info("upload file successfully!!!" + "group_name: {}, remoteFileName: {}", groupName, remoteFileName);

	    
		return groupName + "/" + remoteFileName;
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		log.info("This upload by inputstream method is not implemented");		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadSuffix(InputStream inputStream, String suffix) {
		log.info("This uploadSuffix by inputstream method is not implemented");
		return null;
	}


}
