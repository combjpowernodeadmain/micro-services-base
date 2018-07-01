package com.bjzhianjia.scp.security.wf.mapper;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import com.bjzhianjia.scp.security.wf.entity.WfProcPropsBean;

public interface WfProcPropsMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(WfProcPropsBean record);
    List<WfProcPropsBean> selectByPrimaryKey(Integer id);
    List<WfProcPropsBean> selectPropertyList(WfProcPropsBean record);
    int updateByPrimaryKeySelective(WfProcPropsBean record);
}