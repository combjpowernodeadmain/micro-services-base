package com.bjzhianjia.scp.cgp.config;

import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 现场检查
     */
    @Value("${WritsTemplateIds.SPOT_CHECK}")
    private String spotCheck;

    @Value("${WritsTemplateIds.SPOT_CHECK_CODES}")
    private String spotCheckCodes;

    /**
     * 现场处罚
     */
    @Value("${WritsTemplateIds.SPOT_PUNISHMENT}")
    private String SpotPunishment;

    @Value("${WritsTemplateIds.SPOT_PUNISHMENT_CODES}")
    private String SpotPunishmentCodes;

    /**
     * 责令改正处理
     */
    @Value("${WritsTemplateIds.RECTIFICATION}")
    private String rectification;

    @Value("${WritsTemplateIds.RECTIFICATION_CODES}")
    private String rectificationCodes;

    /**
     * 责令改正处理
     */
    @Value("${WritsTemplateIds.INFORM}")
    private String inform;

    @Value("${WritsTemplateIds.INFORM_CODES}")
    private String informCode;

    /**
     * 文件原文件路
     */
    @Value("${writsInstances.srcPath}")
    private String srcPath;

    /**
     * 目的文书名
     */
    @Value("${writsInstances.destFileName}")
    private String destFileName;

    /**
     * 目的文书路径
     */
    @Value("${writsInstances.tempWordPath}")
    private String tempWordPath;

    /**
     * 现场检查
     * 
     * @author 尚
     * @return
     */
    public String getSpotCheck() {
        return spotCheck;
    }

    public String getSpotCheckCodes() {
        return spotCheckCodes;
    }

    public String getSpotPunishment() {
        return SpotPunishment;
    }

    public String getSpotPunishmentCodes() {
        return SpotPunishmentCodes;
    }

    public String getRectification() {
        return rectification;
    }

    public String getRectificationCodes() {
        return rectificationCodes;
    }

    public String getInform() {
        return inform;
    }

    public String getInformCode() {
        return informCode;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public String getDestFileName() {
        return destFileName;
    }

    public String getTempWordPath() {
        return tempWordPath;
    }
}
