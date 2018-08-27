package com.bjzhianjia.scp.cgp.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;

/**
 * 字典服务
 * 
 * @author zzh
 *
 */
@FeignClient(value = "scp-dict", configuration = FeignApplyConfiguration.class)
public interface DictFeign {

	/**
	 * 按数据字典的code字段进行模糊查询<br/>
	 * 如查询业务条线时，code值为“root_biz_type”
	 * 
	 * @param code
	 * @return "${value}":"${labelDefault}"健值对
	 */
	@Deprecated
	@RequestMapping(value = "/dictValue/feign/{code}", method = RequestMethod.GET)
	public Map<String, String> getDictValues(@PathVariable("code") String code);

	/**
	 * 按数据字典的code字段进行模糊查询<br/>
	 * 如查询业务条线时，code值为“root_biz_type”
	 * 
	 * @param code
	 * @return "${id}":"${labelDefault}"健值对
	 */
	@Deprecated
	@RequestMapping(value = "/dictValue/feign/ids/{code}", method = RequestMethod.GET)
	public Map<String, String> getDictIds(@PathVariable("code") String code);

	/**
	 * 按ID进行查询<br/>
	 * ID条件可有多个，多个ID之间用“,”分隔
	 * 
	 * @author 尚
	 * @param id
	 * @return "${id}":"${记录对应实体的JSON格式字符}"健值对
	 */
	@Deprecated
	@RequestMapping(value = "/dictValue/feign/id/{id}", method = RequestMethod.GET)
	public Map<String, String> getDictValueByID(@PathVariable("id") String id);

	/**
	 * 按code进行查询<br/>
	 * 
	 * @author 尚
	 * @param code   查询 条件
	 * @param isLike 是否按模糊查询
	 * @return "${id}":"${记录对应实体的JSON格式字符}"健值对
	 */
	@Deprecated
	@RequestMapping(value = "/dictValue/feign/values/{code}", method = RequestMethod.GET)
	public Map<String, String> getDictIdByCode(@PathVariable("code") String code,
			@RequestParam(value = "isLike", defaultValue = "false") boolean isLike);
	
	/**
	 * 按code进行查询<br/>
	 * 
	 * @author chenshuai
	 * @param code   查询 条件
	 * @return "${code}":"${label_default}"健值对
	 */
	@RequestMapping(value = "/dictValue/feign/code/{code}", method = RequestMethod.GET)
	public Map<String, String> getByCode(@PathVariable("code") String code) ;
	
	
	/**
	 * 按code进行查询<br/>
	 * 查询语句为code in ('code1','code1','code1','code1')
	 * 
	 * @author 尚
	 * @param codeList   查询 条件,字符串表示的code集合，多个code之间用逗号隔开<br/>
	 * 					如："c1,c2,c3,c4"
	 * @return "${code}":"${label_default}"健值对
	 */
	@RequestMapping(value = "/dictValue/feign/code/in/{codes}", method = RequestMethod.GET)
	public Map<String, String> getByCodeIn(@PathVariable("codes") String codeList) ;
}
