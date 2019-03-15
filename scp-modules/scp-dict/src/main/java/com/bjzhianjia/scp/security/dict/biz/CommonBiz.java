package com.bjzhianjia.scp.security.dict.biz;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.dict.entity.DictValue;
import com.bjzhianjia.scp.security.dict.utils.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author By 尚
 * @date 2019/3/8 10:47
 */
@Service
public class CommonBiz {

    @Autowired
    private Environment environment;

    @Autowired
    private DictValueBiz dictValueBiz;

    /**
     * 生成指挥中心图片标题
     *
     * @return
     */
    public BufferedImage generateImg() throws Exception {
        Example example = new Example(DictValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andLike("code", "%root_system_commonCenterTitle%");
        List<DictValue> dictValues = dictValueBiz.selectByExample(example);

        String textInProfile =
            new String(environment.getProperty("commandCenterTitle.text").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
        int frontSizeInProfile = Integer.parseInt(
            new String(environment.getProperty("commandCenterTitle.frontSize").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8));
        int widthInProfile = Integer.parseInt(
            new String(environment.getProperty("commandCenterTitle.width").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8));
        int heightInProfile = Integer.parseInt(
            new String(environment.getProperty("commandCenterTitle.heigth").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8));
        String frontColorInProfile =
            new String(environment.getProperty("commandCenterTitle.frontColor").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
        String frontType =
            new String(environment.getProperty("commandCenterTitleFrontType").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

        if (BeanUtils.isNotEmpty(dictValues)) {
            try {
                Map<String, DictValue> dictValueCollect =
                    dictValues.stream().collect(Collectors.toMap(DictValue::getCode, dictvalue -> dictvalue));

                textInProfile =
                    StringUtils.isBlank(dictValueCollect.get("root_system_commonCenterTitle_title").getLabelDefault()) ?
                        textInProfile :
                        dictValueCollect.get("root_system_commonCenterTitle_title").getLabelDefault();

                frontSizeInProfile = StringUtils
                    .isBlank(dictValueCollect.get("root_system_commonCenterTitle_frontSize").getLabelDefault()) ?
                    frontSizeInProfile :
                    Integer.parseInt(dictValueCollect.get("root_system_commonCenterTitle_frontSize").getLabelDefault());

                widthInProfile = StringUtils
                    .isBlank(dictValueCollect.get("root_system_commonCenterTitle_imgWidth").getLabelDefault()) ?
                    widthInProfile :
                    Integer.parseInt(dictValueCollect.get("root_system_commonCenterTitle_imgWidth").getLabelDefault());

                heightInProfile = StringUtils
                    .isBlank(dictValueCollect.get("root_system_commonCenterTitle_imgHeight").getLabelDefault()) ?
                    heightInProfile :
                    Integer.parseInt(dictValueCollect.get("root_system_commonCenterTitle_imgHeight").getLabelDefault());

                frontColorInProfile = StringUtils
                    .isBlank(dictValueCollect.get("root_system_commonCenterTitle_frontColor").getLabelDefault()) ?
                    frontColorInProfile :
                    dictValueCollect.get("root_system_commonCenterTitle_frontColor").getLabelDefault();

                if (StringUtils.startsWith(frontColorInProfile, "#")) {
                    frontColorInProfile = StringUtils.removeStart(frontColorInProfile, "#");
                }

            } catch (Exception e) {
                throw new Exception("指挥中心标题配置错误");
            }
        }

        Color color = new Color(Integer.parseInt(frontColorInProfile, 16));

        Font font = new Font(frontType, Font.BOLD, frontSizeInProfile);//字体

        return ImageUtil.generateImg(widthInProfile, heightInProfile, textInProfile, color, font, 0d, 1.0f);
    }

    /**
     * 前端默认加载项
     * @return
     */
    public String getWebDefault() throws Exception {
        // root_biz_webDefault
        Example example = new Example(DictValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andLike("code", "%root_system_webDefault%");
        List<DictValue> dictValues = dictValueBiz.selectByExample(example);

        String downloadUrlPre =
            new String(environment.getProperty("downloadUrlPre").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
        String mapcenter = new String(environment.getProperty("mapcenter").getBytes(StandardCharsets.ISO_8859_1),
            StandardCharsets.UTF_8);
        String platform = new String(environment.getProperty("platform").getBytes(StandardCharsets.ISO_8859_1),
            StandardCharsets.UTF_8);

        if (BeanUtils.isNotEmpty(dictValues)) {
            Map<String, DictValue> dictValueMap =
                dictValues.stream().collect(Collectors.toMap(DictValue::getCode, dictValue -> dictValue));

            downloadUrlPre =
                StringUtils.isBlank(dictValueMap.get("root_system_webDefault_downloadUrlPre").getLabelDefault()) ?
                    downloadUrlPre :
                    dictValueMap.get("root_system_webDefault_downloadUrlPre").getLabelDefault();
            mapcenter = StringUtils.isBlank(dictValueMap.get("root_system_webDefault_mapcenter").getLabelDefault()) ?
                mapcenter :
                dictValueMap.get("root_system_webDefault_mapcenter").getLabelDefault();
            platform = StringUtils.isBlank(dictValueMap.get("root_system_webDefault_platform").getLabelDefault()) ?
                platform :
                dictValueMap.get("root_system_webDefault_platform").getLabelDefault();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("downloadUrlPre", downloadUrlPre);
        jsonObject.put("mapcenter", mapcenter);
        jsonObject.put("platform", platform);
        return jsonObject.toJSONString();
    }
}
