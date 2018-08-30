package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.PatrolTaskMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 * 巡查任务记录表
 *
 * @author bo
 */
@Service
public class PatrolTaskBiz extends BusinessBiz<PatrolTaskMapper, PatrolTask> {

	@Autowired
	private PatrolTaskMapper patrolTaskMapper;

	@Autowired
	private DictFeign dictFeign;
	
	
	/**
	 * 根据查询条件搜索
	 * 
	 * @param patrolTask 巡查任务记录
	 * @param speName    专项任务名称
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @param page       页码
	 * @param limit      页容量
	 * @return
	 */
	public TableResultResponse<Map<String, Object>> selectPatrolTaskList(PatrolTask patrolTask, String speName,
			Date startTime, Date endTime, int page, int limit) {
		Page<Object> result = PageHelper.startPage(page, limit);
		List<Map<String, Object>> list = patrolTaskMapper.selectPatrolTaskList(patrolTask, speName, startTime, endTime);
		Set<String> codes = this.getCodes(list);
		
		if(codes == null) {
			return new TableResultResponse<Map<String, Object>>(0, null);
		}
		
		//获取数据字典：立案单状态值、来源类型
		Map<String, String> dictData = dictFeign.getByCodeIn(String.join(",", codes));
		Object sourceType = null;
		Object status = null;
		if(list != null && list.size() > 0) {
			for(Map<String,Object> map: list) {
				sourceType =  dictData.get(map.get("sourceType"));
				status =  dictData.get(map.get("status"));
				map.put("sourceType", sourceType);
				map.put("status", status);
			}
		}
		return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
	}
	
	
	/**
	 *  获取数字字典codes
	 * @param list
	 * @return
	 */
	private Set<String> getCodes(List<Map<String, Object>> list){
		Set<String> set = new HashSet<>();
		if(list != null && list.size() > 0 ) {
			for(Map<String , Object> map : list) {
				set.add(map.get("sourceType").toString()); //来源类型
				set.add(map.get("status").toString()); //立案单状态
			}
			return set;
		}else {
			return null;
		}
	}

}