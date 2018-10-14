package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.PublicOpinionMapper;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 记录来自舆情的事件
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 09:11:11
 */
@Service
public class PublicOpinionBiz extends BusinessBiz<PublicOpinionMapper, PublicOpinion> {

    @Autowired
    private DictFeign dictFeign;

    /**
     * 获取目前ID最大的那条记录
     * 
     * @author 尚
     * @return
     */
    public PublicOpinion getMaxOne() {
        Example example = new Example(MayorHotline.class);
        example.setOrderByClause("id desc");

        PageHelper.startPage(1, 1);
        List<PublicOpinion> publicOpinionList = this.mapper.selectByExample(example);
        if (publicOpinionList == null || publicOpinionList.isEmpty()) {
            return null;
        }
        return publicOpinionList.get(0);
    }

    /**
     * 按条件查询
     * 
     * @author 尚
     * @param conditions
     * @return
     */
    public List<PublicOpinion> getByMap(Map<String, Object> conditions) {
        Example example = new Example(PublicOpinion.class);
        Criteria criteria = example.createCriteria();

        Set<String> keySet = conditions.keySet();
        for (String key : keySet) {
            criteria.andEqualTo(key, conditions.get(key));
        }

        List<PublicOpinion> list = this.selectByExample(example);
        return list;
    }

    /**
     * 分页获取对象
     * 
     * @author 尚
     * @param publicOpinion
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     */
    public TableResultResponse<PublicOpinion> getList(PublicOpinion publicOpinion, int page, int limit,
        String startTime, String endTime) {
        Example example = new Example(PublicOpinion.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(publicOpinion.getOpinTitle())) {
            criteria.andLike("opinTitle", publicOpinion.getOpinTitle());
        }
        if (StringUtils.isNotBlank(publicOpinion.getOpinCode())) {
            criteria.andLike("opinCode", "%" + publicOpinion.getOpinCode() + "%");
        }
        if (StringUtils.isNotBlank(publicOpinion.getOpinLevel())) {
            criteria.andEqualTo("opinLevel", publicOpinion.getOpinLevel());
        }
        if (StringUtils.isNotBlank(publicOpinion.getOpinType())) {
            criteria.andEqualTo("opinType", publicOpinion.getOpinType());
        }
        if (StringUtils.isNotBlank(publicOpinion.getExeStatus())) {
            criteria.andEqualTo("exeStatus", publicOpinion.getExeStatus());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            Date _startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            Date _endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            // 查询截止日期为次日00:00:00，即包括请求截止日期当天
            Date _endDate = DateUtils.addDays(_endTimeTmp, 1);

            criteria.andBetween("publishTime", _startTime, _endDate);
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<PublicOpinion> result = this.selectByExample(example);
        return new TableResultResponse<PublicOpinion>(pageInfo.getTotal(), result);
    }

    /**
     * 打量删除对象
     * 
     * @author 尚
     * @param ids
     */
    public Result<Void> remove(Integer[] ids) {
        Result<Void> result = new Result<>();

        List<String> idList = new ArrayList<>();
        for (Integer integer : ids) {
            idList.add(integer + "");
        }

        String toExeStatus =
            CommonUtil.exeStatusUtil(dictFeign, Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_TODO);

        List<PublicOpinion> selectByIds = this.mapper.selectByIds(String.join(",", idList));
        for (PublicOpinion tmp : selectByIds) {
            if (!toExeStatus.equals(tmp.getExeStatus())) {
                Map<String, String> todoName =
                    dictFeign.getByCode(Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_TODO);
                result.setIsSuccess(false);
                result.setMessage(
                    "不能删除非【" + todoName.get(Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_TODO) + "】的事件");
                return result;
            }
        }

        this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), new Date());
        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }
}