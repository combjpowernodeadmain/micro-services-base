package com.bjzhianjia.scp.cgp.biz;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bjzhianjia.scp.cgp.util.BeanUtil;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.mapper.EnterpriseInfoMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;

/**
 * 企业信息
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Service
public class EnterpriseInfoBiz extends BusinessBiz<EnterpriseInfoMapper,EnterpriseInfo> {
	/**
	 * 按条件查询
	 * @author 尚
	 * @param condition 封装条件的MAP集合，key:条件名，value:条件值
	 * @return
	 */
	public List<EnterpriseInfo> getByMap(Map<String, Object> conditions ){
		Example example=new Example(EnterpriseInfo.class);
		Example.Criteria criteria=example.createCriteria();
		
		Set<String> keySet = conditions.keySet();
		for (String key : keySet) {
			criteria.andEqualTo(key, conditions.get(key));
		}
		
		List<EnterpriseInfo> result = this.mapper.selectByExample(example);
		return result;
	}

	/**
	 *注册资本 清空 为 null
	 *
	 * @param id
	 */
	public void updatCapitalIsNull(Integer id){
		if(BeanUtil.isNotEmpty(id)){
			this.mapper.updatCapitalIsNull(id);
		}
	}
}