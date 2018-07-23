package com.bjzhianjia.scp.cgp.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;

/**
 * 字典服务
 * @author zzh
 *
 */
@FeignClient(value = "scp-dict",configuration = FeignApplyConfiguration.class)
public interface DictFeign {

	@RequestMapping(value = "/dictValue/feign/{code}",method = RequestMethod.GET)
    public Map<String,String> getDictValues(@PathVariable("code") String code);
	
	@RequestMapping(value = "/dictValue/feign/ids/{code}",method = RequestMethod.GET)
    public Map<String,String> getDictIds(@PathVariable("code") String code);
	
	@RequestMapping(value = "/dictValue/feign/id/{id}",method = RequestMethod.GET)
	public Map<String,String> getDictValueByID(@PathVariable("id") String id);
}
