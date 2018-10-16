package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.EnterpriseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.RegTypeRelationBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectTypeBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.entity.RegTypeRelation;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.mapper.PatrolTaskMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.RegulaObjectVo;
import com.bjzhianjia.scp.cgp.vo.Regula_EnterPriseVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import lombok.extern.log4j.Log4j;
import tk.mybatis.mapper.entity.Example;

/**
 * RegulaObjectService 接管对象.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月21日          尚      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author 尚
 *
 */
@Service
@Log4j
public class RegulaObjectService {

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private EventTypeBiz eventTypeBiz;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private RegulaObjectBiz regulaObjectBiz;

    @Autowired
    private EnterpriseInfoBiz enterpriseInfoBiz;

    @Autowired
    private RegulaObjectTypeBiz regulaObjectTypeBiz;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RegTypeRelationBiz regTypeRelationBiz;

    @Autowired
    private EventTypeMapper eventTypeMapper;

    @Autowired
    private PatrolTaskMapper patrolTaskMapper;

    /**
     * 添加监管对象-经营单位
     * 
     * @author 尚
     * @param regulaObject
     * @param enterpriseInfo
     * @return
     */
    public Result<Void> createRegulaObject(RegulaObject regulaObject, EnterpriseInfo enterpriseInfo) {
        // RegulaObject theMaxRegulaObject = regulaObjectBiz.getTheMaxOne();

        // int maxRegulaObjectId = -1;
        // if (theMaxRegulaObject == null) {
        // maxRegulaObjectId = 1;
        // } else {
        // maxRegulaObjectId = theMaxRegulaObject.getId() + 1;
        // }
        // regulaObject.setId(maxRegulaObjectId);// 指定监管对象记录ID
        // enterpriseInfo.setRegulaObjId(maxRegulaObjectId);// 指定企业信息的外键

        Result<Void> result = new Result<>();

        result = check(regulaObject, enterpriseInfo, false);
        if (!result.getIsSuccess()) {
            return result;
        }

        regulaObjectBiz.insertSelective(regulaObject);
        enterpriseInfo.setRegulaObjId(regulaObject.getId());// 指定企业信息的外键
        enterpriseInfoBiz.insertSelective(enterpriseInfo);
        this.initCacheData();
        return result;
    }

    /**
     * 对添加、修改操作的逻辑进行验证<br/>
     * 1 验证所选网格是否已被删除，如果是未删除，则通过，否则验证失败；<br/>
     * 2 验证所选各数据字典选项是否已被删除，如果是未删除，则通过，否则验证失败；<br/>
     * 2.1業務條線 2.2企業類型 2.3證件類型 3 验证所选事件类别是否已被删除，如果是未删除，则通过，否则验证失败；<br/>
     * 4 验证当前监管对象编码的唯一性；<br/>
     * 5 验证当前监管对象名称的唯一性；<br/>
     * 
     * @author 尚
     * @param regulaObject
     * @param enterpriseInfo
     * @param isUpdate
     * @return
     */
    private Result<Void> check(RegulaObject regulaObject, EnterpriseInfo enterpriseInfo, boolean isUpdate) {
        Result<Void> result = new Result<>();
        result.setIsSuccess(false);

        // 验证所选网格是否已被删除
        if (regulaObject.getGriId() != null) {
            AreaGrid areaGrid = areaGridBiz.selectById(regulaObject.getGriId());
            if (areaGrid == null || areaGrid.getIsDeleted().equals("1")) {
                result.setMessage("所选网格已删除");
                return result;
            }
        }

        /*
         * 验证所选事件类别是否已被删除 事件类别存储结构 22,23;25;26,27
         */
        String eventListLongStr = regulaObject.getEventList();
        if (StringUtils.isNotBlank(eventListLongStr)) {
            List<String> eventIdList = new ArrayList<>();

            String[] eventIDGroups = eventListLongStr.split(";");
            for (String eventIDGroup : eventIDGroups) {
                String[] eventIDs = eventIDGroup.split(",");
                for (String eventID : eventIDs) {
                    if (!eventID.equals("-")) {
                        eventIdList.add(eventID);
                    }
                }
            }
            if (eventIdList != null && !eventIdList.isEmpty()) {
                List<EventType> eventTypeList = eventTypeBiz.getByIds(eventIdList);
                for (EventType eventType : eventTypeList) {
                    if (eventType == null || eventType.getIsDeleted().equals("1")) {
                        result.setMessage("所选事件类别已删除");
                        return result;
                    }
                }
            }
        }

        /*
         * 验证所选各数据字典选项是否已被删除
         */

        // 验证业务 条线是否删除
        String bizListJArrayStr = regulaObject.getBizList();
        if (StringUtils.isNotBlank(bizListJArrayStr)) {
            // 字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
            Map<String, String> bizTypes = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);

            String[] byzTypeIds = bizListJArrayStr.split(",");
            for (String bizTypeId : byzTypeIds) {
                if (!bizTypeId.equals("-")) {
                    if (!bizTypes.containsKey(bizTypeId)) {
                        result.setMessage("所选业务条线不存在");
                        return result;
                    }
                }
            }
        }

