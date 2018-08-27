
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
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.dict.biz.DictValueBiz;
import com.bjzhianjia.scp.security.dict.entity.DictValue;

import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("dictValue")
@CheckClientToken
@CheckUserToken
@Api(tags = "字典值服务", description = "字典值服务")
public class DictValueController extends BaseController<DictValueBiz, DictValue, String> {
	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/type/{code}", method = RequestMethod.GET)
	public TableResultResponse<DictValue> getDictValueByDictTypeCode(@PathVariable("code") String code) {
		Example example = new Example(DictValue.class);
		example.createCriteria().andLike("code", code + "%");
		List<DictValue> dictValues = this.baseBiz.selectByExample(example).stream().sorted(new Comparator<DictValue>() {
			@Override
			public int compare(DictValue o1, DictValue o2) {
				return o1.getOrderNum() - o2.getOrderNum();
			}
		}).collect(Collectors.toList());
		return new TableResultResponse<DictValue>(dictValues.size(), dictValues);
	}

	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/feign/{code}", method = RequestMethod.GET)
	public Map<String, String> getDictValueByCode(@PathVariable("code") String code) {
		Example example = new Example(DictValue.class);
		example.createCriteria().andLike("code", code + "%");

		// 按order_num升序排列=============By尚
		example.setOrderByClause("order_num");

		List<DictValue> dictValues = this.baseBiz.selectByExample(example);
		Map<String, String> result = dictValues.stream()
				.collect(Collectors.toMap(DictValue::getValue, DictValue::getLabelDefault));
		return result;
	}

	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/feign/ids/{code}", method = RequestMethod.GET)
	public Map<String, String> getDictIdByCode(@PathVariable("code") String code) {
		Example example = new Example(DictValue.class);
		example.createCriteria().andLike("code", code + "%");

		// 按order_num升序排列=============By尚
		example.setOrderByClause("order_num");

		List<DictValue> dictValues = this.baseBiz.selectByExample(example);
		Map<String, String> result = dictValues.stream()
				.collect(Collectors.toMap(DictValue::getId, DictValue::getLabelDefault));
		return result;
	}

	/**
	 * 按ID查询特定的字典记录<br/>
	 * 返回Map集合，集合key为字典记录ID，集合value为字典记录转换后的JSON字符串
	 * 
	 * @author 尚
	 * @param id
	 * @return
	 */
	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/feign/id/{id}", method = RequestMethod.GET)
	public Map<String, String> getDictIdById(@PathVariable("id") String id) {
//    	DictValue dictValue = this.baseBiz.selectById(id);
//    	
//    	if(dictValue!=null) {
//    		Map<String, String> result=new HashMap<>();
//    		result.put(dictValue.getId(), JSON.toJSONString(dictValue));
//    		return result;
//    	}
//    	return null;
		return this.baseBiz.getDictValues(id);
	}
	
	 /**
     * 按code查询字典值
     * @author 尚
     * @param code 查询条件
     * @param isLike 是否按模糊查询
     * @return
     */
	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/feign/values/{code}", method = RequestMethod.GET)
	public Map<String, String> getDictIdByCode(@PathVariable("code") String code,@RequestParam(value="isLike",defaultValue="false")boolean isLike) {
		Map<String, String> result = this.baseBiz.getDictValues(code, isLike);
		return result;
	}
	/**
	 *   通过code查询
	 * @param code
	 * 		数据字典code
	 * @return
	 */
	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/feign/code/{code}", method = RequestMethod.GET)
	public Map<String, String> getByCode(@PathVariable("code") String code) {
		Example example = new Example(DictValue.class);
		example.createCriteria().andLike("code", code + "%");

		example.setOrderByClause("order_num");

		List<DictValue> dictValues = this.baseBiz.selectByExample(example);
		Map<String, String> result = dictValues.stream()
				.collect(Collectors.toMap(DictValue::getCode, DictValue::getLabelDefault));
		return result;
	}
	
	
	/**
	 * 按code进行查询<br/>
	 * 查询语句为code in ('code1','code1','code1','code1')
	 * 
	 * @author 尚
	 * @param codeList   查询 条件,字符串表示的code集合，多个code之间用逗号隔开<br/>
	 * 					如："c1,c2,c3,c4",字符串中不要有重复的code值
	 * @return "${code}":"${label_default}"健值对
	 */
	@RequestMapping(value = "/feign/code/in/{codes}", method = RequestMethod.GET)
	public Map<String, String> getByCodeIn(@PathVariable("codes") String codeList){
		if(codeList==null||codeList.isEmpty()) {
			return null;
		}
		
		Example example=new Example(DictValue.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andIn("code", Arrays.asList(codeList.split(",")));
		
		example.setOrderByClause("order_num");
		List<DictValue> dictValues = this.baseBiz.selectByExample(example);
		
		Map<String, String> result = dictValues.stream().collect(Collectors.toMap(DictValue::getCode, DictValue::getLabelDefault));
		return result;
	}
	
	/**
	 * 按code查询字典值
	 * 
	 * @author 尚
	 * @param code code 查询条件
	 * @return {"id":"对应ID值","code":"对应code值","labelDefault":"对应labelDefault值"}
	 */
	@RequestMapping(value = "/list/{code}", method = RequestMethod.GET)
	public List<Map<String, String>> queryDictValueByCode(@PathVariable(value="code")String code) {
		List<Map<String, String>> result = this.baseBiz.getDictValueByCode(code);
		return result;
	}
}