package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.AffairCache;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * AffairCacheMapper 第三方事件数据缓存.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018-12-04 00:41:37          chenshuai      1.0            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
public interface AffairCacheMapper extends CommonMapper<AffairCache> {

    /**
     * 获取最新受理时间的一条数据
     *
     * @return
     */
    AffairCache selectNewAcceptTime();
}
