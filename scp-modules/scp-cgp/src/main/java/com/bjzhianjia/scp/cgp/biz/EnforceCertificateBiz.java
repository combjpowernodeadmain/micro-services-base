package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.mapper.EnforceCertificateMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 执法证管理
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Service
public class EnforceCertificateBiz extends BusinessBiz<EnforceCertificateMapper,EnforceCertificate> {
	
	@Autowired
	private EnforceCertificateMapper enforceCertificateMapper;
	
	/**
	 * 根据证件编号获取
	 * @param certCode 证件编号
	 * @return
	 */
	public EnforceCertificate getByCertCode(String certCode) {
		
		Example example = new Example(EnforceCertificate.class);
		
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("certCode", certCode);
		
		List<EnforceCertificate> list = enforceCertificateMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			return null;
		}
		
		EnforceCertificate enforceCertificate = list.get(0);
		
		if(enforceCertificate.getIsDeleted().equals("1")) {
			return null;
		}
		
		return enforceCertificate;
	}
	
	/**
	 * 根据持证人获取
	 * @param holderName 持证人
	 * @return
	 */
	public EnforceCertificate getByHolderName(String holderName) {
		Example example = new Example(EnforceCertificate.class);
		
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("holderName", holderName);
		
		List<EnforceCertificate> list = enforceCertificateMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			return null;
		}
		
		EnforceCertificate enforceCertificate = list.get(0);
		
		if(enforceCertificate.getIsDeleted().equals("1")) {
			return null;
		}
		
		return enforceCertificate;
	}
	
	/**
	 * 根据查询条件搜索
	 * @param eventType
	 * @return
	 */
	public TableResultResponse<EnforceCertificate> getList(int page, int limit, EnforceCertificate enforceCertificate) {
		Example example = new Example(EnforceCertificate.class);
	    Example.Criteria criteria = example.createCriteria();
	    
	    criteria.andEqualTo("isDeleted", "0");
	    if(StringUtils.isNotBlank(enforceCertificate.getHolderName())){
	    	criteria.andEqualTo("holderName", enforceCertificate.getHolderName());
	    }
	    if(StringUtils.isNotBlank(enforceCertificate.getCertCode())){
	    	criteria.andEqualTo("certCode", enforceCertificate.getCertCode());
	    }
	    
	    example.setOrderByClause("id desc");

	    Page<Object> result = PageHelper.startPage(page, limit);
	    List<EnforceCertificate> list = enforceCertificateMapper.selectByExample(example);
	    return new TableResultResponse<EnforceCertificate>(result.getTotal(), list);
	}
	
	/**
	 * 根据编号获取终端
	 * @param id 编号
	 * @return
	 */
	public EnforceCertificate getById(Integer id) {
		
		EnforceCertificate enforceCertificate = enforceCertificateMapper.selectByPrimaryKey(id);
		
		if(enforceCertificate != null && enforceCertificate.getIsDeleted().equals("1")) {
			return null;
		}

		return enforceCertificate;
	}
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		enforceCertificateMapper.deleteByIds(ids, BaseContextHandler.getUserID(),BaseContextHandler.getName(),new Date());
	}
	/**
	 * 获取所有执法者用户
	 * @return
	 */
	public List<EnforceCertificate> getEnforceCertificateList(){
		  List<EnforceCertificate> list = enforceCertificateMapper.selectAllUserInfo();
		  return list;
	}
}