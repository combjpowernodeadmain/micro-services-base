package com.bjzhianjia.scp.security.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 尚
 */
@Component
@PropertySource(value = "classpath:properties/fields.properties")
@ConfigurationProperties
public class PropertiesConfig {

}
