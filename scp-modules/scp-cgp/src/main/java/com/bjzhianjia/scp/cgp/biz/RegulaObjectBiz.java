package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.mapper.EnterpriseInfoMapper;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;


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
        return mapper.selectByTypeAndGri(objType, griIds);

    }
}