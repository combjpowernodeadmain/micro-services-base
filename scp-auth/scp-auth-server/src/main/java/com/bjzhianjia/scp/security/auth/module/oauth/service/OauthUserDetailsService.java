package com.bjzhianjia.scp.security.auth.module.oauth.service;

import com.bjzhianjia.scp.security.auth.feign.IUserService;
import com.bjzhianjia.scp.security.auth.module.oauth.bean.OauthUser;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Component
public class OauthUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户名为空！");
        }
        ObjectRestResponse<Map<String, String>> response = userService.getUserInfoByUsername(username);
        Map<String, String> data = response.getData();
        if(BeanUtils.isEmpty(data)){
            throw new UsernameNotFoundException("用户账户错误！");
        }
        if (StringUtils.isEmpty(data.get("id"))) {
            throw new UsernameNotFoundException("用户名不合法！");
        }
        //1禁用用户 0正常
        String isDisabled =  data.get("isDisabled");
        if(StringUtils.isEmpty(isDisabled) && "1".equals(isDisabled)) {
            throw new UsernameNotFoundException("用户被禁用！");
        }
        
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new OauthUser(data.get("id"), data.get("username"), data.get("password"), data.get("name"), data.get("departId"), data.get("tenantId"),
                authorities);
    }
}
