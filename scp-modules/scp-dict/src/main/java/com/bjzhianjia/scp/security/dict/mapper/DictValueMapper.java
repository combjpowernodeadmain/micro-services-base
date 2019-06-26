
package com.bjzhianjia.scp.security.dict.mapper;

import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import com.bjzhianjia.scp.security.dict.entity.DictValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
public interface DictValueMapper extends CommonMapper<DictValue> {
    /**
     * 通过DictType的code获取DictValue列表
     *
     * @param typeCodes DictType的code
     * @return
     */
    List<DictValue> selectListByTypeCode(@Param("typeCodes") List<String> typeCodes);
}
