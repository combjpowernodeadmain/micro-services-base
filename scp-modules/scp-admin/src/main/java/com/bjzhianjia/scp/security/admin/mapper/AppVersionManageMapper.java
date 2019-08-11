package com.bjzhianjia.scp.security.admin.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.admin.entity.AppVersionManage;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * app版本管理表
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2019-03-24 11:35:35
 */
public interface AppVersionManageMapper extends CommonMapper<AppVersionManage> {

     List<JSONObject> getDownloadUrl();

}