        // 验证企业类型是否已被删除
        String typeCode = enterpriseInfo.getTypeCode();
        if (StringUtils.isNotBlank(typeCode)) {
            // 字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
            Map<String, String> entTypes = dictFeign.getByCode(Constances.ROOT_BIZ_ENTTYPE);
            if (!entTypes.containsKey(typeCode)) {
                result.setMessage("所选企业类型不存在");
                return result;
            }
        }

        // 验证证件类型是否删除
        String certificateType = enterpriseInfo.getCertificateType();
        if (StringUtils.isNotBlank(certificateType)) {
            // 字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
            Map<String, String> sertificateTypeMap = dictFeign.getByCode(Constances.ROOT_BIZ_CERT_T);
            if (!sertificateTypeMap.containsKey(certificateType)) {
                result.setMessage("所选证件类型不存在");
                return result;
            }
        }

        // 验证当前监管对象编码的唯一性
        String objCode = regulaObject.getObjCode();
        if (StringUtils.isNotBlank(objCode)) {
            Map<String, Object> params = new HashMap<>();
            params.put("objCode", objCode);
            TableResultResponse<RegulaObject> regulaObjectTable = regulaObjectBiz.getByMap(params);
            List<RegulaObject> rows = regulaObjectTable.getData().getRows();
            if (rows != null) {
                if (isUpdate) {
                    for (RegulaObject tmp : rows) {
                        if (!tmp.getId().equals(regulaObject.getId()) && !tmp.getIsDeleted().equals("1")) {
                            result.setMessage("所填监管对象编码已存在");
                            return result;
                        }
                    }
                } else {
                    for (RegulaObject tmp : rows) {
                        if (!tmp.getIsDeleted().equals("1")) {
                            result.setMessage("所填监管对象编码已存在");
                            return result;
                        }
                    }

                    // if (rows.size() > 0) {
                    // result.setMessage("所填监管对象编码已存在");
                    // return result;
                    // }
                }

            }
        }

        // 验证当前监管对象名称的唯一性
        String objName = regulaObject.getObjName();
        if (StringUtils.isNotBlank(objName)) {
            Map<String, Object> params = new HashMap<>();
            params.put("objName", objName);
            TableResultResponse<RegulaObject> regulaObjectTable = regulaObjectBiz.getByMap(params);
            List<RegulaObject> rows = regulaObjectTable.getData().getRows();
            if (rows != null) {
                if (isUpdate) {
                    for (RegulaObject tmp : rows) {
                        if (!tmp.getId().equals(regulaObject.getId()) && !tmp.getIsDeleted().equals("1")) {
                            result.setMessage("所填监管对象名称已存在");
                            return result;
                        }
                    }
                } else {
                    for (RegulaObject tmp : rows) {
                        if (!tmp.getIsDeleted().equals("1")) {
                            result.setMessage("所填监管对象名称已存在");
                            return result;
                        }
                    }
                }
            }
        }

