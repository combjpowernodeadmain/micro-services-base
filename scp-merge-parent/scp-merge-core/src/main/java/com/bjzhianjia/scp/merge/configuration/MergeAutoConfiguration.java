package com.bjzhianjia.scp.merge.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bjzhianjia.scp.merge.core.BeanFactoryUtils;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.merge.facade.DefaultMergeResultParser;

/**
 * @author scp
 * @create 2018/2/3.
 */
@Configuration
@ComponentScan("com.bjzhianjia.scp.merge.aspect")
@ConditionalOnProperty(name = "merge.enabled", matchIfMissing = false)
public class MergeAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MergeProperties mergeProperties() {
        return new MergeProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanFactoryUtils beanFactoryUtils() {
        return new BeanFactoryUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public MergeCore mergeCore() {
        return new MergeCore(mergeProperties());
    }

    @Bean
    public DefaultMergeResultParser defaultMergeResultParser() {
        return new DefaultMergeResultParser();
    }
}
