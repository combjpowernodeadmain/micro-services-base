package com.bjzhianjia.scp.security.gate.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bjzhianjia.scp.security.common.constant.RequestHeaderConstants;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.gate.feign.IUserFeign;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component(value = "checkLoginSourceFilter")
public class CheckLoginSourceFilter extends ZuulFilter {
    @Value("${zuul.prefix}")
    private String zuulPrefix;

    @Autowired
    private IUserFeign iUserFeign;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String username = request.getParameter("username");
        final String requestUri = request.getRequestURI().substring(zuulPrefix.length());

        if(requestUri.endsWith("oauth/token")){
            String authorization = request.getHeader("Authorization");
            if(StringUtils.isNotBlank(authorization)){
                ObjectRestResponse<Map<String, String>> response = iUserFeign.getUserInfoByUsername(username);
                Map<String, String> data = response.getData();
                String attr3 = data.get("attr3");

                String loginSource = StringUtils.removeStart(authorization, RequestHeaderConstants.AUTHORIZATION_BASIC);
                switch (loginSource){
                    case RequestHeaderConstants.AUTHORIZATION_CLIENT:
                        // APP端
                        if(!StringUtils.equals(attr3, RequestHeaderConstants.AUTHORIZATION_CLIENT)){
                            // 表明登录来自APP端，但该用户为非APP用户
                            setFailedRequest("登录名无效", HttpStatus.UNAUTHORIZED.value());
                        }
                        break;
                    case RequestHeaderConstants.AUTHORIZATION_VUE:
                        // WEB端
                        if(!StringUtils.equals(attr3, RequestHeaderConstants.AUTHORIZATION_VUE)){
                            // 表明登录来自WEB端，但该用户为非WEB用户
                            setFailedRequest("登录名无效", HttpStatus.UNAUTHORIZED.value());
                        }
                        break;
                }
            }
        }

        return null;
    }

    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private void setFailedRequest(String body, int code) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
        }
    }
}
