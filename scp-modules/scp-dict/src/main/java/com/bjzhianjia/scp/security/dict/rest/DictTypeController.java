
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

package com.bjzhianjia.scp.security.dict.rest;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import com.bjzhianjia.scp.security.dict.biz.DictTypeBiz;
import com.bjzhianjia.scp.security.dict.entity.DictType;
import com.bjzhianjia.scp.security.dict.vo.DictTree;

import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("dictType")
@CheckClientToken
@CheckUserToken
public class DictTypeController extends BaseController<DictTypeBiz, DictType,String> {
	
	@Autowired
	private DictTypeBiz dictTypeBiz;
	
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<DictTree> getTree() {
        List<DictType> dictTypes = this.baseBiz.selectListAll();
        List<DictTree> trees = new ArrayList<>();
        dictTypes.forEach(dictType -> {
            trees.add(new DictTree(dictType.getId(), dictType.getParentId(), dictType.getName(),dictType.getCode()));
        });
        return TreeUtil.bulid(trees, "-1", null);
    }
    
    @RequestMapping(value = "update",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<DictType> update(@RequestBody DictType entity){
    	ObjectRestResponse<DictType> result =  new ObjectRestResponse<DictType>();
    	if(entity != null && entity.getId() != null) {
    		//code不进行修改
    		String code = entity.getCode();
        	entity.setCode(null);
        	dictTypeBiz.updata(entity);
        	
        	//修改完成返回code
        	entity.setCode(code);
        	result.data(entity);
    	}else {
    		result.setStatus(400);
    	}
        return result;
    }
    
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<DictType> add(@RequestBody DictType entity){
    	ObjectRestResponse<DictType> result =  new ObjectRestResponse<DictType>();
    	if(entity != null) {
    		entity.setName(entity.getName().trim());
    		entity.setCode(entity.getCode().trim());
    		//code唯一
    		int count = dictTypeBiz.selByCode(entity.getCode());
    		if(count == 0) {
    			dictTypeBiz.insertSelective(entity);
    			result.setData(entity);
    		}else {
    			result.setStatus(400);
    			result.setMessage("code已存在！");
    		}
    	}
        return result;
    }
    
    @RequestMapping(value = "delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<DictType> del(@PathVariable("id") String id){
    	ObjectRestResponse<DictType> result =  new ObjectRestResponse<>();
    	if(StringUtils.isNotBlank(id)) {
    		DictType dictType = dictTypeBiz.selectById(id);
    		if(dictType != null){
            	dictType.setIsDeleted("1");
            	dictTypeBiz.delete(dictType);
    		}else {
    			result.setStatus(400);
    		}
    	}else {
    		result.setStatus(400);
    	}
        return result;
    }
}