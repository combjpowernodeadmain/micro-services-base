package com.bjzhianjia.scp.security.gate.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * @author scp
 * @create 2018/2/7.
 */
@Component
@Primary
public class SwaggerConfig implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("管理服务", "/api/admin/v2/api-docs", "2.0"));
        resources.add(swaggerResource("字典服务", "/api/dict/v2/api-docs", "2.0"));
        resources.add(swaggerResource("鉴权服务", "/api/auth/v2/api-docs", "2.0"));
        resources.add(swaggerResource("工作流服务", "/api/workflow/v2/api-docs", "2.0"));
        resources.add(swaggerResource("综合执法", "/api/cgp/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
