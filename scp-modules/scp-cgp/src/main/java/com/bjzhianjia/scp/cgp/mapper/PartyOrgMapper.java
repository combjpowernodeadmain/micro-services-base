package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.PartyOrg;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

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
}
