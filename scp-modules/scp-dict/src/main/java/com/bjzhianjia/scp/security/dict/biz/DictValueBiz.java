
package com.bjzhianjia.scp.security.dict.biz;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.bjzhianjia.scp.security.dict.entity.DictType;
import com.bjzhianjia.scp.security.dict.entity.DictValue;
import com.bjzhianjia.scp.security.dict.mapper.DictValueMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0
 */
@Service
public class DictValueBiz extends BusinessBiz<DictValueMapper, DictValue> {
	
	@Autowired
	private DictValueMapper dictValueMapper;
	
	
	@Override
	public void insertSelective(DictValue entity) {
		entity.setId(UUIDUtils.generateUuid());
		super.insertSelective(entity);
	}

	public Map<String, String> getDictValues(String id) {
		List<String> ids = new ArrayList<>();
		String[] split = id.split(",");
		for (String string : split) {
			ids.add(string);
		}
		Example example = new Example(DictValue.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", ids);
	    //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");
		example.setOrderByClause("order_num");// 按order_num升序排列

		Map<String, String> result = new HashMap<>();

		List<DictValue> dictValueList = mapper.selectByExample(example);

		if (dictValueList != null) {
			for (DictValue dictValue : dictValueList) {
				result.put(dictValue.getId(), JSON.toJSONString(dictValue));
			}
			return result;
		}
		return null;
	}

	/**
	 * 按code查询字典值
	 * 
	 * @author 尚
	 * @param code   查询条件
	 * @param isLike 是否按模糊查询
	 * @return
	 */
	public Map<String, String> getDictValues(String code, boolean isLike) {
		Example example = new Example(DictValue.class);
		Example.Criteria criteria = example.createCriteria();

		if (isLike) {
			criteria.andLike("code", "%" + code + "%");
		} else {
			criteria.andEqualTo("code", code);
		}
	      //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");
		example.setOrderByClause("order_num");// 按order_num升序排列

		Map<String, String> result = new HashMap<>();
		List<DictValue> dictValueList = mapper.selectByExample(example);

		if (dictValueList != null) {
			for (DictValue dictValue : dictValueList) {
				result.put(dictValue.getId(), JSON.toJSONString(dictValue));
			}
			return result;
		}
		return null;
	}

	/**
	 * 按code查询字典值
	 * 
	 * @author 尚
	 * @param code code 查询条件
	 * @return {"id":"对应ID值","code":"对应code值","labelDefault":"对应labelDefault值"}
	 */
	public List<Map<String, String>> getDictValueByCode(String code) {
		List<Map<String, String>> resultList=new ArrayList<>();
		
		Example example = new Example(DictValue.class);
		Example.Criteria criteria = example.createCriteria();
	    //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");
		criteria.andLike("code", "%" + code + "%");
		example.setOrderByClause("order_num");// 按order_num升序排列

		List<DictValue> dictValueList = mapper.selectByExample(example);
		
		if (dictValueList != null) {
			for (DictValue dictValue : dictValueList) {
				Map<String, String> result = new HashMap<>();
				result.put("id", dictValue.getId());
				result.put("code", dictValue.getCode());
				result.put("labelDefault", dictValue.getLabelDefault());
				
				resultList.add(result);
			}
			return resultList;
		}
		return null;
	}
	
	  /**
     * 根据主键更新属性不为null的值
     * @param entity
     */
    public void updata(DictValue entity) {
    	dictValueMapper.updateByPrimaryKeySelective(entity);
    }
    /**
     * 根据code条件进行查询总数
     * @param code
     * 		  编码
     * @return
     */
    public int selByCode(String code) {
    	Example example = new Example(DictType.class);
    	Criteria criteria = example.createCriteria();
        //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");
    	criteria.andEqualTo("code",code);
    	return dictValueMapper.selectCountByExample(example);
    }
    
    /**
     * 根据主键更新属性不为null的值
     * @param entity
     */
    public void delete(DictValue entity) {
    	dictValueMapper.updateByPrimaryKeySelective(entity);
    }
}