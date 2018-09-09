package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsProcessBind;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.mapper.WritsTemplatesMapper;
import com.bjzhianjia.scp.cgp.vo.WritsProcessBindVo;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 文书模板
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Service
public class WritsTemplatesBiz extends BusinessBiz<WritsTemplatesMapper, WritsTemplates> {
	@Autowired
	private PropertiesConfig propertiesConfig;
	@Autowired
	private WritsProcessBindBiz writsProcessBindBiz;

	/**
	 * 添加单个对象
	 * 
	 * @author 尚
	 * @param writsTemplates
	 * @return
	 */
	public Result<Void> created(WritsTemplates writsTemplates) {
		Result<Void> result = check(writsTemplates);
		if (!result.getIsSuccess()) {
			return result;
		}

		this.insertSelective(writsTemplates);

		result.setIsSuccess(true);
		return result;
	}

	private Result<Void> check(WritsTemplates writsTemplates) {
		Result<Void> result = new Result<>();

		Example example = new Example(WritsTemplates.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDeleted", "0");
		// 验证名称是否重复
		if (StringUtils.isNotBlank(writsTemplates.getName())) {
			criteria.andEqualTo("name", writsTemplates.getName());
			List<WritsTemplates> instanceInDBList = this.selectByExample(example);
			if (instanceInDBList != null && !instanceInDBList.isEmpty()) {
				result.setIsSuccess(false);
				result.setMessage("模板名称已存在");
				return result;
			}
		}

		result.setIsSuccess(true);
		return result;
	}

	/**
	 * 按ID集合获取对象记录
	 * 
	 * @author 尚
	 * @param ids
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<WritsTemplates> getListByNode(String node, int page, int limit) {
		TableResultResponse<WritsTemplates> restResult = new TableResultResponse<>();

		if (StringUtils.isBlank(node)) {
			restResult.setStatus(400);
			restResult.setMessage("请指定待查询节点");
			return restResult;
		}

		/*
		 * 获取tcode值====================================开始===========================
		 */
		String codes = "";

		// 查询文书模板节点绑定表中数据
		WritsProcessBind writsProcessBind=new WritsProcessBind();
		writsProcessBind.setProcessNodeId(node);
		TableResultResponse<WritsProcessBindVo> bindRestResult = writsProcessBindBiz.getListOfCurrentNode(writsProcessBind, 1, 2147483647);
		List<WritsProcessBindVo> bindList = bindRestResult.getData().getRows();
		if (bindList != null && !bindList.isEmpty()) {
			List<String> idList = bindList.stream().map(o -> String.valueOf(o.getWritsId())).distinct()
					.collect(Collectors.toList());
			codes = String.join(",", idList);
		}

		if (StringUtils.isBlank(codes)) {
			// 说明在文书模板节点绑定表中没有进行配置，从常量类中获取
			codes = getWritsTemplateIds(node);
		}
		/*
		 * 获取tcode值====================================结束===========================
		 */

		Example example=new Example(WritsTemplates.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDeleted", "0");
		List<String> codeList=Arrays.asList(codes.split(","));
		criteria.andIn("tcode", codeList);
		Page<Object> pageInfo = PageHelper.startPage(page, limit);
		List<WritsTemplates> rows = this.mapper.selectByExample(example);
		
		restResult.getData().setRows(rows);
		restResult.getData().setTotal(pageInfo.getTotal());
		return restResult;
	}

	private String getWritsTemplateIds(String node) {
		String codes = "";

		if (node.equals(propertiesConfig.getSpotCheck())) {
			codes = propertiesConfig.getSpotCheckCodes();
		} else if (node.equals(propertiesConfig.getSpotPunishment())) {
			codes = propertiesConfig.getSpotPunishmentCodes();
		} else if (node.equals(propertiesConfig.getRectification())) {
			codes = propertiesConfig.getRectificationCodes();
		} else if (node.equals(propertiesConfig.getInform())) {
			codes = propertiesConfig.getInformCode();
		}

		return codes;
	}
	
	/**
	 * =按tcode查询文书模板
	 * @param tcode code集合，多个code之间用逗号隔开
	 * @return
	 */
	public List<WritsTemplates> getByTcodes(String tcode) {
	    if(StringUtils.isBlank(tcode)) {
	        return null;
	    }
	    
	    Example example=new Example(WritsTemplates.class);
	    Criteria criteria = example.createCriteria();
	    criteria.andEqualTo("isDeleted", "0");
	    criteria.andIn("tcode", Arrays.asList(tcode.split(",")));
	    
	    List<WritsTemplates> list = this.selectByExample(example);
	    if(list!=null&&!list.isEmpty()) {
	        return list;
	    }
	    return new ArrayList<WritsTemplates>();
	}
}