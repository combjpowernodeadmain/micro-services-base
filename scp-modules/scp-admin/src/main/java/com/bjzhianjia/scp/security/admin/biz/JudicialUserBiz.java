
package com.bjzhianjia.scp.security.admin.biz;

import com.bjzhianjia.scp.security.admin.entity.Depart;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.mapper.JudicialUserMapper;
import com.bjzhianjia.scp.security.common.biz.BaseBiz;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * JudicialUserController 司法用户.
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/9/29          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class JudicialUserBiz extends BaseBiz<JudicialUserMapper, User> {

    @Autowired
    private JudicialUserMapper judicialUserMapper;



    /**
     * 获取分案人总数
     *
     * @param departId     部门id
     * @param groupId      角色id
     * @param professional 专业id
     * @return
     */
    public Integer getUserByDepartAndGroup(String departId, String groupId, String professional) {
        if (StringUtils.isBlank(departId) || StringUtils.isBlank(groupId) || StringUtils.isBlank(professional)) {
            return 0;
        }
        return judicialUserMapper.selectUserByDepartAndGroup(departId, groupId, professional);
    }

}
