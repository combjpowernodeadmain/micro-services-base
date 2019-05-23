package com.bjzhianjia.scp.security.admin.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;

import java.util.Map;

/**
 * @author scp
 * @create 2018/2/1.
 */
@FeignClient(value = "scp-dict",configuration = FeignApplyConfiguration.class)
public interface DictFeign {
    /**
     * 获取字典对对应值
     * @param code
     * @return
     */
    @RequestMapping(value = "/dictValue/feign/{code}",method = RequestMethod.GET)
    public Map<String,String> getDictValues(@PathVariable("code") String code);

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
     * @param codeList
     *            查询 条件,字符串表示的code集合，多个code之间用逗号隔开<br/>
     *            如："c1,c2,c3,c4"
     * @return "${code}":"${label_default}"健值对
     */
    @RequestMapping(value = "/dictValue/feign/code/in/{codes}", method = RequestMethod.GET)
    Map<String, String> getByCodeIn(@PathVariable("codes") String codeList);
}
