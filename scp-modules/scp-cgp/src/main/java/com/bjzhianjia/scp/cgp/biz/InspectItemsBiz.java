package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.entity.InspectItems;
import com.bjzhianjia.scp.cgp.mapper.InspectItemsMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 巡查事项
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-16 16:08:44
 */
@Service
public class InspectItemsBiz extends BusinessBiz<InspectItemsMapper,InspectItems> {
	
	@Autowired
	private InspectItemsMapper inspectItemsMapper;
	
	/**
	 * 根据编号
	 * @param code 编号
	 * @return
	 */
	public InspectItems getByCode(String code) {
		
		Example example = new Example(InspectItems.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("code", code);
		
		List<InspectItems> list = inspectItemsMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			return null;
		}
		
		InspectItems inspectItems = list.get(0);
		
		if(inspectItems.getIsDeleted().equals("1")) {
			return null;
		}
		
		return inspectItems;
	}
	
	/**
	 * 根据名称
	 * @param name 名称
	 * @return
	 */
	public InspectItems getByName(String name) {
		Example example = new Example(InspectItems.class);
		
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("name", name);
		
		List<InspectItems> list = inspectItemsMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			return null;
		}
		
		InspectItems inspectItems = list.get(0);
		
		if(inspectItems.getIsDeleted().equals("1")) {
			return null;
		}
		
		return inspectItems;
	}

	/**
	 * 根据查询条件分页搜索
	 * @param page
	 * @param limit
	 * @param inspectItems
	 * @return
	 */
	public TableResultResponse<InspectItems> getList(int page, int limit, InspectItems inspectItems) {
		Example example = new Example(InspectItems.class);
	    Example.Criteria criteria = example.createCriteria();
	    
	    criteria.andEqualTo("isDeleted", "0");
	    if(StringUtils.isNotBlank(inspectItems.getCode())){
	    	criteria.andEqualTo("code", inspectItems.getCode());
	    }
		if (StringUtils.isNotBlank(inspectItems.getName())) {
			criteria.andLike("name", "%" + inspectItems.getName() + "%");
		}
	    if(StringUtils.isNotBlank(inspectItems.getBizType())){
	    	criteria.andEqualTo("bizType", inspectItems.getBizType());
	    }
        // 添加按事件类别查询巡查事项
        if (StringUtils.isNotBlank(inspectItems.getType())) {
            criteria.andEqualTo("type", inspectItems.getType());
        }
	    
	    example.setOrderByClause("id desc");

	    Page<Object> result = PageHelper.startPage(page, limit);
	    List<InspectItems> list = inspectItemsMapper.selectByExample(example);
	    return new TableResultResponse<InspectItems>(result.getTotal(), list);
	}
	
	/**
	 * 根据编号获取终端
	 * @param id 编号
	 * @return
	 */
	public InspectItems getById(Integer id) {
		
		InspectItems inspectItems = inspectItemsMapper.selectByPrimaryKey(id);
		
		if(inspectItems != null && inspectItems.getIsDeleted().equals("1")) {
			return null;
		}

		return inspectItems;
	}
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		inspectItemsMapper.deleteByIds(ids, BaseContextHandler.getUserID(),BaseContextHandler.getName(),new Date());
	}
	/**
	 *  根据事件类别id查询清单
	 * @param eventTypeId 事件类别id
	 * @return
	 * 		巡查事项清单
	 */
	public List<InspectItems> getByEventType(Integer eventTypeId) {
		
		Example example = new Example(InspectItems.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("type", eventTypeId);
		List<InspectItems> list = inspectItemsMapper.selectByExample(example);
		
		return list;
	}
	/**
	 * 根据编号ids获取
	 *
	 * @param ids 编号
	 * @return
	 */
	public List<InspectItems> getByInspectIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			return new ArrayList<>();
		}
		List<InspectItems> inspectItemsList = inspectItemsMapper.selectByIds(ids);
		return BeanUtil.isEmpty(inspectItemsList) ? new ArrayList<>() : inspectItemsList;
	}
}