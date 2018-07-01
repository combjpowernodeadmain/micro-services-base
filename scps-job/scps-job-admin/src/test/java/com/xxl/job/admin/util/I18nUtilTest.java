package com.xxl.job.admin.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bjzhianjia.scp.job.admin.core.util.I18nUtil;

/**
 * email util test
 *
 * @author xuxueli 2017-12-22 17:16:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationcontext-*.xml")
public class I18nUtilTest {

    @Test
    public void test(){
        System.out.println(I18nUtil.getString("admin_name"));
        System.out.println(I18nUtil.getMultString("admin_name", "admin_name_full"));
        System.out.println(I18nUtil.getMultString());
    }

}
