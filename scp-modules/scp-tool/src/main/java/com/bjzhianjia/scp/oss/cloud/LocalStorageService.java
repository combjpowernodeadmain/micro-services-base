package com.bjzhianjia.scp.oss.cloud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.bjzhianjia.scp.config.CloudStorageConfig;
import com.bjzhianjia.scp.oss.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地存储
 * 
 * @author lutuo001
 *
 */
@Slf4j
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
	/**
	 * 保存本地文件
	 *
	 * @param multipartFile 上传文件对象
	 * @return prefix 前缀
	 * 格式：xxx/xxxx.xxx(E:/data/text.txt)
	 */
	public static String saveLocalStorage(MultipartFile multipartFile, String prefix) {
		//上传文件
		String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
		String filePath = FileUtil.getPath(suffix);
		try {
			File file = new File(prefix + filePath);
			File fileParent = file.getParentFile();
			//判断目录/文件，没有则创建
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			multipartFile.transferTo(file);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("saveFile file fail,  LocalStorage throw error.", e);
			return null;
		}
		return filePath;
	}
}
