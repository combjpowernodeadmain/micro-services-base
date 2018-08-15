
/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.security.dict.biz;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.bjzhianjia.scp.security.dict.entity.DictValue;
import com.bjzhianjia.scp.security.dict.mapper.DictValueMapper;

import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
@Service
public class DictValueBiz extends BusinessBiz<DictValueMapper,DictValue> {
    @Override
    public void insertSelective(DictValue entity) {
        entity.setId(UUIDUtils.generateUuid());
        super.insertSelective(entity);
    }
    
    public Map<String, String> getDictValues(String id){
    	List<String> ids=new ArrayList<>(); 
    	String[] split = id.split(",");
    	for (String string : split) {
			ids.add(string);
		}
    	Example example=new Example(DictValue.class);
    	Example.Criteria criteria=example.createCriteria();
    	criteria.andIn("id", ids);
    	example.setOrderByClause("order_num");//按order_num升序排列
    	
    	Map<String, String> result=new HashMap<>();
    	
        List<DictValue> dictValueList = mapper.selectByExample(example);
    	
    	if(dictValueList!=null) {
    		for (DictValue dictValue : dictValueList) {
    			result.put(dictValue.getId(), JSON.toJSONString(dictValue));
			}
    		return result;
    	}
    	return null;
    }
    
    /**
     * 按code查询字典值
     * @author 尚
     * @param code 查询条件
     * @param isLike 是否按模糊查询
     * @return
     */
    public Map<String, String> getDictValues(String code,boolean isLike){
    	Example example=new Example(DictValue.class);
    	Example.Criteria criteria=example.createCriteria();
    	
    	if(isLike) {
    		criteria.andNotLike("code", code);
    	}else {
    		criteria.andEqualTo("code",code);
    	}
    	
    	example.setOrderByClause("order_num");//按order_num升序排列
    	
    	Map<String, String> result=new HashMap<>();
    	List<DictValue> dictValueList = mapper.selectByExample(example);
    	
    	if(dictValueList!=null) {
    		for (DictValue dictValue : dictValueList) {
    			result.put(dictValue.getId(), JSON.toJSONString(dictValue));
    		}
    		return result;
    	}
    	return null;
    }
}