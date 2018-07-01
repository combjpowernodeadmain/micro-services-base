package com.bjzhianjia.scp.merge.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bjzhianjia.scp.merge.facade.DefaultMergeResultParser;
import com.bjzhianjia.scp.merge.facade.IMergeResultParser;

/**
 * @author scp
 * @create 2018/2/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface MergeResult {
    Class<? extends IMergeResultParser> resultParser() default DefaultMergeResultParser.class;
}
