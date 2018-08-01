package com.bjzhianjia.scp.cgp.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.AnnouncementInfo;
import com.bjzhianjia.scp.cgp.mapper.AnnouncementInfoMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import tk.mybatis.mapper.entity.Example;

/**
 * 公告信息
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-30 22:12:53
 */
@Service
public class AnnouncementInfoBiz extends BusinessBiz<AnnouncementInfoMapper,AnnouncementInfo> {
	
	@Autowired
	private AnnouncementInfoMapper announcementInfoMapper;
	
	/**
	 * 根据查询条件搜索
	 * @param eventType
	 * @return
	 */
	public TableResultResponse<AnnouncementInfo> getList(int page, int limit, Map<String,String> announcementInfo) {
		Example example = new Example(AnnouncementInfo.class);
	    Example.Criteria criteria = example.createCriteria();
	    
	    criteria.andEqualTo("isDeleted", "0");
	    if(!StringUtils.isNotBlank(announcementInfo.get("title"))){
	    	criteria.andLike("title", announcementInfo.get("title"));
	    }
	    if(!StringUtils.isNotBlank(announcementInfo.get("status"))){
	    	criteria.andEqualTo("status", announcementInfo.get("status"));
	    }
	    if(!StringUtils.isNotBlank(announcementInfo.get("isStick"))){
	    	criteria.andEqualTo("is_stick", announcementInfo.get("isStick"));
	    }
	    if(!StringUtils.isNotBlank(announcementInfo.get("startDate"))) {
	    	criteria.andGreaterThanOrEqualTo("crt_time", announcementInfo.get("startDate"));
	    }
	    if(!StringUtils.isNotBlank(announcementInfo.get("endDate"))) {
	    	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String endDate = announcementInfo.get("endDate");
			try {
				Date newDate = sdf.parse(announcementInfo.get("endDate"));
				Calendar calendar = Calendar.getInstance();
				
				calendar.setTime(newDate);
				calendar.add(Calendar.DATE, 1);
				endDate = sdf.format(calendar.getTime());
				criteria.andLessThan("crt_time", endDate);
			} catch (ParseException e) {
				
			}
	    }
	    example.setOrderByClause("id desc");

	    Page<Object> result = PageHelper.startPage(page, limit);
	    List<AnnouncementInfo> list = announcementInfoMapper.selectByExample(example);
	    return new TableResultResponse<AnnouncementInfo>(result.getTotal(), list);
	}
	
	/**
	 * 根据编号获取终端
	 * @param id 编号
	 * @return
	 */
	public AnnouncementInfo getById(Integer id) {
		
		AnnouncementInfo announcementInfo = announcementInfoMapper.selectByPrimaryKey(id);
		
		if(announcementInfo != null && announcementInfo.getIsDeleted().equals("1")) {
			return null;
		}

		return announcementInfo;
	}
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(Integer[] ids) {
		announcementInfoMapper.deleteByIds(ids, BaseContextHandler.getUserID(),BaseContextHandler.getName(),new Date());
	}
}