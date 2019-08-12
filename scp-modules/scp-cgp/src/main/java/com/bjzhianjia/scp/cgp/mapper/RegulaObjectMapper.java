package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	/**
	 * 通过网格ids，统计监管对象类型
	 *
	 * @param gridIds 网格ids
	 * @return
	 */
	List<Map<String, String>> selectRegulaObjCountByType(@Param("gridIds") Set<Integer> gridIds);
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

	/**
	 * 查询全部定位
	 * @param regulaObject
     * @return
	 */
    List<RegulaObject> allPosition(@Param("regulaObject") RegulaObject regulaObject);

	/**
	 * 通过监管对象类型和名称查询
	 * @param objType 对象类型id
	 * @param objName 名称查询
	 * @return
	 */
	List<Map<String,Object>> selectByTypeAndName(@Param("objType") Set<String> objType, @Param("objName") String objName);
}
