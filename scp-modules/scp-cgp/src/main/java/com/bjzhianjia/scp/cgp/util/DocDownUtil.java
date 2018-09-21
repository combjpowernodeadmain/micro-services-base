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
    
    public ResponseEntity<?> getFile(String filePath,String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(filePath, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
