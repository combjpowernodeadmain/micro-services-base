package com.bjzhianjia.scp.cgp.util;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bjzhianjia.scp.cgp.config.PropertiesConfig;


/**
 * @author 尚
 */
@Component
public class DocDownUtil{
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private PropertiesConfig propertiesConfig;
    
    public ResponseEntity<?> getFile(String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(propertiesConfig.getDestFilePath(), filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
