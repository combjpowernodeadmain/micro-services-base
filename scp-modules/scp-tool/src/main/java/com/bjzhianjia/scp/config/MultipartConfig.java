package com.bjzhianjia.scp.config;

import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.*;

import javax.servlet.*;

/**
 * ${type_name} 类描述.
 * <p>
 * ${tags}
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/10/30          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@Configuration
public class MultipartConfig {
    /**
     * 配置上传文件大小的配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("102400KB");
        /// 总上传数据大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
}
