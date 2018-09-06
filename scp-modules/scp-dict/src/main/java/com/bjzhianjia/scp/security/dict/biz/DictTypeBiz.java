package com.bjzhianjia.scp.security.dict.biz;

import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.bjzhianjia.scp.security.dict.entity.DictType;
import com.bjzhianjia.scp.security.dict.mapper.DictTypeMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

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
public class DictTypeBiz extends BusinessBiz<DictTypeMapper,DictType> {
	
	@Autowired
	private DictTypeMapper dictTypeMapper;
	
    @Override
    public void insertSelective(DictType entity) {
        entity.setId(UUIDUtils.generateUuid());
        super.insertSelective(entity);
    }
    /**
     * 根据主键更新属性不为null的值
     * @param entity
     */
    public void updata(DictType entity) {
    	dictTypeMapper.updateByPrimaryKeySelective(entity);
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
    	criteria.andEqualTo("code",code);
    	return dictTypeMapper.selectCountByExample(example);
    }
    
    /**
     * 根据主键更新属性不为null的值
     * @param entity
     */
    public void delete(DictType entity) {
    	dictTypeMapper.updateByPrimaryKeySelective(entity);
    }
    
    /**
     * 查询非删除的列表
     * @return
     */
    public List<DictType> getListAll() {
        Example example = new Example(DictType.class);
        Criteria criteria = example.createCriteria();
        //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");
        return dictTypeMapper.selectByExample(example);
    }
    
}