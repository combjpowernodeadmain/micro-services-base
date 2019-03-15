package com.bjzhianjia.scp.security.gate.filter;

import java.util.Calendar;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 *
 * AuthorizationFilter 授权认证拦截器.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年10月16日          admin      1.0            ADD
 * </pre>
 *
 *
 * @version 1.0
 * @author admin
 *
 */
@Component
public class AuthorizationFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return "pre";
    }
    /**
     * 授权时间比较
     *
     * @return true：允许访问
     *         false:不允许访问
     */
    private static boolean before() {
        try {
            // 系统时间
            Calendar starTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
			// 2019-08-01 00:00:00
            endTime.setTimeInMillis(1564588800000L);
            // 比较时间  
            return starTime.before(endTime);
        } catch (Exception exception) {
            return false;
        }
    }
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 不对其进行路由
        if (!before()) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(4003);
            ctx.setResponseBody("Authorized expired, please contact the administrator!");
            return null;
        }
        return null;
    }
}
