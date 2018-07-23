package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

/**
 * 权利事项数据访问接口
 * @author zzh
 *
 */
@Tenant
public interface RightsIssuesMapper extends CommonMapper<RightsIssues>, DeleteByIdsMapper<RightsIssues> {

}
