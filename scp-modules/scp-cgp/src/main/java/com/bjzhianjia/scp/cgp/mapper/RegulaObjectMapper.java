package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 监管对象
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:26
 */
//@Tenant
public interface RegulaObjectMapper extends CommonMapper<RegulaObject> {
	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids") Integer[] ids, @Param("updUserId") String updUserId,
			@Param("updUserName") String updUserName, @Param("updTime") Date updTime);
	
	public List<Map<String, String>> selectRegulaObjCountByType();
	/**
	 *   监管对象列表  
	 * @return
	 * 		集合中只有 id,obj_name,obj_type,longitude,latitude 属性
	 */
	public List<RegulaObject> selectDistanceAll();
	/**
	 * 通过监管对象类型和网格源查询
	 * @param objType
	 * 		监管对象类型 ids
	 * @param griIds
	 * 		网格源 ids
	 * @return
	 */
	public List<RegulaObject> selectByTypeAndGri(@Param("objType")String objType,
			@Param("griIds")String griIds);
}
