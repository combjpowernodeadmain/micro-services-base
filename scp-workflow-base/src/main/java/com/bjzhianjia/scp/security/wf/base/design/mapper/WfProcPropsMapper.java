package com.bjzhianjia.scp.security.wf.base.design.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcPropsBean;

@Repository
public interface WfProcPropsMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(WfProcPropsBean record);
    int insertList(@Param("recordList")List<WfProcPropsBean> recordList);
    List<WfProcPropsBean> selectByPrimaryKey(Integer id);
    List<WfProcPropsBean> selectPropertyList(WfProcPropsBean record);
    int updateByPrimaryKeySelective(WfProcPropsBean record);
}