package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.LeadershipAssignMapper;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 09:11:11
 */
@Service
public class LeadershipAssignBiz extends BusinessBiz<LeadershipAssignMapper, LeadershipAssign> {

    @Autowired
    private DictFeign dictFeign;

    /**
     * 获取目前ID最大的那条记录
     * 
     * @author 尚
     * @return
     */
    public LeadershipAssign getMaxOne() {
        Example example = new Example(MayorHotline.class);
        example.setOrderByClause("id desc");

        PageHelper.startPage(1, 1);
        List<LeadershipAssign> leadershipAssignList = this.mapper.selectByExample(example);
        if (leadershipAssignList == null || leadershipAssignList.isEmpty()) {
            return null;
        }
        return leadershipAssignList.get(0);
    }

    /**
     * 分页获取对象
     * 
     * @author 尚
     * @param leadershipAssign
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     */
    public TableResultResponse<LeadershipAssign> getList(LeadershipAssign leadershipAssign, int page, int limit,
        String startTime, String endTime) {
        Example example = new Example(LeadershipAssign.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(leadershipAssign.getTaskCode())) {
            criteria.andLike("taskCode", "%" + leadershipAssign.getTaskCode() + "%");
        }
        if (StringUtils.isNotBlank(leadershipAssign.getTaskTitle())) {
            criteria.andLike("taskTitle", "%" + leadershipAssign.getTaskTitle() + "%");
        }
        if (StringUtils.isNotBlank(leadershipAssign.getTaskAddr())) {
            criteria.andLike("taskAddr", "%" + leadershipAssign.getTaskAddr() + "%");
        }
        if (StringUtils.isNotBlank(leadershipAssign.getExeStatus())) {
            criteria.andEqualTo("exeStatus", leadershipAssign.getExeStatus());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            Date _startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            Date _endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            Date _endTime = DateUtils.addDays(_endTimeTmp, 1);

            criteria.andBetween("taskTime", _startTime, _endTime);
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<LeadershipAssign> result = this.selectByExample(example);
        return new TableResultResponse<>(pageInfo.getTotal(), result);
    }

    /**
     * 打量删除对象
     * 
     * @author 尚
     * @param ids
     */
    public Result<Void> remove(Integer[] ids) {
        Result<Void> result = new Result<>();
        List<String> idStrList = new ArrayList<>();
        for (int id : ids) {
            idStrList.add(String.valueOf(id));
        }

        List<LeadershipAssign> leadershipListInDB = this.mapper.selectByIds(String.join(",", idStrList));
        for (LeadershipAssign leadershipAssign : leadershipListInDB) {
            if (!Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_TODO.equals(leadershipAssign.getExeStatus())) {
                // 待删除的对象中包含有不是未发起状态的记录
                Map<String, String> todoNameMap =
                    dictFeign.getByCode(Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_TODO);
                result.setIsSuccess(false);
                result.setMessage(
                    "不能删除非【" + todoNameMap.get(Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_TODO) + "】的事件");
                return result;
            }
        }

        this.mapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), new Date());

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }
}