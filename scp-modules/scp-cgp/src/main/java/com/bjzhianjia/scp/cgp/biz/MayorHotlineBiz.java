package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.MayorHotlineMapper;
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
 * 记录来自市长热线的事件
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-08 16:14:18
 */
@Service
public class MayorHotlineBiz extends BusinessBiz<MayorHotlineMapper, MayorHotline> {

    @Autowired
    private DictFeign dictFeign;

    /**
     * 获取目前ID最大的那条记录
     * 
     * @author 尚
     * @return
     */
    public MayorHotline getMaxOne() {
        Example example = new Example(MayorHotline.class);
        example.setOrderByClause("id desc");

        PageHelper.startPage(1, 1);
        List<MayorHotline> mayorHotlineList = this.mapper.selectByExample(example);
        if (mayorHotlineList == null || mayorHotlineList.isEmpty()) {
            return null;
        }
        return mayorHotlineList.get(0);
    }

    /**
     * 按条件查询
     * 
     * @author 尚
     * @param conditions
     * @return
     */
    public List<MayorHotline> getByMap(Map<String, Object> conditions) {
        Example example = new Example(MayorHotline.class);
        Criteria criteria = example.createCriteria();

        Set<String> keySet = conditions.keySet();
        for (String key : keySet) {
            criteria.andEqualTo(key, conditions.get(key));
        }

        List<MayorHotline> list = this.selectByExample(example);
        return list;
    }

    /**
     * 分页获取对象
     * 
     * @author 尚
     * @param mayorHotline
     * @return
     */
    public TableResultResponse<MayorHotline> getList(MayorHotline mayorHotline, int page, int limit, String startTime,
        String endTime,String sortColumn) {
        Example example = new Example(MayorHotline.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        // 通过热线名称或者编码查询
        if (StringUtils.isNotBlank(mayorHotline.getHotlnTitle()) || StringUtils.isNotBlank(mayorHotline.getHotlnCode())) {
            StringBuilder sql = new StringBuilder();
            sql.append("( (hotln_title like '%").append(mayorHotline.getHotlnTitle()).append("%') ")
                    .append(" OR (hotln_code like '%").append(mayorHotline.getHotlnCode()).append("%')")
                    .append(") ");
            criteria.andCondition(sql.toString());
        }

        String appealPerson = mayorHotline.getAppealPerson();
        if (StringUtils.isNotBlank(appealPerson)) {
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            // 如果用户输入了纯数字，则认为用户想按手机号进行查询，不名不可能是纯数字
            if (pattern.matcher(appealPerson.trim()).matches()) {
                criteria.andLike("appealTel", "%" + appealPerson + "%");
            } else {
                criteria.andLike("appealPerson", "%" + appealPerson + "%");
            }
        }
        if (StringUtils.isNotBlank(mayorHotline.getHotlnType())) {
            criteria.andLike("hotlnType", "%" + mayorHotline.getHotlnType() + "%");
        }
        if (StringUtils.isNotBlank(mayorHotline.getExeStatus())) {
            criteria.andEqualTo("exeStatus", mayorHotline.getExeStatus());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            Date _startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            Date _endDateTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            Date _endDate = DateUtils.addDays(_endDateTmp, 1);
            criteria.andBetween("appealDatetime", _startTime, _endDate);
        }
        if(StringUtils.isNotBlank(mayorHotline.getHotlnSource())){
            criteria.andEqualTo("hotlnSource", mayorHotline.getHotlnSource());
        }

        // 判断是否存在排序字段
        if (StringUtils.isNotBlank(sortColumn)) {
            this.setSortColumn(example, sortColumn);
        } else {
            // 默认id降序
            example.setOrderByClause(" id desc");
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<MayorHotline> result = this.mapper.selectByExample(example);

        return new TableResultResponse<MayorHotline>(pageInfo.getTotal(), result);
    }
    /**
     * 设置排序字段
     * <p>
     * 此方法直接接受前端的参数进行sql拼接，修改此方法需注意sql注入
     * </p>
     *
     * @param example   查询对象
     * @param sortColumn 查询条件
     */
    private void setSortColumn(Example example,String sortColumn) {
        // 判断是否有排序条件
        if (BeanUtils.isNotEmpty(sortColumn)) {
            String[] columns = sortColumn.split(":");
            // 排序字段的解析长度
            int len = 2;
            if (len == columns.length) {
                String orderColumn = null;
                // 获取sql拼接字段
                switch (columns[0]) {
                    // ID
                    case "id":
                        orderColumn = "id ";
                        break;
                    // 热线事项编号
                    case "hotlnCode":
                        orderColumn = "hotln_code ";
                        break;
                    // 诉求时间
                    case "appealDatetime":
                        orderColumn = "appeal_datetime ";
                        break;
                    default:
                        break;
                }
                // 获取排序规则
                String sort = "desc";
                if (!sort.equals(columns[1])) {
                    sort = "asc";
                }
                // 设置排序字段和规则
                example.setOrderByClause(orderColumn + sort);
            }
        }
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

        List<MayorHotline> selectByIds = this.mapper.selectByIds(String.join(",", idList));

        String toDoExeStatus =
            CommonUtil.exeStatusUtil(dictFeign, Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_TODO);

        for (MayorHotline mayorHotline : selectByIds) {
            if (!toDoExeStatus.equals(mayorHotline.getExeStatus())) {
                Map<String, String> todoNameMap =
                    dictFeign.getByCode(Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_TODO);
                result.setIsSuccess(false);
                result.setMessage(
                    "不能删除非【" + todoNameMap.get(Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_TODO) + "】的事件");
                return result;
            }
        }

        this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), new Date());

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }
}