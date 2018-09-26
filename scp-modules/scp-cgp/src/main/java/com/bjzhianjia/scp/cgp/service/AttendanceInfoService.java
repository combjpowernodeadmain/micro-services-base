package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.cgp.biz.AttendanceInfoBiz;
import com.bjzhianjia.scp.cgp.biz.DeptUtilBiz;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.vo.AttendanceVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import tk.mybatis.mapper.util.StringUtil;

/**
 * 终端服务类
 * 
 * @author zzh
 *
 */
@Service
public class AttendanceInfoService {

    @Autowired
    private AttendanceInfoBiz attendanceInfoBiz;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private DeptUtilBiz deptUtilBiz;

    /**
     * 分页查询
     * 
     * @param map
     *            条件
     * @return
     */
    public TableResultResponse<AttendanceVo> getList(Map<String, Object> map) {

        if (map.get("depart") != null && !StringUtil.isEmpty(map.get("depart").toString())) {
            List<String> departs = deptUtilBiz.getDeptIds(map.get("depart").toString());
            map.put("depart", departs);
        }

        TableResultResponse<AttendanceVo> tableResult = attendanceInfoBiz.getList(map);

        List<AttendanceVo> list = tableResult.getData().getRows();

        for (AttendanceVo attendanceVo : list) {
            System.out.println("日期：" + attendanceVo.getDateDay() + "\t签到时间：" + attendanceVo.getSignInTime() + "\t签退时间："
                + attendanceVo.getSignOutTime());
        }

        if (list.size() == 0) {
            return tableResult;
        }

        setDepart(list);

        return tableResult;
    }

    private void setDepart(List<AttendanceVo> list) {

        Map<String, String> departs = new HashMap<>();
        List<String> uniqueDeparts = new ArrayList<>();
        for (AttendanceVo attendanceVo : list) {
            if (StringUtils.isNotBlank(attendanceVo.getDepart())) {
                uniqueDeparts.add(attendanceVo.getDepart());
            }
        }

        if (uniqueDeparts != null && !uniqueDeparts.isEmpty()) {
            departs = adminFeign.getLayerDepart(String.join(",", uniqueDeparts));
        }

        if (!departs.isEmpty()) {
            for (AttendanceVo attendanceVo : list) {
                String departName = departs.get(attendanceVo.getDepart());
                if (StringUtil.isEmpty(departName)) {
                    departName = JSON.parseObject(departName).getString("name");
                }
                attendanceVo.setDepart(departName);
            }
        }
    }
}