        // 判断是否有地理信息
        if (StringUtils.isNotBlank(regulaObject.getMapInfo())) {
            JSONObject jObj = JSONObject.parseObject(regulaObject.getMapInfo());
            // {"lng":116.2993,"lat":40.060234}
            regulaObject.setLatitude(jObj.getFloat("lat"));
            regulaObject.setLongitude(jObj.getFloat("lng"));
        }

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }

    /**
     * 修改监管对象-经营单位
     * 
     * @author 尚
     * @param regulaObject
     * @param enterpriseInfo
     * @return
     */
    public Result<Void> updateRegulaObject(RegulaObject regulaObject, EnterpriseInfo enterpriseInfo) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("regulaObjId", regulaObject.getId());
        List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoBiz.getByMap(conditions);
        if (enterpriseInfoList != null && !enterpriseInfoList.isEmpty()) {
            enterpriseInfo.setId(enterpriseInfoList.get(0).getId());
        } else {
            throw new NullPointerException("没有与待更新的监管对象对应的企业信息");
        }

        Result<Void> result = new Result<>();

        result = check(regulaObject, enterpriseInfo, true);
        if (!result.getIsSuccess()) {
            return result;
        }

        regulaObjectBiz.updateSelectiveById(regulaObject);
        enterpriseInfoBiz.updateSelectiveById(enterpriseInfo);
        this.initCacheData();
        return result;
    }

    /**
     * 按条件分页查询
     * 
     * @author 尚
     * @param regulaObject
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<RegulaObjectVo> getList(RegulaObject regulaObject, int page, int limit,
        boolean isObjType) {
        TableResultResponse<RegulaObject> tableResult = new TableResultResponse<>();
        if (isObjType) {
            List<Integer> secondCategoryId = new ArrayList<>();
            if (regulaObject.getObjType() != null) {
                // 进行了按二级类型查询监管对象

                // 如果isObjType为true，则在regulaObject里封装的监管对象类型参数为二级类型，需要将对应的三级类型查出，再去查找相应的监管对象
                secondCategoryId.add(regulaObject.getObjType());
                /*
                 * 此时将objType设为null，因为按该分支的逻辑，在后续会执行obj_type in (... ...)<br/>
                 * objType属性的作用只是将二级监管对象类型ID附着在其上
                 */
                regulaObject.setObjType(null);
            } else {
                // 根据`reg_type_relation`表的配置项进行查询
                String regulaObjTypeIds = "";
                RegTypeRelation reTypeRelation =
                    new RegTypeRelation(Constances.RegTypeRelation.CONCERNED_COMPANY, Constances.RegTypeRelation.Z_Z);
                List<RegTypeRelation> list = regTypeRelationBiz.getList(reTypeRelation);
                if (list != null && !list.isEmpty()) {
                    regulaObjTypeIds = list.get(0).getRegObjId();
                }
                String[] split = regulaObjTypeIds.split(",");
                for (String string : split) {
                    secondCategoryId.add(Integer.valueOf(string));
                }
            }
            List<RegulaObjectType> third = regulaObjectTypeBiz.getBySecondCategory(secondCategoryId);// 根据二级类型查出
                                                                                                     // 的三级类型
            List<Integer> collect = new ArrayList<>();
            if (BeanUtil.isNotEmpty(third)) {
                collect = third.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());
            }

            tableResult = regulaObjectBiz.getList(regulaObject, page, limit, collect);
        } else {
            tableResult = regulaObjectBiz.getList(regulaObject, page, limit, null);
        }
        List<RegulaObject> rows = tableResult.getData().getRows();

        /*
         * 进行业务条线聚和 业务条线存储结构[{"bizList","$业务条线ID"},{"bizList","$业务条线ID"}]
         */
        List<RegulaObjectVo> voList = queryAssist(rows);

        return new TableResultResponse<>(tableResult.getData().getTotal(), voList);
    }

    private List<RegulaObjectVo> queryAssist(List<RegulaObject> rows) {
        List<RegulaObjectVo> voList = BeanUtil.copyBeanList_New(rows, RegulaObjectVo.class);

        if (voList != null && !voList.isEmpty()) {
            Set<String> bizTypeIds = new HashSet<>();
            Set<String> objTypeIdStrList = new HashSet<>();

            for (RegulaObjectVo tmp : voList) {
                // 数据库记录中biz_list字段有可能 为空
                String bizList = tmp.getBizList();
                if (StringUtils.isNotBlank(bizList) && !"-".equals(bizList)) {
                    String[] split = bizList.split(",");
                    for (String string : split) {
                        bizTypeIds.add(string);
                    }
                }

                objTypeIdStrList.add(tmp.getObjType() + "");
            }

            // 聚和业务条线及事件类别
            if (bizTypeIds != null && !bizTypeIds.isEmpty()) {
                Map<String, String> bizTypeMap = dictFeign.getByCodeIn(String.join(",", bizTypeIds));

                for (RegulaObjectVo tmp : voList) {
                    JSONArray bizResultJArray = new JSONArray();

                    String bizList = tmp.getBizList();
                    boolean isBizListEmpty = (bizList == null || bizList.isEmpty());
                    String[] bizListSplit = isBizListEmpty ? new String[0] : bizList.split(",");

                    for (int i = 0; i < bizListSplit.length; i++) {
                        String string = bizListSplit[i];
                        JSONObject bizResultJObject = new JSONObject();
                        if (!string.equals("-")) {
                            bizResultJObject.put("code", string);
                            bizResultJObject.put("labelDefault", bizTypeMap.get(string));
                        }
                        bizResultJArray.add(bizResultJObject);
                    }

                    tmp.setBizList(bizResultJArray.toJSONString());
                }
            }

            // 所属监管对象类型
            if (objTypeIdStrList != null && !objTypeIdStrList.isEmpty()) {
                List<RegulaObjectType> regulaObjTypeList =
                    regulaObjectTypeBiz.selectByIds(String.join(",", objTypeIdStrList));
                Map<Integer, String> type_ID_NAME_Map =
                    regulaObjTypeList.stream()
                        .collect(Collectors.toMap(RegulaObjectType::getId, RegulaObjectType::getObjectTypeName));
                for (RegulaObjectVo tmp : voList) {
                    tmp.setObjTypeName(type_ID_NAME_Map.get(tmp.getObjType()));
                }
            }

            // 整合每个监管对象被巡查的次数
            List<Integer> regObjIdList = voList.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());
            // 以regObjIdList为基础，查询巡查记录表
            List<JSONObject> regulaObjCount = patrolTaskMapper.regulaObjCount(regObjIdList);
            // 将regulaObjCount转化为key-value形式
            Map<Integer, Integer> regObj_ID_COUNT_Map = new HashMap<>();
            if (BeanUtil.isNotEmpty(regulaObjCount)) {
                for (JSONObject jsonObject : regulaObjCount) {
                    regObj_ID_COUNT_Map.put(jsonObject.getInteger("regula_object_id"), jsonObject.getInteger("rcount"));
                }
            }
            for (RegulaObjectVo tmp : voList) {
                tmp.setPatrolCount(
                    BeanUtil.isEmpty(regObj_ID_COUNT_Map.get(tmp.getId())) ? 0 : regObj_ID_COUNT_Map.get(tmp.getId()));
            }
        }
        return voList;
    }

    /**
     * 获取单个对象
     * 
     * @author 尚
     * @param id
     * @return
     */
    public Regula_EnterPriseVo getById(Integer id) {
        // 查询监管对象
        RegulaObject regulaObject = regulaObjectBiz.selectById(id);

        if (regulaObject == null) {
            return null;
        } else if ("1".equals(regulaObject.getIsDeleted())) {
            return null;
        }

        // 查询企业信息
        Example example = new Example(EnterpriseInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("regulaObjId", id);

        List<EnterpriseInfo> list = enterpriseInfoBiz.selectByExample(example);
        EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
        if (BeanUtil.isNotEmpty(list)) {
            enterpriseInfo = list.get(0);
        }

        // List<RegulaObject> list=new ArrayList<>();
        // list.add(regulaObject);

        // 聚和监管对象类别
        // 监管对象类别放弃使用字典值，类型使用监管对象类别表，数据类型为Integer
        Integer objTypeId = regulaObject.getObjType();
        RegulaObjectType regulaObjectType = regulaObjectTypeBiz.selectById(objTypeId);

        /*
         * 聚和企业类型与证件类型
         */
        String typeCodeId = enterpriseInfo.getTypeCode();
        String certificateTypeId = enterpriseInfo.getCertificateType();
        String bizTypeId = regulaObject.getBizList();

        Set<String> dictIds = new HashSet<>();
        dictIds.add(typeCodeId);
        dictIds.add(certificateTypeId);
        String[] split = bizTypeId.split(",");
        for (String string : split) {
            if (!"-".equals(string)) {
                dictIds.add(string);
            }
        }

        Map<String, String> map = new HashMap<>();
        if (dictIds != null && !dictIds.isEmpty()) {
            map = dictFeign.getByCodeIn(String.join(",", dictIds));
        }

        // 所属网格
        Integer griId = regulaObject.getGriId();
        AreaGrid gridInDB = new AreaGrid();
        if (griId != null) {
            gridInDB = areaGridBiz.selectById(griId);
        }

        // 业务条线
        List<String> bizListNameList = new ArrayList<>();
        for (String string : split) {
            if (!"-".equals(string) && StringUtils.isNotBlank(string)) {
                bizListNameList.add(map.get(string));
            }
        }

        /*
         * 事件类别
         */
        String eventList = regulaObject.getEventList();
        // 解析事件类别ID
        Set<String> eventListId = new HashSet<>();
        String[] eventSplit1 = eventList.split(";");
        for (String string : eventSplit1) {
            if (!"-".equals(string) && StringUtils.isNotBlank(string)) {
                String[] eventSplit2 = string.split(",");
                for (String string2 : eventSplit2) {
                    eventListId.add(string2);
                }
            }
        }
        List<EventType> eventTypeList = new ArrayList<>();
        if (eventListId != null && !eventListId.isEmpty()) {
            eventTypeList = eventTypeMapper.selectByIds(String.join(",", eventListId));
        }
        Map<Integer, String> collect =
            eventTypeList.stream().collect(Collectors.toMap(EventType::getId, EventType::getTypeName));
        List<String> eventTypeName1 = new ArrayList<>();
        for (String string : eventSplit1) {
            if (!"-".equals(string) && StringUtils.isNotBlank(string)) {
                String[] eventSplit2 = string.split(",");
                List<String> eventTypeName2 = new ArrayList<>();
                for (String string2 : eventSplit2) {
                    eventTypeName2.add(collect.get(Integer.valueOf(string2)));
                }
                eventTypeName1.add(String.join(",", eventTypeName2));
            } else {
                eventTypeName1.add("-");
            }
        }

        // 企业类型
        JSONObject typeCodeJObject = new JSONObject();
        typeCodeJObject.put("code", typeCodeId);
        typeCodeJObject.put("labelDefault", map.get(typeCodeId));
        enterpriseInfo.setTypeCode(typeCodeJObject.toJSONString());

        // 证件类型
        JSONObject certificateJObj = new JSONObject();
        certificateJObj.put("code", certificateTypeId);
        certificateJObj.put("labelDefault", map.get(certificateTypeId));
        enterpriseInfo.setCertificateType(certificateJObj.toJSONString());

        String regulaObjectJStr = JSON.toJSONString(regulaObject);
        String enterpriseInfoJStr = JSON.toJSONString(enterpriseInfo);

        JSONObject other =
            BeanUtil.jsonObjectMergeOther(JSONObject.parseObject(regulaObjectJStr),
                JSONObject.parseObject(enterpriseInfoJStr));

        Regula_EnterPriseVo result = JSON.parseObject(other.toJSONString(), Regula_EnterPriseVo.class);

        JSONObject regulaObjTypeJObj = new JSONObject();
        regulaObjTypeJObj.put("id", regulaObjectType.getId());
        regulaObjTypeJObj.put("objectTypeName", regulaObjectType.getObjectTypeName());
        regulaObjTypeJObj.put("templetType", regulaObjectType.getTempletType());
        result.setObjType(regulaObjTypeJObj.toJSONString());
        result.setBizListName(String.join(",", bizListNameList));
        result.setEventListName(String.join(";", eventTypeName1));
        result.setGridName(gridInDB.getGridName());
        return result;
    }

    /**
     * 批量删除对象
     * 
     * @author 尚
     * @param ids
     *            待删除对象的ID数组
     */
    public void remove(Integer[] ids) {
        regulaObjectBiz.remove(ids);
        this.initCacheData();
    }

    /**
     * 通过指定范围，获取监管对象集
     * 
     * @param longitude
     *            经度
     * @param latitude
     *            纬度
     * @param objType
     *            监管对象类型
     * @param size
     *            范围（单位：米）
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getByDistance(double longitude, double latitude, Integer objType, Double size) {
        List<Map<String, Object>> result = new ArrayList<>();

        // 监管对象列表
        List<Map<String, Object>> objNameList =
            (List<Map<String, Object>>) redisTemplate.opsForValue().get(PatrolTask.REGULA_OBJECT_NAME);
        if (objNameList == null) {
            this.initCacheData();
            objNameList = (List<Map<String, Object>>) redisTemplate.opsForValue().get(PatrolTask.REGULA_OBJECT_NAME);
        }

        // 获取指定范围的所有监管对象
        Circle within = new Circle(new Point(longitude, latitude), new Distance(size, DistanceUnit.METERS));
        GeoResults<GeoLocation<Object>> geoResults =
            redisTemplate.opsForGeo().geoRadius(PatrolTask.REGULA_OBJECT_LOCATION, within);
        // 指定范围内的ids
        List<Integer> ids = new ArrayList<>();
        geoResults.forEach(geoLocation -> {
            ids.add((Integer) geoLocation.getContent().getName());
        });

        // 筛选指定监控类型和指定范围内的数据
        Map<String, Object> resultMap = null;
        RegulaObject regulaObject = null;
        if (objNameList.size() > 0 && (ids != null && ids.size() > 0)) {
            // 数据筛选，匹配符合监管对象类型
            Map<String, RegulaObject> objs = new HashMap<>();
            for (Map<String, Object> obj : objNameList) {
                if (objType.equals(obj.get("objType"))) { // 匹配类型
                    regulaObject = new RegulaObject();
                    regulaObject.setId(new Integer(obj.get("id").toString()));
                    regulaObject.setObjName(String.valueOf(obj.get("objName")));
                    objs.put(String.valueOf(obj.get("id")), regulaObject);
                }
            }

            for (Integer id : ids) { // 匹配范围内id
                regulaObject = objs.get(String.valueOf(id));
                if (regulaObject != null) {
                    resultMap = new HashMap<>();
                    resultMap.put("id", regulaObject.getId());
                    resultMap.put("objName", regulaObject.getObjName());
                    result.add(resultMap);
                }
            }
        }

        return result;
    }

    /**
     * 初始化监管对象缓存数据
     * <p>
     * key：regulaObjDistanceList ，value：监管对象的id和经纬度集.<br>
     * key: regulaObjNameceList , value: 监管对象id和对象名称集.
     * </p>
     */
    private void initCacheData() {
        List<Map<String, Object>> result = new ArrayList<>();

        List<RegulaObject> regulaObjects = regulaObjectBiz.selectDistanceAll();
        if (regulaObjects != null && regulaObjects.size() > 0) {
            redisTemplate.execute(new RedisCallback<Boolean>() { // 批量提交

                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    Map<String, Object> distanceMap = null;
                    for (RegulaObject regulaObject : regulaObjects) {
                        String objId = String.valueOf(regulaObject.getId());
                        // 封装id和对象名
                        distanceMap = new HashMap<>();
                        distanceMap.put("id", objId);
                        distanceMap.put("objName", regulaObject.getObjName());
                        distanceMap.put("objType", regulaObject.getObjType());
                        result.add(distanceMap);
                        // 缓存经纬度
                        if (regulaObject.getLongitude() != null && regulaObject.getLatitude() != null) {
                            connection.geoAdd(PatrolTask.REGULA_OBJECT_LOCATION.getBytes(),
                                new Point(regulaObject.getLongitude(), regulaObject.getLatitude()), objId.getBytes());
                        }
                    }
                    return true;
                }
            }, false, true);
        }

        // 缓存监管对象名称
        redisTemplate.opsForValue().set(PatrolTask.REGULA_OBJECT_NAME, result, 24, TimeUnit.DAYS);
    }
    
    /**
     * 按监管对象类型集合查询监管对象
     * 
     * @param page
     * @param limit
     * @param objTypes
     * @param name
     * @return
     */
    public TableResultResponse<RegulaObjectVo> listByObjType(int page, int limit, String objTypes, String name) {
        TableResultResponse<RegulaObject> tableResult = regulaObjectBiz.listByObjType(page, limit, objTypes, name);

        List<RegulaObject> rows = tableResult.getData().getRows();

        /*
         * 进行业务条线聚和 业务条线存储结构[{"bizList","$业务条线ID"},{"bizList","$业务条线ID"}]
         */
        List<RegulaObjectVo> voList = queryAssist(rows);

        return new TableResultResponse<>(tableResult.getData().getTotal(), voList);
    }
}
