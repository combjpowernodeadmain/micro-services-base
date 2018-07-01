package com.bjzhianjia.scp.merge.demo.merge;

import org.springframework.stereotype.Component;

import com.bjzhianjia.scp.merge.facade.IMergeResultParser;

import java.util.List;

/**
 * @author scp
 * @create 2018/2/3.
 */
@Component
public class TestMergeResultParser implements IMergeResultParser {
    @Override
    public List parser(Object o) {
        return (List)o;
    }
}
