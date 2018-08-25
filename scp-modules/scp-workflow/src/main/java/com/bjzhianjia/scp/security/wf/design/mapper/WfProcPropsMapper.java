package com.bjzhianjia.scp.security.wf.design.mapper;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.wf.design.entity.WfProcPropsBean;

@Repository
public interface WfProcPropsMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(WfProcPropsBean record);
    List<WfProcPropsBean> selectByPrimaryKey(Integer id);
    List<WfProcPropsBean> selectPropertyList(WfProcPropsBean record);
    int updateByPrimaryKeySelective(WfProcPropsBean record);
}