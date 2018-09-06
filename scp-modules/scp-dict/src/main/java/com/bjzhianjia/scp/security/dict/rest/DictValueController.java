
package com.bjzhianjia.scp.security.dict.rest;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.dict.biz.DictValueBiz;
import com.bjzhianjia.scp.security.dict.entity.DictValue;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("dictValue")
@CheckClientToken
@CheckUserToken
@Api(tags = "字典值服务", description = "字典值服务")
public class DictValueController extends BaseController<DictValueBiz, DictValue, String> {
	
	@Autowired
	private DictValueBiz dictValueBiz;
	
	
	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/type/{code}", method = RequestMethod.GET)
	public TableResultResponse<DictValue> getDictValueByDictTypeCode(@PathVariable("code") String code) {
		Example example = new Example(DictValue.class);
		Criteria criteria = example.createCriteria();
		criteria.andLike("code", code + "%");
		  //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");
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
	    Criteria criteria = example.createCriteria();
        criteria.andLike("code", code + "%");
          //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");

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
	    Criteria criteria = example.createCriteria();
        criteria.andLike("code", code + "%");
          //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");

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
		Criteria criteria = example.createCriteria();
        criteria.andLike("code", code + "%");
          //0没有删除，1 删除
        criteria.andEqualTo("isDeleted","0");
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
	@IgnoreClientToken
	@IgnoreUserToken
	@RequestMapping(value = "/feign/code/in/{codes}", method = RequestMethod.GET)
	public Map<String, String> getByCodeIn(@PathVariable("codes") String codeList){
		if(codeList==null||codeList.isEmpty()) {
			return null;
		}
		
		Example example=new Example(DictValue.class);
		Criteria criteria = example.createCriteria();
		
		//避免因传重复的code造成查询时条件量较大
		Set<String> codeSet=new HashSet<>();
		String[] split = codeList.split(",");
		for (String string : split) {
			codeSet.add(string);
		}
		criteria.andIn("code", codeSet);
	    //0没有删除，1 删除
	    criteria.andEqualTo("isDeleted","0");
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
	
	@RequestMapping(value = "update",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<DictValue> update(@RequestBody DictValue entity){
    	ObjectRestResponse<DictValue> result =  new ObjectRestResponse<>();
    	if(entity != null && entity.getId() != null) {
    		//code不进行修改
    		String code = entity.getCode();
        	entity.setCode(null);
        	dictValueBiz.updata(entity);
        	
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
    public ObjectRestResponse<DictValue> add(@RequestBody DictValue entity){
    	ObjectRestResponse<DictValue> result =  new ObjectRestResponse<>();
    	if(entity != null) {
    		entity.setValue(entity.getValue().trim());
    		entity.setCode(entity.getCode().trim());
    		//code唯一
    		int count = dictValueBiz.selByCode(entity.getCode());
    		if(count == 0) {
    			dictValueBiz.insertSelective(entity);
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
    public ObjectRestResponse<DictValue> del(@PathVariable("id") String id){
    	ObjectRestResponse<DictValue> result =  new ObjectRestResponse<>();
    	if(StringUtils.isNotBlank(id)) {
    		DictValue dictValue = dictValueBiz.selectById(id);
    		if(dictValue != null){
    			dictValue.setIsDeleted("1");
            	dictValueBiz.delete(dictValue);
    		}else {
    			result.setStatus(400);
    		}
    	}else {
    		result.setStatus(400);
    	}
        return result;
    }
}