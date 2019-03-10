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
        if (BeanUtils.isNotEmpty(dictValues)) {
            try {
                Map<String, DictValue> dictValueCollect =
                    dictValues.stream().collect(Collectors.toMap(DictValue::getCode, dictvalue -> dictvalue));

                String text = dictValueCollect.get("root_system_commonCenterTitle_title").getLabelDefault();
                int frontSize =
                    Integer.parseInt(dictValueCollect.get("root_system_commonCenterTitle_frontSize").getLabelDefault());
                int width =
                    Integer.parseInt(dictValueCollect.get("root_system_commonCenterTitle_imgWidth").getLabelDefault());
                int heigth =
                    Integer.parseInt(dictValueCollect.get("root_system_commonCenterTitle_imgHeight").getLabelDefault());
                String frontColor = dictValueCollect.get("root_system_commonCenterTitle_frontColor").getLabelDefault();

                if (StringUtils.startsWith(frontColor, "#")) {
                    frontColor = StringUtils.removeStart(frontColor, "#");
                }
                Color color = new Color(Integer.parseInt(frontColor, 16));

                String frontType = new String(
                    environment.getProperty("commandCenterTitleFrontType").getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8);

                Font font = new Font(frontType, Font.BOLD, frontSize);//字体

                return ImageUtil.generateImg(width, heigth, text, color, font, 0d, 1.0f);
            } catch (Exception e) {
                throw new Exception("指挥中心标题配置错误");
            }
        } else {
            throw new Exception("指挥中心标题配置错误");
        }
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

        if(BeanUtils.isNotEmpty(dictValues)){
            Map<String, DictValue> dictValueMap =
                dictValues.stream().collect(Collectors.toMap(DictValue::getCode, dictValue -> dictValue));

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("downloadUrlPre",dictValueMap.get("root_system_webDefault_downloadUrlPre").getLabelDefault());
            jsonObject.put("mapcenter",dictValueMap.get("root_system_webDefault_mapcenter").getLabelDefault());
            jsonObject.put("platform",dictValueMap.get("root_system_webDefault_platform").getLabelDefault());

            return jsonObject.toJSONString();
        }else{
            throw new Exception("前端默认加载项配置错误");
        }
    }
}
