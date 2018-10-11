package com.bjzhianjia.scp.cgp.biz;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMemberMapper;
import com.bjzhianjia.scp.cgp.mapper.EnforceCertificateMapper;

/**
 * PersonalCenterBiz 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月30日          can      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author can
 *
 */
@Service
public class PersonalCenterBiz {

    @Autowired
    private EnforceCertificateMapper enforceCertificateMapper;

    @Autowired
    private AreaGridMemberMapper areaGridMemberMapper;

    @Autowired
    private VhclManagementBiz vhclManagementBiz;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

   

    public JSONObject countOfPeopleOnHome() {
        // 在线人数
        int countOnLine = countOnLine();

        // 执法人员数量
        int countOfEnforce = enforceCertificateMapper.countOfEnforce();

        // 网格管理人员数量
        int countOfAreaMember = areaGridMemberMapper.countOfAreaMember();

        // 车辆数量
        int countOfVhcl = vhclManagementBiz.countOfVhcl();

        JSONObject result = new JSONObject();
        result.put("countOnLine", countOnLine);
        result.put("countOfEnforce", countOfEnforce);
        result.put("countOfAreaMember", countOfAreaMember);
        result.put("countOfVhcl", countOfVhcl);

        return result;
    }

    private int countOnLine() {
        String auth = "AG:OAUTH:auth:*";
        Set<String> key = redisTemplate.keys(auth);
        return key == null ? 0 : key.size();
    }
}