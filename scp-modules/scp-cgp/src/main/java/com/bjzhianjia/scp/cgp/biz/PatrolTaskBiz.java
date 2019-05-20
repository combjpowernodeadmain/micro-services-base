package com.bjzhianjia.scp.cgp.biz;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMemberMapper;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.PatrolTaskMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;


/**
 * 巡查任务记录表
 *
 * @author bo
 */
@Service
public class PatrolTaskBiz extends BusinessBiz<PatrolTaskMapper, PatrolTask> {

    @Autowired
    private PatrolTaskMapper patrolTaskMapper;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private AreaGridMemberBiz areaGridMemberBiz;

    @Autowired
    private AreaGridMemberMapper areaGridMemberMapper;

    @Autowired
    private AdminFeign adminFeign;
    
    
    /**
     * 根据查询条件搜索
     * 
     * @param patrolTask 巡查任务记录
     * @param speName    专项任务名称
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param page       页码
     * @param limit      页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> selectPatrolTaskList(PatrolTask patrolTask, String speName,
            Date startTime, Date endTime, int page, int limit) {
        // 判断是否有需求按上报人查询
        List<String> userIdList = new ArrayList<>();
        boolean isSelectByUserName=false;
        if (BeanUtil.isNotEmpty(patrolTask.getCrtUserName())) {
            JSONArray usersByName = adminFeign.getUsersByName(patrolTask.getCrtUserName());
            if (BeanUtil.isNotEmpty(usersByName)) {
                for (int i = 0; i < usersByName.size(); i++) {
                    JSONObject userJObj = usersByName.getJSONObject(i);
                    userIdList.add(userJObj.getString("id"));
                }
            }
            isSelectByUserName=true;
        }

        if (isSelectByUserName) {
            if (BeanUtil.isEmpty(userIdList)) {
                // 说明有需求按名称查询，但未找到相关人员
                return new TableResultResponse<>(0, new ArrayList<>());
            } else {
                patrolTask.setCrtUserId("'" + StringUtils.join(userIdList, ",").replaceAll(",", "','") + "'");
            }
        }

        Page<Object> result = PageHelper.startPage(page, limit);
        List<Map<String, Object>> list = patrolTaskMapper.selectPatrolTaskList(patrolTask, speName, startTime, endTime);
        Set<String> codes = this.getCodes(list);
        
        if(codes == null) {
            return new TableResultResponse<Map<String, Object>>(0, null);
        }
        
        //获取数据字典：立案单状态值、来源类型
        Map<String, String> dictData = dictFeign.getByCodeIn(String.join(",", codes));
        Object sourceType = null;
        Object status = null;
        if(list != null && list.size() > 0) {
            for(Map<String,Object> map: list) {
                if(BeanUtil.isNotEmpty(map)) {
                    sourceType =  dictData.get(map.get("sourceType"));
                    status =  dictData.get(map.get("status"));
                }
                map.put("sourceType", sourceType);
                map.put("status", status);
            }
        }
        return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
    }
    
    
    /**
     *  获取数字字典codes
     * @param list
     * @return
     */
    private Set<String> getCodes(List<Map<String, Object>> list){
        Set<String> set = new HashSet<>();
        if(list != null && list.size() > 0 ) {
            for(Map<String , Object> map : list) {
                if(BeanUtil.isNotEmpty(map)) {
                    set.add(String.valueOf(map.get("sourceType"))); //来源类型
                    set.add(String.valueOf(map.get("status"))); //立案单状态
                }
            }
            return set;
        }else {
            return null;
        }
    }

    public List<PatrolTask> getByMap(Map<String,Object> conditions){
        Example example = new Example(PatrolTask.class);
        Example.Criteria criteria = example.createCriteria();

        Set<String> keySet = conditions.keySet();
        for (String string : keySet) {
            criteria.andEqualTo(string, conditions.get(string));
        }

        List<PatrolTask> result = this.mapper.selectByExample(example);
        return result;
    }

    /**
     * 按监管对象查询巡查记录
     * @param queryData
     * @return
     */
    public List<JSONObject> listOfRegObj(JSONObject queryData) {
        return this.mapper.listOfRegObj(queryData);
    }

    /**
     * 按网格员角色分页列表
     *
     * @param patrolTask
     * @param speName
     * @param startTime
     * @param endTime
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<Map<String, Object>> selectPatrolTaskListByRole(PatrolTask patrolTask, String speName,
        Date startTime, Date endTime, int page, int limit) {
        /*
         * 1)        网格长、网格员、执法队员、安全员 登录终端后，点击巡查记录，能看到自己及所属网格中所有的巡查记录。

2)        联络员可看到自己所有巡查记录。

3)        与自己及所属网格无关的其他网格的记录不可见
         */

        // 查询当前人员网格及所属网格
        List<AreaGridMember> memList = getMemberList();
        List<Integer> gridIdListOfMem =
            memList.stream().map(AreaGridMember::getGridId).distinct().collect(Collectors.toList());

        patrolTask.setCrtUserId(BaseContextHandler.getUserID());
        Page<Object> result = PageHelper.startPage(page, limit);
        List<Map<String, Object>> patrolTaskList =
            patrolTaskMapper.selectPatrolTaskListByRole(patrolTask, speName, startTime, endTime, gridIdListOfMem);

        if (BeanUtil.isEmpty(patrolTaskList)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        Set<String> codes = this.getCodes(patrolTaskList);

        //        if(codes == null) {
        //            return new TableResultResponse<JSONObject>(0, null);
        //        }

        //获取数据字典：立案单状态值、来源类型
        Map<String, String> dictData = null;
        if (BeanUtil.isNotEmpty(codes)) {
            dictData = dictFeign.getByCodeIn(String.join(",", codes));
        }
        if (BeanUtil.isEmpty(dictData)) {
            dictData = new HashMap<>();
        }

        Object sourceType = null;
        Object status = null;
        for (Map<String, Object> map : patrolTaskList) {
            if (BeanUtil.isNotEmpty(map)) {
                sourceType = dictData.get(map.get("sourceType"));
                status = dictData.get(map.get("status"));
            }
            map.put("sourceType", sourceType);
            map.put("status", status);
        }
        return new TableResultResponse<>(result.getTotal(), patrolTaskList);
    }

    private List<AreaGridMember> getMemberList() {
        List<AreaGridMember> memList;
        AreaGridMember areaGridMember=new AreaGridMember();
        areaGridMember.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        areaGridMember.setIsDisabled(BooleanUtil.BOOLEAN_FALSE);
        areaGridMember.setGridMember(BaseContextHandler.getUserID());
        memList=areaGridMemberMapper.select(areaGridMember);
        return BeanUtil.isEmpty(memList)?new ArrayList<>():memList;
    }
}