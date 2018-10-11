package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.PublicManagement;
import com.bjzhianjia.scp.cgp.mapper.PublicManagementMapper;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-29 15:59:04
 */
@Service
public class PublicManagementBiz extends BusinessBiz<PublicManagementMapper, PublicManagement> {

    @Autowired
    private PropertiesProxy propertiesProxy;

    /**
     * 获取全部记录列表
     * 
     * @return
     */
    public TableResultResponse<JSONObject> getAll() {
        Example example = new Example(PublicManagement.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        example.setOrderByClause("order_num");

        List<PublicManagement> all = this.selectByExample(example);
        List<JSONObject> resultJObjList = new ArrayList<>();
        for (PublicManagement publicManagement : all) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject =
                    propertiesProxy.swapProperties(publicManagement, "id", "netName", "netUrl", "iconUrl", "orderNum");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            resultJObjList.add(jsonObject);
        }

        return new TableResultResponse<>(all.size(), resultJObjList);
    }

    /**
     * 批量删除对象
     * 
     * @param ids
     * @return
     */
    public ObjectRestResponse<Void> remove(Integer[] ids) {
        this.mapper.remove(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), new Date());
        return new ObjectRestResponse<Void>().data(null);
    }
}