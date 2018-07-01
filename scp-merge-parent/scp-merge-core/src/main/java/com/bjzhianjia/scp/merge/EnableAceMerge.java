package com.bjzhianjia.scp.merge;

import org.springframework.context.annotation.Import;

import com.bjzhianjia.scp.merge.configuration.MergeAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author scp
 * @create 2018/2/3.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MergeAutoConfiguration.class})
public @interface EnableAceMerge {
}
