package com.bjzhianjia.scp.cgp.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;

/**
 * 管理服务
 * @author zzh
 *
 */
@FeignClient(value = "scp-admin",configuration = FeignApplyConfiguration.class)
public interface AdminFeign {

	@RequestMapping(value = "/depart/getByPK/{id}",method = RequestMethod.GET)
    public Map<String,String> getDepart(@PathVariable(value="id") String id);
	
	@RequestMapping(value = "/user/getByPK/{id}",method = RequestMethod.GET)
	public Map<String,String> getUser(@PathVariable(value="id") String id);
	
	@RequestMapping(value = "/depart/getLayerByPK/{id}",method = RequestMethod.GET)
    public Map<String,String> getLayerDepart(@PathVariable(value="id") String id);
}
