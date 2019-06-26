package com.bjzhianjia.scp.cgp.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.PartyOrg;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 党组织表
 *
 * @author chenshuai
 * @version 2019-06-25
 * @email cs4380@163.com
 */
public interface PartyOrgMapper extends CommonMapper<PartyOrg> {
    /**
     * 获取全部数据，排除逻辑删除
     *
     * @return
     */
    List<PartyOrg> selectPartyOrgAll();

    /**
     * 获取最新排序编号
     *
     * @param parentOrgId id 父级id
     * @return
     */
    String selectMaxOrderById(@Param("parentOrgId") String parentOrgId);

    /**
     * 通过主键id获取详情
     *
     * @param partyOrgId 详情id
     * @return
     */
    JSONObject selectBaseInfoById(@Param("partyOrgId") String partyOrgId);
}
