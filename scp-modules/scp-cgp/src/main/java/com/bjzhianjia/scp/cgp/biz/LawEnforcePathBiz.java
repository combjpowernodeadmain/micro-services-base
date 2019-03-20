package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.PatrolTaskPath;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.LawEnforcePathVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.constances.CommonConstances;
import com.bjzhianjia.scp.cgp.entity.LawEnforcePath;
import com.bjzhianjia.scp.cgp.mapper.LawEnforcePathMapper;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.github.pagehelper.PageHelper;

import org.springframework.web.bind.annotation.RequestBody;
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
@Log4j
public class LawEnforcePathBiz extends BusinessBiz<LawEnforcePathMapper, LawEnforcePath> {

    @Autowired
    private LawEnforcePathMapper lawEnforcePathMapper;

    @Autowired
    private EnforceCertificateBiz enforceCertificateBiz;

    @Autowired
    private PatrolTaskPathBiz patrolTaskPathBiz;

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
    public JSONArray getByUserIdAndDate(String userId, String startTime, String endTime) {
        JSONArray array = new JSONArray();
        List<LawEnforcePath> list = this.getByUserId(userId, startTime, endTime);

        JSONObject obj = null;
        for (LawEnforcePath lawEnforcePath : list) {
            obj = new JSONObject();
            obj.put("mapInfo",
                "{\"lng\":\"" + lawEnforcePath.getLng() + "\",\"lat\":\"" + lawEnforcePath.getLat() + "\"}");
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
    public List<LawEnforcePath> getByUserId(String userId, String startTime, String endTime) {
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

        // 查询的结束日期向后推一天
        Date endTimeT =
            DateUtil
                .theDayOfTommorrow(DateUtil.dateFromStrToDate(endTime, DateUtil.DATE_FORMAT_DF));

        // 结束日期往前推4天，最大查询4天的轨迹记录
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTimeT);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 4);
        Date maxStartTime = calendar.getTime();
        String maxEndTime = DateUtil.dateFromDateToStr(maxStartTime, "yyyy-MM-dd HH:mm:ss");

        StringBuilder sql = new StringBuilder();
        sql.append("(('").append(startTime).append("'").append("<=crt_time  and '").append(maxEndTime)
            .append("'<=crt_time)");
        sql.append(" and crt_time<=").append("'")
            .append(DateUtil.dateFromDateToStr(endTimeT, DateUtil.DATE_FORMAT_DF)).append("')");

        criteria.andCondition(sql.toString());
        list = lawEnforcePathMapper.selectByExample(example);

        return list != null ? list : new ArrayList<>();
    }

    /**
     * 通过用户id查询定位
     * 
     * @param userId
     * @return
     */
    public JSONObject getMapInfoByUserId(String userId) {
        JSONObject obj = new JSONObject();
        LawEnforcePath lawEnforcePath = this.getMaxOne(userId);
        if (lawEnforcePath != null) {
            obj.put("mapInfo",
                "{\"lng\":\"" + lawEnforcePath.getLng() + "\",\"lat\":\"" + lawEnforcePath.getLat() + "\"}");
            obj.put("time", lawEnforcePath.getCrtTime());
        }
        return obj;
    }

    /**
     * 通过用户ID查询最新定位记录
     * 
     * @return
     */
    public LawEnforcePath getMaxOne(String userId) {
        if (StringUtils.isBlank(userId)) {
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
     * 
     * @param userIds
     * @return
     */
    public Map<String, JSONObject> getByUserIds(String userIds) {
        userIds = "'" + userIds.replaceAll(",", "','") + "'";
        List<LawEnforcePath> list = this.mapper.getByUserIds(userIds);

        if (list == null || list.isEmpty()) {
            return new HashMap<String, JSONObject>();
        }

        Map<String, JSONObject> result = new HashMap<>();
        for (LawEnforcePath lawEnforcePath : list) {
            JSONObject obj = new JSONObject();
            if (lawEnforcePath != null) {
                obj.put("mapInfo",
                    "{\"lng\":\"" + lawEnforcePath.getLng() + "\",\"lat\":\"" + lawEnforcePath.getLat() + "\"}");
                obj.put("time",
                    DateUtil.dateFromDateToStr(lawEnforcePath.getCrtTime(), CommonConstances.DATE_FORMAT_FULL));
            }

            result.put(lawEnforcePath.getCrtUserId(), obj);
        }
        return result;
    }

    /**
     * 添加执法人员轨迹
     * @param lawEnforcePathVo
     * @return
     */
    public ObjectRestResponse<Void> addLawEnforcePath(@ApiParam("执法轨迹") @RequestBody LawEnforcePathVo lawEnforcePathVo) {
        ObjectRestResponse<Void> restResult=new ObjectRestResponse<>();

        JSONObject json = null;
        try {
            json = JSONObject.parseObject(lawEnforcePathVo.getMapInfo());
            if (json == null) {
                restResult.setStatus(400);
                return restResult;
            }

            EnforceCertificate enforceCertificate =
                enforceCertificateBiz.getEnforceCertificateByUserId(BaseContextHandler.getUserID());

            // 当前人员是执法人员
            //mapInfo 数据封装
            log.debug("添加执法人员轨迹");
            LawEnforcePath lawEnforcePath = new LawEnforcePath();
            lawEnforcePath.setLat(json.getDouble("lat"));
            lawEnforcePath.setLng(json.getDouble("lng"));
            lawEnforcePath.setTerminalId(lawEnforcePathVo.getTerminalId());
            lawEnforcePath.setDeptId(BaseContextHandler.getDepartID());
            lawEnforcePath.setTanentId(BaseContextHandler.getTenantID());
            this.insertSelective(lawEnforcePath);

        } catch (Exception e) {
            restResult.setMessage("添加失败");
            restResult.setStatus(400);
            return restResult;
        }
        return restResult;
    }
}