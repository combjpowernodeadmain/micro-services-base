package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Autowired
    private RegulaObjectBiz regulaObjectBiz;
    
    @Autowired
    private DictFeign dictFeign;
    
    @Autowired
    private Environment environment;

    /**
     * 查询信息采集列表
     * @param queryJObj
     */
    public TableResultResponse<JSONObject> getList(JSONObject queryJObj, int page, int limit) {
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<JSONObject> list = this.mapper.list(queryJObj);

        if (BeanUtil.isNotEmpty(list)) {
            // 收集审批状态字典，聚合审批状态名称
            List<String> infoApproveStatus =
                    list.stream().map(o -> o.getString("infoApproveStatus")).distinct()
                            .collect(Collectors.toList());
            Map<String, String> dictValueMap =
                    dictFeign.getByCodeIn(String.join(",", infoApproveStatus));
            if (BeanUtil.isEmpty(dictValueMap)) {
                dictValueMap = new HashMap<>();
            }
            for (JSONObject tmpJObj : list) {
                tmpJObj.put("infoApproveStatusName",
                        dictValueMap.get(tmpJObj.getString("infoApproveStatus")) == null ? ""
                                : dictValueMap.get(tmpJObj.getString("infoApproveStatus")));
            }

            return new TableResultResponse<>(pageInfo.getTotal(), list);
        }

        return new TableResultResponse<>(0,new ArrayList<>());
    }

    /**
     * 进行信息采集的审批
     * @param regulaObjectInfoCollect
     * @return
     */
    public ObjectRestResponse<RegulaObjectInfoCollect> approve(RegulaObjectInfoCollect regulaObjectInfoCollect) {
        /*
         * 1 修改信息采集表状态
         * 2 如果信息采集审批意见为通过，则修改监管对象表状态is_disable为‘0’
         */
        ObjectRestResponse<RegulaObjectInfoCollect> objectResult = new ObjectRestResponse<>();
        regulaObjectInfoCollect.setInfoApprover(BaseContextHandler.getUserID());//审批人ID
        regulaObjectInfoCollect.setInfoApproverName(BaseContextHandler.getUsername());//审批人姓名
        regulaObjectInfoCollect.setInfoApproveTime(new Date());//审批时间

        if (environment.getProperty("regulaObjectInfoCollectAgree")
            .equals(regulaObjectInfoCollect.getInfoApproveStatus())) {
            // 审批通过,修改监管对象表状态is_disable为‘0’
            RegulaObject regulaObject = new RegulaObject();
            if (BeanUtil.isEmpty(regulaObjectInfoCollect.getObjId())) {
                // 前端未传入监管对象ID过来
                RegulaObjectInfoCollect infoCollectInDB =
                    this.selectById(regulaObjectInfoCollect.getId());
                regulaObject.setId(infoCollectInDB.getObjId());
            } else {
                regulaObject.setId(regulaObjectInfoCollect.getObjId());
            }
            regulaObject.setIsDisabled("0");
            regulaObjectBiz.updateSelectiveById(regulaObject);
        }else{
            // 如果审批不通过，则须指定不通过的原因
            if(StringUtils.isEmpty(regulaObjectInfoCollect.getInfoApproveOpinion())){
                objectResult.setStatus(400);
                objectResult.setMessage("请指定审批意见");
                return objectResult;
            }
        }

        this.updateSelectiveById(regulaObjectInfoCollect);
        objectResult.setStatus(200);
        objectResult.setMessage("信息审批成功");
        return objectResult;
    }
}