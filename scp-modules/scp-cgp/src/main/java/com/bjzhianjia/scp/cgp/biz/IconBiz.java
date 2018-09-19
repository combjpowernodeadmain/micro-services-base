package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.Icon;
import com.bjzhianjia.scp.cgp.mapper.IconMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
 * IconBiz 图标管理逻辑层.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月19日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
@Service
public class IconBiz extends BusinessBiz<IconMapper,Icon> {
    
    @Autowired
    private IconMapper iconMapper;
    
    /**
     * 通过图标分组查询
     * @param groupCode
     *          图标分组（数据字典code）
     * @return
     */
    public List<Map<String,Object>> getIconByGroupCode(String groupCode){
        List<Map<String,Object>> result = new ArrayList<>();
        
        Example example = new Example(Icon.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", groupCode);
        criteria.andEqualTo("isDeleted", "0");
        
        List<Icon> dataList = iconMapper.selectByExample(example);
        
        if(BeanUtil.isNotEmpty(dataList)) {
            Map<String,Object> map = null;
            for(Icon data : dataList) {
                map = new HashMap<>();
                map.put("id",data.getId());
                map.put("iconValue",data.getIconValue());
                map.put("title",data.getIconTitle());
                map.put("typeCode", data.getTypeCode());
                result.add(map);
            }
        }
        return result;
    }
}