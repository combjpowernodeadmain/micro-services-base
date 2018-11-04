package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.RegTypeRelation;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.mapper.EnterpriseInfoMapper;
import com.bjzhianjia.scp.cgp.mapper.PatrolTaskMapper;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RegulaObjectBiz 监管对象
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年7月7日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author bo
 *
 */
@Service
@Transactional
public class RegulaObjectBiz extends BusinessBiz<RegulaObjectMapper, RegulaObject> {

    @Autowired
    private RegulaObjectMapper regulaObjectMapper;

    @Autowired
    private EnterpriseInfoMapper enterpriseInfoMapper;

    @Autowired
    private RegulaObjectTypeBiz regulaObjectTypeBiz;

    @Autowired
    private PatrolTaskMapper patrolTaskMapper;
    
    @Autowired
    private PropertiesProxy propertiesProxy;

    @Autowired
    private RegTypeRelationBiz regTypeRelationBiz;

    /**
     * 查询id最大的那条记录
     * 
     * @author 尚
     * @return
     */
    public RegulaObject getTheMaxOne() {
        Example example = new Example(RegulaObject.class);
        example.setOrderByClause("id desc");
        PageHelper.startPage(0, 1);
        List<RegulaObject> regulaObject = this.mapper.selectByExample(example);
        if (regulaObject != null && !regulaObject.isEmpty()) {
            return regulaObject.get(0);
        }
        return null;
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
    public TableResultResponse<RegulaObject> getList(RegulaObject regulaObject, int page, int limit,
        List<Integer> objTypeIdList) {
        Example example = new Example(RegulaObject.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");

        // 是否输入了按监管对象名称查询
        if (StringUtils.isNotBlank(regulaObject.getObjName())) {
            criteria.andLike("objName", "%" + regulaObject.getObjName() + "%");
        }

        // 是否输入了按监管对象类型查询
        if (regulaObject.getObjType() != null) {
            // 监管对象类型集
            List<RegulaObjectType> objectTypeList = regulaObjectTypeBiz.selectIdAll();
            if (BeanUtil.isNotEmpty(objectTypeList)) {
                // 当前对象类型子集
                Set<Integer> ids = new HashSet<>();
                this.getSonId(objectTypeList, ids, regulaObject.getObjType());
                if (!ids.isEmpty()) {
                    criteria.andIn("objType", ids);
                } else {
                    criteria.andEqualTo("objType", regulaObject.getObjType());
                }
            }

        }

        // 是否输入了按监管对象所属业务条线查询
        if (StringUtils.isNotBlank(regulaObject.getBizList())) {
            criteria.andLike("bizList", "%" + regulaObject.getBizList() + "%");
        }

        if (objTypeIdList != null && !objTypeIdList.isEmpty()) {
            criteria.andIn("objType", objTypeIdList);
        }

        example.setOrderByClause("crt_time desc");

        Page<Object> result = PageHelper.startPage(page, limit);
        List<RegulaObject> list = this.mapper.selectByExample(example);
        return new TableResultResponse<RegulaObject>(result.getTotal(), list);
    }

    /**
     * 递归查询子id集合
     * 
     * @param objectTypeList
     *            监管对象类型集
     * @param ids
     *            监管对象类型子集
     * @param id
     *            当前节点id
     */
    private void getSonId(List<RegulaObjectType> objectTypeList, Set<Integer> ids, Integer id) {
        Integer tempId;
        RegulaObjectType regulaObjectType = null;
        ids.add(id);// 把父级先加进去  By尚
        for (int i = 0; i < objectTypeList.size(); i++) {
            regulaObjectType = objectTypeList.get(i);
            if (id.equals(regulaObjectType.getParentObjectTypeId())) {
                tempId = regulaObjectType.getId();
                ids.add(tempId);
                objectTypeList.remove(i);
                this.getSonId(objectTypeList, ids, tempId);
            }
        }
    }

    public void remove(Integer[] ids) {
        Date date = new Date();

        regulaObjectMapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(), date);

        enterpriseInfoMapper.deleteByRegulaObjIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(),
            date);
    }

    /**
     * 按条件查询
     * 
     * @author 尚
     * @param condition
     *            封装条件的MAP集合，key:条件名，value:条件值
     * @return
     */
    public TableResultResponse<RegulaObject> getByMap(Map<String, Object> condition) {
        Example example = new Example(RegulaObject.class);
        Example.Criteria criteria = example.createCriteria();

        Set<String> keySet = condition.keySet();
        for (String string : keySet) {
            criteria.andEqualTo(string, condition.get(string));
        }

        List<RegulaObject> rows = regulaObjectMapper.selectByExample(example);
        return new TableResultResponse<>(-1, rows);
    }

    /**
     * 按Id查询记录
     * 
     * @author 尚
     * @param ids
     *            多个id集合，逗号隔开，如"1,2,3,4"
     * @return
     */
    public List<RegulaObject> selectByIds(String ids) {
        return this.mapper.selectByIds(ids);
    }

