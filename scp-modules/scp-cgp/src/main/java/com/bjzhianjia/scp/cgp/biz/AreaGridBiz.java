package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 网格
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Service
@Slf4j
public class AreaGridBiz extends BusinessBiz<AreaGridMapper,AreaGrid> {
	@Autowired
	private MergeCore mergeCore;
	
	/**
	 * 按网络编号查询网格
	 * @author 尚
	 * @param gridCde
	 * @return
	 */
	public List<AreaGrid> getByConditions(Map<String, String> conditions) {
		Example example=new Example(AreaGrid.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");
		
		Set<String> keySet = conditions.keySet();
		for (String string : keySet) {
			criteria.andEqualTo(string,conditions.get(string));
		}
		
		List<AreaGrid> gridAreaList = mapper.selectByExample(example);
		
		return gridAreaList;
	}
	
	/**
	 * 分页按条件获取网格数据
	 * @author 尚
	 * @param page
	 * @param limit
	 * @param areaGrid
	 * @return
	 */
	public TableResultResponse<AreaGridVo> getList(int page,int limit,AreaGrid areaGrid){
		Example example=new Example(AreaGrid.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");
		
		if(StringUtils.isNotBlank(areaGrid.getGridName())) {
			criteria.andLike("gridName", "%"+areaGrid.getGridName()+"%");
		}
		if(StringUtils.isNotBlank(areaGrid.getGridLevel())) {
			criteria.andEqualTo("gridLevel", areaGrid.getGridLevel());
		}
		
		example.setOrderByClause("crt_time desc");
		
		Page<Object> result = PageHelper.startPage(page, limit);
		List<AreaGrid> list = this.mapper.selectByExample(example);
		
		try {
			mergeCore.mergeResult(AreaGrid.class, list);
		} catch(Exception ex) {
			log.error("merge data exception", ex);
		}
		
		List<AreaGridVo> voList=new ArrayList<>();
		for (AreaGrid tmp : list) {
			AreaGridVo vo = BeanUtil.copyBean_New(tmp, new AreaGridVo());
			voList.add(vo);
		}
		return new TableResultResponse<>(result.getTotal(), voList);
	}
	
	/**
	 * 按网格等级获取网格列表
	 * @author 尚
	 * @param areaGrid
	 * @return
	 */
	public List<AreaGrid> getByGridLevel(String gridLevel){
		Example example=new Example(AreaGrid.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");
		
		String[] split = gridLevel.split(",");
		List<String> gridLevelList = Arrays.asList(split);
		criteria.andIn("gridLevel", gridLevelList);
		
		example.setOrderByClause("id desc");
		
		List<AreaGrid> list = this.mapper.selectByExample(example);
		return list;
	}
	
	/**
	 * 按ID集合查询记录
	 * @author 尚
	 * @param ids
	 * @return
	 */
	public List<AreaGrid> getByIds(List<Integer> ids ){
		Example example=new Example(AreaGrid.class);
		Example.Criteria criteria=example.createCriteria();
		
		criteria.andEqualTo("isDeleted","0");
		criteria.andIn("id", ids);
		List<AreaGrid> result = this.mapper.selectByExample(example);
		
		
		return result;
	}
}