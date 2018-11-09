package com.bjzhianjia.scp.cgp.util;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * @author 尚
 */
@Component
public class DocDownUtil{
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 按路径+文件名下载文件
     * @param filePath
     * @param filename
     * @return
     */
    public ResponseEntity<?> getFile(String filePath,String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(filePath, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 按全路径下载文件
     * @param fullFilePath
     * @return
     */
    public ResponseEntity<?> getFile(String fullFilePath) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + fullFilePath));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
