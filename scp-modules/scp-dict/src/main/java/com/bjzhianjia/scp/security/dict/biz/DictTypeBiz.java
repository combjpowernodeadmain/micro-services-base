
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

import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.bjzhianjia.scp.security.dict.entity.DictType;
import com.bjzhianjia.scp.security.dict.mapper.DictTypeMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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
}