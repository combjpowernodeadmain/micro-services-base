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
     * openOffice所在主机地址
     */
    @Value("${openOffice.host}")
    private String openOfficeHost;

    /**
     * openOffice端口号
     */
    @Value("${openOffice.port}")
    private String openOfficePort;

    /**
     * 目的文书路径
     */
    @Value("${writsInstances.destFilePath}")
    private String destFilePath;

    @Value("${writsTemplates.src}")
    private String templateSrcPath;

    /**
     * 网格等级--自然网格
     */
    @Value("${areaGrid.gridLevel.zrwg}")
    private String lowestGridLevel;

    /**
     * 网格等级--自然网格
     */
    @Value("${lawTasks.todo}")
    private String lawTasksToDo;

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

    public String getDestFilePath() {
        return destFilePath;
    }

    public String getTemplateSrcPath() {
        return templateSrcPath;
    }

    public String getOpenOfficeHost() {
        return openOfficeHost;
    }

    public String getOpenOfficePort() {
        return openOfficePort;
    }

    public String getLowestGridLevel() {
        return lowestGridLevel;
    }

    public String getLawTasksToDo() {
        return lawTasksToDo;
    }
}
