/*
 *
 * Copyright 2018 by zxbit.cn
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZXBIT ("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with ZXBIT.
 *
 */
package com.bjzhianjia.scp.security.wf.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

/**
 * IDepartFeign 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * Jul 29, 2018          ric_w      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @date Jul 29, 2018
 * @author ric_w
 *
 */
@FeignClient(value = "scp-admin",configuration = FeignApplyConfiguration.class)
public interface IDepartFeign {

}
