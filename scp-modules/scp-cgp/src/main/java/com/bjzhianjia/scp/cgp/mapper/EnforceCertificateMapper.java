package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 执法证管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
// @Tenant
public interface EnforceCertificateMapper extends CommonMapper<EnforceCertificate> {

    /**
     * 批量删除
     * 
     * @param ids
     *            id列表
     */
    public void deleteByIds(@Param("ids") Integer[] ids, @Param("updUserId") String updUserId,
        @Param("updUserName") String updUserName, @Param("updTime") Date updTime);

    /**
     * 查询全部用户信息
     * 
     * @return
     *         usr_id,holder_name,depart_id
     */
    public List<EnforceCertificate> selectAllUserInfo();

    /**
     * 查询未删除的执法记录数
     * 
     * @return
     */
    public int countOfEnforce();

    List<String> distinctUsrId();
}