    public List<Map<String, String>> selectRegulaObjCountByType() {
        return this.mapper.selectRegulaObjCountByType();
    }

    /**
     * 监管对象列表
     * 
     * @return 集合中只有 id,obj_name,obj_type,longitude,latitude 属性
     */
    public List<RegulaObject> selectDistanceAll() {
        return mapper.selectDistanceAll();
    }

    /**
     * 通过监管对象类型和网格源查询监管对象
     * 
     * @param objType
     *            监管对象类型 ids
     * @param griIds
     *            网格源 ids
     * @return
     *         监管对象列表（id，name）
     */
    public List<RegulaObject> selectByTypeAndGri(String objType, String griIds) {
        griIds = "'" + griIds.replaceAll(",", "','") + "'";
        return mapper.selectByTypeAndGri(objType, griIds);

    }

    public TableResultResponse<JSONObject> getRegObjPatrolInfo(RegulaObject regulaObject, String regObjIds, int page,
        int limit) {
        Example example = new Example(RegulaObject.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        if (BeanUtil.isNotEmpty(regulaObject.getObjType())) {
            criteria.andEqualTo("objType", regulaObject.getObjType());
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<RegulaObject> regulaObjectList = this.selectByExample(example);
        if (BeanUtil.isEmpty(regulaObject)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
        if (BeanUtil.isNotEmpty(regObjIds)) {
            criteria.andIn("id", Arrays.asList(regObjIds.split(",")));
        }

        List<JSONObject> resultList = queryAssist(regulaObjectList);
        return new TableResultResponse<>(pageInfo.getTotal(), resultList);
    }

    private List<JSONObject> queryAssist(List<RegulaObject> regulaObjectList) {
        List<JSONObject> result = new ArrayList<>();

        // 收集监管对象ID集合
        List<Integer> regObjIdList =
            regulaObjectList.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());

        // 以regObjIdList为基础，查询巡查记录表
        List<JSONObject> regulaObjCount = patrolTaskMapper.regulaObjCount(regObjIdList);
        // 将regulaObjCount转化为key-value形式
        Map<Integer, Integer> regObj_ID_COUNT_Map = new HashMap<>();
        if (BeanUtil.isNotEmpty(regulaObjCount)) {
            for (JSONObject jsonObject : regulaObjCount) {
                regObj_ID_COUNT_Map.put(jsonObject.getInteger("regula_object_id"), jsonObject.getInteger("rcount"));
            }
        }

        for (RegulaObject regObjTmp : regulaObjectList) {
            JSONObject resultJObj = new JSONObject();
            resultJObj.put("id", regObjTmp.getId());
            resultJObj.put("objName", regObjTmp.getObjName());
            resultJObj.put("patrolCount", BeanUtil.isEmpty(regObj_ID_COUNT_Map.get(regObjTmp.getId())) ? 0
                : regObj_ID_COUNT_Map.get(regObjTmp.getId()));
            result.add(resultJObj);
        }
        return result;
    }
    
    /**
     * 按ID集合获取列表
     * 
     * @param ids
     * @return
     */
    public TableResultResponse<JSONObject> getByIds(Integer[] ids) {
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();
        if (BeanUtil.isEmpty(ids)) {
            restResult.setMessage("请输入监管对象ID");
            restResult.setStatus(400);
            return restResult;
        }

        Example example = new Example(RegulaObject.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andIn("id", Arrays.asList(ids));

        // 以regObjIdList为基础，查询巡查记录表
        List<JSONObject> regulaObjCount = patrolTaskMapper.regulaObjCount(Arrays.asList(ids));
        // 将regulaObjCount转化为key-value形式
        Map<Integer, Integer> regObj_ID_COUNT_Map = new HashMap<>();
        if (BeanUtil.isNotEmpty(regulaObjCount)) {
            for (JSONObject jsonObject : regulaObjCount) {
                regObj_ID_COUNT_Map.put(jsonObject.getInteger("regula_object_id"), jsonObject.getInteger("rcount"));
            }
        }

        List<RegulaObject> regObjList = this.selectByExample(example);
        List<JSONObject> resultJObj = new ArrayList<>();
        if (BeanUtil.isNotEmpty(regObjList)) {
            for (RegulaObject regulaObject : regObjList) {
                try {
                    JSONObject regulaObjectJObj =
                        propertiesProxy.swapProperties(regulaObject, "id", "mapInfo", "objName");
                    regulaObjectJObj.put("patrolCount", regObj_ID_COUNT_Map.get(regulaObject.getId()) == null ? 0
                        : regObj_ID_COUNT_Map.get(regulaObject.getId()));
                    resultJObj.add(regulaObjectJObj);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        restResult.setStatus(200);
        restResult.setMessage("成功");
        restResult.getData().setRows(resultJObj);
        return restResult;
    }

    /**
     * 按网格，查询该网格下有哪些监管对象类型
     * @param areaGridIds
     * @return 符合条件的监管对象类型ID集合
     */
    public TableResultResponse<Integer> getListNoPage(String areaGridIds) {
        TableResultResponse<Integer> restResult = new TableResultResponse<>();

        if (StringUtils.isBlank(areaGridIds)) {
            restResult.setStatus(400);
            restResult.setMessage("请选择网格");
            return restResult;
        }

        Example example = new Example(RegulaObject.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andIn("griId", Arrays.asList(areaGridIds.split(",")));

        List<RegulaObject> regObjList = this.selectByExample(example);
        List<Integer> result = new ArrayList<>();
        if (BeanUtil.isNotEmpty(regObjList)) {
            result = regObjList.stream().map(o -> o.getObjType()).distinct().collect(Collectors.toList());
        }

        restResult.setStatus(200);
        restResult.setMessage("成功");
        restResult.getData().setRows(result);
        return restResult;
    }
    
    /**
     * 按监管对象类型集合查询监管对象
     * @param page
     * @param limit
     * @param objTypes
     * @param objName
     * @return
     */
    public TableResultResponse<RegulaObject> listByObjType(int page, int limit, String objTypes, String objName) {
        TableResultResponse<RegulaObject> tableResultResponse = new TableResultResponse<>();

//        if (StringUtils.isBlank(objTypes)) {
//            tableResultResponse.setStatus(400);
//            tableResultResponse.setMessage("请选择监管对象类型");
//            return tableResultResponse;
//        }

        Example example = new Example(RegulaObject.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");

        if (StringUtils.isNotBlank(objTypes)) {
            List<RegulaObjectType> objectTypeList = regulaObjectTypeBiz.selectIdAll();
            if (BeanUtil.isNotEmpty(objectTypeList)) {
                // 当前对象类型子集
                Set<Integer> ids = new HashSet<>();

                String[] split = objTypes.split(",");
                for (String objTypeId : split) {
                    this.getSonId(objectTypeList, ids, Integer.valueOf(objTypeId));
                }
                if (!ids.isEmpty()) {
                    criteria.andIn("objType", ids);
                } else {
                    criteria.andEqualTo("objType", Integer.valueOf(objTypes));
                }
            }
        }

        if (StringUtils.isNotBlank(objName)) {
            criteria.andLike("objName", "%"+objName+"%");
        }
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<RegulaObject> regObjList = this.selectByExample(example);
        tableResultResponse.getData().setRows(regObjList);
        tableResultResponse.getData().setTotal(pageInfo.getTotal());

        return tableResultResponse;
    }

    /**
     * 判断一个监管对象是否为企业
     *
     * @param regObjId 待判断监管对象ID
     * @return true:如果一个监管对象属于企业<br/>
     *          false:如果一个监管对象不属于企业
     */
    public ObjectRestResponse<Boolean> isRegObjEnterprise(Integer regObjId){
        /*
         * 1 获取类型为企业的监管对象类型
         * 2 获取第1步查询到类型的子类型
         * 3 查询待对比监管对象属于哪个监管对象
         * 4 进行对比
         */

        ObjectRestResponse<Boolean> restResult=new ObjectRestResponse<>();

        //查询有哪些监管对象类型属于企业
        RegTypeRelation regTypeRelation=new RegTypeRelation("concerned_company", "z_z");
        List<RegTypeRelation> regTypeRelationList = regTypeRelationBiz.getList(regTypeRelation);

        RegTypeRelation inDB;

        Set<Integer> regObjIdSet=new HashSet<>();
        if(BeanUtil.isNotEmpty(regTypeRelationList)){
            //按【监管对象类型与业务之间的关系】的code与projectSign联合查到的，如果不为空，则一定唯一
            inDB=regTypeRelationList.get(0);
            if(BeanUtil.isNotEmpty(inDB)){
                String regObjIdStr = inDB.getRegObjId();
                if(StringUtils.isNotBlank(regObjIdStr)){
                    for(String regObjIdTmp:regObjIdStr.split(",")){
                        try {
                            regObjIdSet.add(Integer.valueOf(regObjIdTmp));
                        } catch (NumberFormatException e) {
                            // 如果程序在该处发生异常，不会对主逻辑造成大影响，忽略该异常，使程序继续进行
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        //查询待对比监管对象属于哪个类型
        RegulaObject regulaObject = this.selectById(regObjId);

        // 监管对象类型集
        List<RegulaObjectType> objectTypeList = regulaObjectTypeBiz.selectIdAll();
        if (BeanUtil.isNotEmpty(objectTypeList)) {
            // 当前对象类型子集
            Set<Integer> ids = new HashSet<>();
            if (BeanUtil.isNotEmpty(regObjIdSet)) {
                for (Integer regObjIdTmp : regObjIdSet) {
                    this.getSonId(objectTypeList, ids, regObjIdTmp);
                }
            }

            if(ids.contains(regulaObject.getObjType())){
                //如果企业包含的监管对象类型集里包含待对比的监管对象所属的类型，则返回true
                restResult.setData(true);
            }else{
                restResult.setData(false);
            }
        }

        return restResult;
    }

    public List<RegulaObject> allPotition(RegulaObject regulaObject) {
        return null;
    }
}