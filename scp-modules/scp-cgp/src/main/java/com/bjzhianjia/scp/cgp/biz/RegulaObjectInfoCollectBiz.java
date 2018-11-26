package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectInfoCollect;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectInfoCollectMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 监管对象信息采集过程记录
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-11-22 17:30:13
 */
@Service
public class RegulaObjectInfoCollectBiz extends BusinessBiz<RegulaObjectInfoCollectMapper,RegulaObjectInfoCollect> {

    /**
     * 查询审批的信息采集审批列表
     * @param queryJObj
     */
    public TableResultResponse<JSONObject> getListToApprove(JSONObject queryJObj,int page,int limit) {
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<JSONObject> list = this.mapper.list(queryJObj);

        if(BeanUtil.isNotEmpty(list)){
            return new TableResultResponse<>(pageInfo.getTotal(),list);
        }

        return new TableResultResponse<>(0,new ArrayList<>());
    }
}