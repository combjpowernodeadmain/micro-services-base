package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.util.HttpUtil;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.AffairCache;
import com.bjzhianjia.scp.cgp.mapper.AffairCacheMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AffairCacheBiz 第三方事件数据缓存.
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年12月4日          chenshuai      1.0            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@Service
@Transactional
@Slf4j
public class AffairCacheBiz extends BusinessBiz<AffairCacheMapper, AffairCache> {
    @Autowired
    private Environment environment;

    /**
     * 更新缓存数据表
     */
    public void insertSelective() {
        //日期筛选条件参数
        JSONObject params = new JSONObject();
        params.put("CurrAffairCode", "");
        params.put("AffairName", "");
        params.put("ClassifyId", 0);
        params.put("PersonName", "");
        params.put("CardNum", "");


        //响应集
        JSONObject response = null;
        String url = environment.getProperty("common.external.url");
        url += "/XXKAffairInfo/GetAffairInfo";
        //查询缓存表
        Example example = new Example(AffairCache.class);
        Integer count = this.mapper.selectCountByExample(example);

        if (count != null) {
            Date starTime = null;
            String endTime = DateUtil.dateFromDateToStr(new Date(), DateUtil.DEFAULT_DATE_FORMAT);
            //TODO 系统第一次对接接口时调用，数据量太大需要优化
            //当前系统没有数据时,默认去拉取第三方全部数据
            if (count == 0) {
                starTime = DateUtil.getMonthFullDay(2000, 1);
                params.put("BeginDate", DateUtil.dateFromDateToStr(starTime, DateUtil.DEFAULT_DATE_FORMAT));
                params.put("EndDate", endTime);
                //结果集
                log.info("---------第三方接口缓存数据初始化开始---------");
                response = HttpUtil.post(url, params);
                this.addTempData(response);
                log.info("---------第三方接口缓存数据初始化结束---------");
            }
            //当前系统已有缓存数据时,获取缓存表中 “ 受理时间 ” 为最新的时间点。
            if (count > 0) {
                AffairCache affairCache = this.mapper.selectNewAcceptTime();
                if (affairCache != null) {
                    //最新的受理时间点
                    starTime = DateUtil.addSecond(affairCache.getAcceptTime(), 1);
                    params.put("BeginDate", DateUtil.dateFromDateToStr(starTime, DateUtil.DEFAULT_DATE_FORMAT));
                    params.put("EndDate", endTime);
                    //结果集
                    response = HttpUtil.post(url, params);
                    this.addTempData(response);
                }
            }
        }
    }

    /**
     * 遍历结果集添加缓存数据
     *
     * @param response
     */
    private void addTempData(JSONObject response) {
        if (response != null) {
            //遍历插入表中
            JSONArray datas = response.getJSONArray("Data");
            if (BeanUtil.isNotEmpty(datas)) {
                JSONObject temp = null;
                for (int i = 0; i < datas.size(); i++) {
                    temp = datas.getJSONObject(i);
                    if (temp != null) {
                        this.mapper.insertSelective(this.copeData(temp));
                    }
                }
            }
        }
    }

    /**
     * 初始化结果集
     *
     * @param temp 结果集单条数据
     * @return
     */
    private AffairCache copeData(JSONObject temp) {
        AffairCache affairCache = new AffairCache();
        //业务流水号
        affairCache.setAffairCode(temp.getString("CurrAffairCode"));
        //事项名称
        affairCache.setAffairName(temp.getString("AffairName"));
        //受理时间
        String transTime = temp.getString("TransTime");
        // 去除多余的T
        transTime = transTime.replaceAll("T", " ");
        affairCache.setAcceptTime(DateUtil.dateFromStrToDate(transTime, DateUtil.DEFAULT_DATE_FORMAT));
        //经办人姓名
        affairCache.setAgentName(temp.getString("OperateName"));
        //办事人姓名
        affairCache.setClerkName(temp.getString("PersonName"));
        //事项结果
        affairCache.setState(temp.getString("AffairInfoResult"));
        //记录日期
        affairCache.setCrtTime(new Date());
        return affairCache;
    }

    /**
     * 翻页查询
     *
     * @param param 筛选条件参数
     *              格式：
     *              {
     *              "CurrAffairCode": "",  			------业务流水号
     *              "AffairName": "",   			------事项名称
     *              "BeginDate": "yyyy-MM-dd", 	    ------开始日期 (不能为空)
     *              "EndDate": "yyyy-MM-dd",		------截止日期 (不能为空)
     *              "ClassifyId": 0,					------业务部门
     *              "PersonName": "",		 		------办事人
     *              "CardNum": ""					------证件号码
     *              }
     * @return
     */
    public TableResultResponse<AffairCache> list(JSONObject param) {
        Example example = new Example(AffairCache.class);
        Example.Criteria criteria = example.createCriteria();
        //业务流水号
        String affairCode = param.getString("CurrAffairCode");
        //事项名称
        String affairName = param.getString("AffairName");

        //受理时间范围
        String startTime = param.getString("BeginDate");
        String endTime = param.getString("EndDate");

        //办事人
        String clerkName = param.getString("PersonName");

        // 事务编号
        if (StringUtil.isNotBlank(affairCode)) {
            criteria.andLike("affairCode", "%"+affairCode+"%");
        }
        // 事项名称
        if (StringUtil.isNotBlank(affairName)) {
            criteria.andLike("affairName", "%"+affairName+"%");
        }
        // 办事人(单位)
        if (StringUtil.isNotBlank(clerkName)) {
            criteria.andLike("clerkName", "%" + clerkName + "%");
        }
        // 受理时间范围内的数据
        if (StringUtil.isNotBlank(startTime) && StringUtil.isNotBlank(endTime)) {
            criteria.andCondition("'" + startTime + "'<= accept_time and accept_time <= '" + endTime + "'");
        }
        example.setOrderByClause(" accept_time desc ");
        Page<Object> result = PageHelper.startPage(param.getInteger("page"), param.getInteger("limit"));
        List<AffairCache> list = this.mapper.selectByExample(example);
        if (BeanUtil.isNotEmpty(list)) {
            return new TableResultResponse<>(result.getTotal(), list);
        } else {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
    }


}