package com.bjzhianjia.scp.security.admin.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bjzhianjia.scp.security.admin.entity.AppVersionManage;
import com.bjzhianjia.scp.security.admin.feign.DictFeign;
import com.bjzhianjia.scp.security.admin.mapper.AppVersionManageMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * app版本管理表
 *
 * @author bo
 * @version 2019-03-24 11:35:35
 * @email 576866311@qq.com
 */
@Service
public class AppVersionManageBiz extends BusinessBiz<AppVersionManageMapper, AppVersionManage> {

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private  AppVersionManageMapper appVersionManageMapper;
    /**
     * 添加记录
     *
     * @param appVersionManage
     * @return
     */
    public ObjectRestResponse<Void> addAppVersion(AppVersionManage appVersionManage) {
        /*
         * 版本号唯一
         * 版本号只能增长
         */
        ObjectRestResponse<Void> restResponse = new ObjectRestResponse<>();

        check(restResponse, appVersionManage, false);
        if (restResponse.getStatus() == 400) {
            return restResponse;
        }

        this.insertSelective(appVersionManage);

        restResponse.setStatus(200);
        restResponse.setMessage("添加APP版本成功");
        return restResponse;
    }

    private void check(ObjectRestResponse<Void> restResponse, AppVersionManage appVersionManage, boolean isUpdate) {
        ObjectRestResponse<AppVersionManage> restLastestVsrsion = getLastestVsrsion(new AppVersionManage());
        AppVersionManage appVersionManageInDB = restLastestVsrsion.getData();

        if (appVersionManageInDB != null) {
            if (isUpdate) {
                // 只能修改最新版本的记录 appVersionManageInDB
                if (!appVersionManageInDB.getAppVersion().equals(appVersionManage.getAppVersion())) {
                    restResponse.setStatus(400);
                    restResponse.setMessage("只能修改最新版本记录");
                }
            } else {
                // 如果待添加版本号小于或等于最新版本号，则提示
                if (appVersionManage.getAppVersion() <= appVersionManageInDB.getAppVersion()) {
                    restResponse.setStatus(400);
                    restResponse.setMessage("当前版本号小于最新版本号");
                }
            }

        }
    }

    /**
     * 添加记录
     *
     * @param appVersionManage
     * @return
     */
    public ObjectRestResponse<Void> updateAppVersion(AppVersionManage appVersionManage) {
        /*
         * 版本号唯一
         * 版本号只能增长
         */
        ObjectRestResponse<Void> restResponse = new ObjectRestResponse<>();

        check(restResponse, appVersionManage, true);
        if (restResponse.getStatus() == 400) {
            return restResponse;
        }

        this.updateSelectiveById(appVersionManage);

        restResponse.setStatus(200);
        restResponse.setMessage("更新APP版本成功");
        return restResponse;
    }

    /**
     * 获取最新版本
     *
     * @return
     */
    public ObjectRestResponse<AppVersionManage> getLastestVsrsion(AppVersionManage qAppversion) {
        Example example = new Example(AppVersionManage.class)
            .selectProperties("id", "appVersion", "updateType", "updateContent", "downloadUrl", "osType", "osVersion",
                "resolution", "phoneRam", "createTime", "packageMd5", "isPartUpdate");

        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(qAppversion.getOsType())){
            criteria.andEqualTo("osType", qAppversion.getOsType());
        }
        if(StringUtils.isNotBlank(qAppversion.getOsVersion())){
            criteria.andEqualTo("osVersion", qAppversion.getOsVersion());
        }

        example.setOrderByClause("id desc");

        PageHelper.startPage(1, 1);
        List<AppVersionManage> appVersionManages = this.selectByExample(example);

        AppVersionManage appVersionManage = null;
        if (BeanUtils.isNotEmpty(appVersionManages)) {
            // 查询长度为1，如果如果集不空，则长度为1
            appVersionManage = appVersionManages.get(0);
        }

        return new ObjectRestResponse<AppVersionManage>().data(appVersionManage);
    }

    /**
     * 获取最新版本
     *
     * @return
     */
    public List<JSONObject>  getDownloadUrl( ) {

        List<JSONObject> appVersionManages = appVersionManageMapper.getDownloadUrl();

        return  appVersionManages;
    }
    /**
     * 分页查询记录
     * @param appVersionManage
     * @param page
     * @param limit
     * @param startCreateTime
     * @param endCreateTime
     * @return
     */
    public TableResultResponse<JSONObject> getAppVersionList(AppVersionManage appVersionManage, int page,
        int limit, Date startCreateTime, Date endCreateTime) {
        Example example = new Example(AppVersionManage.class);

        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(appVersionManage.getUpdateType())) {
            criteria.andEqualTo("updateType", appVersionManage.getUpdateType());
        }
        if (BeanUtils.isNotEmpty(appVersionManage.getAppVersion())) {
            criteria.andLike("appVersion", "%" + appVersionManage.getAppVersion() + "%");
        }
        if (BeanUtils.isNotEmpty(startCreateTime) && BeanUtils.isNotEmpty(endCreateTime)) {
            criteria.andBetween("createTime", startCreateTime, endCreateTime);
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<AppVersionManage> appVersionManages = this.selectByExample(example);

        if(BeanUtils.isNotEmpty(appVersionManages)){
            List<JSONObject> result = queryAssist(appVersionManages);
            return new TableResultResponse<>(pageInfo.getTotal(), result);
        }

        return new TableResultResponse<>(0L, new ArrayList<>());
    }

    /**
     * 获取单个对象
     * @param id
     * @return
     */
    public ObjectRestResponse<JSONObject> getAppVsrsionById(Integer id) {
        ObjectRestResponse<JSONObject> restResult=new ObjectRestResponse<>();

        List<AppVersionManage> list=new ArrayList<>();

        AppVersionManage appVersionManage = this.selectById(id);
        list.add(appVersionManage);

        List<JSONObject> jsonObjects = queryAssist(list);
        if(BeanUtils.isNotEmpty(jsonObjects)){
            restResult.setData(jsonObjects.get(0));
        }

        return restResult;
    }

    private List<JSONObject> queryAssist(List<AppVersionManage> list) {
        List<JSONObject> result=new ArrayList<>();

        if(BeanUtils.isEmpty(list)){
            return new ArrayList<>();
        }

        // 更新类型  手机类型
        Set<String> appDictCode=new HashSet<>();

        list.forEach(tmp->{
            appDictCode.add(tmp.getUpdateType());
            appDictCode.add(tmp.getOsType());
        });

        Map<String, String> byCodeIn = dictFeign.getByCodeIn(String.join(",", appDictCode));
        if(BeanUtils.isNotEmpty(byCodeIn)){
            for(AppVersionManage tmp:list){
                JSONObject tmpJObj = JSONObject.parseObject(JSONObject.toJSONString(tmp, SerializerFeature.WriteDateUseDateFormat));
                tmpJObj.put("updateTypeName", byCodeIn.get(tmp.getUpdateType()));
                tmpJObj.put("osTypeName", byCodeIn.get(tmp.getOsType()));
                result.add(tmpJObj);
            }
        }

        return result;
    }
}