package com.bjzhianjia.scp.cgp.service;

import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegulaObjectTypeService {

    @Autowired
    private RegulaObjectTypeMapper regulaObjectTypeMapper;



    public RegulaObjectType createRegulaObjectType(RegulaObjectType regulaObjectType) {

        regulaObjectTypeMapper.insertSelective(regulaObjectType);
        return regulaObjectType;
    }

    /**
     * 修改监管对象-经营单位
     * @author meng
     * @return
     */
    public int updateRegulaObject(RegulaObjectType regulaObjectType) {
        int result = regulaObjectTypeMapper.updateByPrimaryKeySelective(regulaObjectType);
        return result;
    }

}
