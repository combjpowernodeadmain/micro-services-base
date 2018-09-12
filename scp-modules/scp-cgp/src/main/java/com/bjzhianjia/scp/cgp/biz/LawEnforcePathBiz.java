package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.LawEnforcePath;
import com.bjzhianjia.scp.cgp.mapper.LawEnforcePathMapper;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
 * LawEnforcePathBiz 执法轨迹记录.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月4日          admin      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author admin
 *
 */
@Service
public class LawEnforcePathBiz extends BusinessBiz<LawEnforcePathMapper,LawEnforcePath> {
    
    @Autowired
    private LawEnforcePathMapper lawEnforcePathMapper;
    /**
     * 查询指定用户，指定时间段的行为轨迹
     * 
     * @param userId
     *            用户id
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     *         [{"mapinfo":{"lng":"xxx","lat":"xxx"}, "time":"xxx"},
     *         {"mapinfo":{"lng":"xxx","lat":"xxx"},"time":"xxx"}]
     */
    public JSONArray getByUserIdAndDate(String userId, Date startTime, Date endTime) {
        JSONArray array = new JSONArray();
        List<LawEnforcePath> list = this.getByUserId(userId, startTime, endTime);

        JSONObject obj = null;
        for (LawEnforcePath lawEnforcePath : list) {
            obj = new JSONObject();
            obj.put("mapInfo", "{\"lng\":\""+lawEnforcePath.getLng()+"\",\"lat\":\""+lawEnforcePath.getLat()+"\"}");
            obj.put("time", lawEnforcePath.getCrtTime());
            array.add(obj);
        }
        return array;
    }

    /**
     * 查询指定用户，指定时间段的行为轨迹
     * 
     * @param userId
      *            用户id
     * @param startTime
      *            开始时间
     * @param endTime
      *            结束时间
     * @return
     *         List<LawEnforcePath>
     */
    public List<LawEnforcePath> getByUserId(String userId, Date startTime, Date endTime) {
        List<LawEnforcePath> list = new ArrayList<>();
        Example example = new Example(LawEnforcePath.class);
        if (StringUtils.isBlank(userId)) {
            return list;
        }
        if (startTime == null || endTime == null) {
            return list;
        }
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("crtUserId", userId);

        String _startTime = DateUtil.dateFromDateToStr(startTime, "yyyy-MM-dd HH:mm:ss");
        String _endTime = DateUtil.dateFromDateToStr(endTime, "yyyy-MM-dd HH:mm:ss");
        
        //结束日期往前推4天，最大查询4天的轨迹记录
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-4);
        Date maxStartTime = calendar.getTime();
        String maxEndTime = DateUtil.dateFromDateToStr(maxStartTime, "yyyy-MM-dd HH:mm:ss");
        
        StringBuilder sql = new StringBuilder();
        sql.append("(('").append(_startTime).append("'").append("<=crt_time  and '").append(maxEndTime).append("'<=crt_time)");
        sql.append(" OR crt_time<=").append("'").append(_endTime).append("')");
        
        criteria.andCondition(sql.toString());
        list = lawEnforcePathMapper.selectByExample(example);
        
        return list != null ? list:new ArrayList<>();
    }
    
    /**
     * 通过用户id查询定位
     * @param userId
     * @return
     */
    public JSONObject getMapInfoByUserId(String userId) {
        JSONObject obj = new JSONObject();
        LawEnforcePath lawEnforcePath = this.getMaxOne(userId);
        if(lawEnforcePath != null) {
            obj.put("mapInfo", "{\"lng\":\""+lawEnforcePath.getLng()+"\",\"lat\":\""+lawEnforcePath.getLat()+"\"}");
            obj.put("time", lawEnforcePath.getCrtTime());
        }
        return obj;
    }
    /**
     *      通过用户ID查询最新定位记录
     * 
     * @return
     */
    public LawEnforcePath getMaxOne(String userId) {
        if(StringUtils.isBlank(userId)) {
            return null;
        }
        Example example = new Example(LawEnforcePath.class);
        example.setOrderByClause("id desc");
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("crtUserId", userId);
        
        PageHelper.startPage(1, 1);
        List<LawEnforcePath> list = lawEnforcePathMapper.selectByExample(example);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    /**
     * 按用户ID查询记录，多个ID之间用逗号隔开
     * @param userIds
     * @return
     */
    public Map<String, JSONObject> getByUserIds(String userIds){
        userIds="'"+userIds.replaceAll(",", "','")+"'";
        List<LawEnforcePath> list = this.mapper.getByUserIds(userIds);
        
        if (list == null || list.isEmpty()) {
            return new HashMap<String,JSONObject>();
        }
        
        Map<String, JSONObject> result=new HashMap<>();
        for(LawEnforcePath lawEnforcePath:list) {
            JSONObject obj = new JSONObject();
            if(lawEnforcePath != null) {
                obj.put("mapInfo", "{\"lng\":\""+lawEnforcePath.getLng()+"\",\"lat\":\""+lawEnforcePath.getLat()+"\"}");
                obj.put("time", lawEnforcePath.getCrtTime());
            }
            
            result.put(lawEnforcePath.getCrtUserId(), obj);
        }
        return result;
    }
}