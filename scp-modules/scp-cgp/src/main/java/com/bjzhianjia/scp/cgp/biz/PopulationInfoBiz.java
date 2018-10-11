package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PopulationInfo;
import com.bjzhianjia.scp.cgp.mapper.PopulationInfoMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
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
 * @version 2018-09-28 15:50:11
 */
@Service
public class PopulationInfoBiz extends BusinessBiz<PopulationInfoMapper, PopulationInfo> {

    /**
     * 批量插入记录
     * 
     * @param populationInfoList
     * @return
     */
    public ObjectRestResponse<PopulationInfo> insertPopulationInfoList(List<PopulationInfo> populationInfoList) {
        ObjectRestResponse<PopulationInfo> restResult = new ObjectRestResponse<>();

        if (BeanUtil.isEmpty(populationInfoList)) {
            restResult.setMessage("请输入要添加的记录");
            restResult.setStatus(400);
            return restResult;
        }

        /*
         * 判断待添加的记录中有没有已经添加过的人口类型<br/>
         * 如果有，则将请求集合中对应人口类型的数量赋值给已添加过的人口类型，进行更新操作<br/>
         * 如果没有，则直接进行集合添加操作
         */
        List<String> pTypeList =
            populationInfoList.stream().map(o -> o.getPType()).distinct().collect(Collectors.toList());
        List<PopulationInfo> listByPType = getByPType(pTypeList);
        if (BeanUtil.isNotEmpty(listByPType)) {
            for (int i = 0; i < populationInfoList.size(); i++) {
                PopulationInfo i_p = populationInfoList.get(i);
                for (int j = 0; j < listByPType.size(); j++) {
                    PopulationInfo j_p = listByPType.get(j);
                    if (j_p.getPType().equals(i_p.getPType())) {
                        populationInfoList.remove(i_p);
                        j_p.setQuantity(i_p.getQuantity());
                        i--;
                    }
                }
            }

            // 设置更新信息
            for (PopulationInfo populationInfo : listByPType) {
                populationInfo.setUpdTime(new Date());
                populationInfo.setUpdUserId(BaseContextHandler.getUserID());
                populationInfo.setUpdUserName(BaseContextHandler.getUsername());
            }

            // 将已经添加过的人口类型进行更新操作
            this.mapper.updatePopulationInfoList(listByPType);
        }

        if (BeanUtil.isNotEmpty(populationInfoList)) {
            for (PopulationInfo populationInfo : populationInfoList) {
                populationInfo.setCrtTime(new Date());
                populationInfo.setCrtUserId(BaseContextHandler.getUserID());
                populationInfo.setCrtUserName(BaseContextHandler.getUsername());
                populationInfo.setDepartId(BaseContextHandler.getDepartID());
                populationInfo.setTenantId(BaseContextHandler.getTenantID());
            }
            this.mapper.insertPopulationInfoList(populationInfoList);
        }

        restResult.setMessage("成功");
        restResult.setStatus(200);
        return restResult;
    }

    private List<PopulationInfo> getByPType(List<String> pTypeList) {
        Example example = new Example(PopulationInfo.class);

        Criteria criteria = example.createCriteria();
        criteria.andIn("pType", pTypeList);

        List<PopulationInfo> list = this.selectByExample(example);
        if (BeanUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * 获取全部记录，返回数据中只包含部分字段
     * 
     * @return
     */
    public TableResultResponse<PopulationInfo> getAllWithParticalFields() {
        List<PopulationInfo> result = new ArrayList<>();

        List<PopulationInfo> all = this.selectListAll();
        for (PopulationInfo populationInfo : all) {
            PopulationInfo tmPopulationInfo = new PopulationInfo();
            tmPopulationInfo.setId(populationInfo.getId());
            tmPopulationInfo.setPType(populationInfo.getPType());
            tmPopulationInfo.setQuantity(populationInfo.getQuantity());
            result.add(tmPopulationInfo);
        }

        return new TableResultResponse<>(all.size(), result);
    }
}