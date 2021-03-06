package com.bjzhianjia.scp.security.admin.biz;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.merge.annonation.MergeResult;
import com.bjzhianjia.scp.security.admin.entity.Depart;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.mapper.DepartMapper;
import com.bjzhianjia.scp.security.admin.mapper.UserMapper;
import com.bjzhianjia.scp.security.admin.service.TableResultParser;
import com.bjzhianjia.scp.security.admin.vo.DepartTree;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.exception.base.BusinessException;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 *
 *
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0
 */
@Service
@Slf4j
public class DepartBiz extends BusinessBiz<DepartMapper,Depart> {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MergeCore mergeCore;

    @MergeResult(resultParser = TableResultParser.class)
    public TableResultResponse<User> getDepartUsers(String departId,String userName) {
        List<User> users = this.mapper.selectDepartUsers(departId,userName.trim());
        try {
            mergeCore.mergeResult(User.class, users);
        } catch (Exception ex) {
            log.error("merge data exception", ex);
        }
        return new TableResultResponse<User>(users.size(),users);
    }

    public void addDepartUser(String departId, String userIds) {
        if (!StringUtils.isEmpty(userIds)) {
            String[] uIds = userIds.split(",");
            for (String uId : uIds) {
                this.mapper.insertDepartUser(UUIDUtils.generateUuid(),departId,uId, BaseContextHandler.getTenantID());
            }
        }
    }

    /**
     * 根据ID批量获取部门值
     * @param departIDs
     * @return
     */
    public Map<String,String> getDeparts(String departIDs){
        if(StringUtils.isBlank(departIDs)) {
            return new HashMap<>();
        }
        departIDs = "'"+departIDs.replaceAll(",","','")+"'";
        List<Depart> departs = mapper.selectByIds(departIDs);
        return departs.stream().collect(Collectors.toMap(Depart::getId, depart -> JSONObject.toJSONString(depart)));
    }
    
    public Map<String,String> getLayerDeparts(String departIds) {
    	if(StringUtils.isBlank(departIds)) {
            return new HashMap<>();
        }
    	
    	List<Depart> departs = mapper.selectAll();
    	
    	String[] aryId = departIds.split(",");
    	Map<String,String> mapResult = new HashMap<>(); 
    	for(String id:aryId) {
    		List<Depart> result = new ArrayList<>();
    		getDepart(departs, id, result);
    		if(result.size() > 0) {
    			Collections.reverse(result);
    			List<String> names = new ArrayList<>();
    			List<String> ids=new ArrayList<>();
    			result.forEach(d->names.add(d.getName()));
    			result.forEach(d->ids.add(d.getId()));
    			String name= String.join("-", names.toArray(new String[names.size()]));
    			String idString=String.join("-", ids.toArray(new String[ids.size()]));
    			
    			Map<String, String> map=new HashMap<>();
    			map.put("name", name);
    			map.put("id", idString);
    			mapResult.put(id,JSON.toJSONString(map));
    			
//    			if(result.size() > 0) {
//        			Collections.reverse(result);
//        			List<String> names = new ArrayList<>();
//        			result.forEach(d->names.add(d.getName()));
//        			mapResult.put(id, String.join("-", names.toArray(new String[names.size()])));
//        		}
    		}
    	}
    	
    	return mapResult;
    }
    
    private void getDepart(List<Depart> departs, String id, List<Depart> result) {

    	Depart tmpDepart = null;
    	for(Depart depart:departs) {
    		
    		if(depart.getId().equals(id)) {
    			tmpDepart = depart;
    			break;
    		}
    	}
    	
    	if(tmpDepart != null && !tmpDepart.getParentId().equals("-1")) {
    		result.add(tmpDepart);
    		getDepart(departs, tmpDepart.getParentId(), result);
    	}else {
    		//By尚
    		/*
    		 * 剔除result中Depart==null的对象，因权限问题，某部门的父部门有可能查询不到
    		 */
    		if(tmpDepart!=null) {
    			result.add(tmpDepart);
    		}
    	}
    	
//    	for(int i=0;i<result.size();i++) {
//    		if(result.get(i)==null) {
//    			result.remove(i);
//    			i--;
//    		}
//    	}
    }

    public void delDepartUser(String departId, String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getDepartId().equals(departId)){
            throw new BusinessException("无法移除用户的默认关联部门,若需移除,请前往用户模块更新用户部门!");
        }
        this.mapper.deleteDepartUser(departId,userId);
    }

    @Override
    public void insertSelective(Depart entity) {
        entity.setId(UUIDUtils.generateUuid());
        super.insertSelective(entity);
    }
    
    /**
     * 根据父部门ID获取子部门
     * @author 尚
     * @param parentId
     * @return
     */
    public List<Depart> getDeptByParent(String parentId){
    	Example example=new Example(Depart.class);
    	Example.Criteria criteria=example.createCriteria();
    	
    	criteria.andEqualTo("parentId",parentId);
    	List<Depart> deptList = mapper.selectByExample(example);
    	return deptList;
    }

    /**
     * 查询执法分队
     * @return
     */
    public List<Depart> getEnforcersGroup(){
        Example example = new Example(Depart.class);
        Criteria criteria = example.createCriteria();
        //1 执法分队 0非执法分队
        String isEnforcersGroup = "1";
        criteria.andEqualTo("attr1", isEnforcersGroup);
        //执法中队默认通过创建时间降序
        example.setOrderByClause(" crt_time asc");
        return this.mapper.selectByExample(example);
    }

    /**
     * 通过部门id获取所在部门和子部门的集合
     *
     * @param departId 部门id
     * @return
     */
    public ObjectRestResponse<List<DepartTree>> getDeptSonByDepartId(String departId, String name) {
        ObjectRestResponse<List<DepartTree>> result = new ObjectRestResponse<>();
        //返回结果集，当前部门和子部门
        List<DepartTree> resultData = new ArrayList<>();
        //添加当前部门信息
        Depart depart = this.selectById(departId);
        if (depart == null) {
            result.setMessage("没有匹配的部门信息！");
            result.setStatus(400);
            return result;
        }
        resultData.add(this.resetBean(depart));

        //添加当前部门的子部门集
        List<Depart> departList = this.selectListAll() ; //this.selectByExample(example);
        if (departList != null && !departList.isEmpty()) {
            this.getDepartSon(departId, departList, resultData,name);
        }
        //构建成前台需要的树型数据
        //bulid 方法会构建指定部门的子集，所以第二个参数需要传入当前部门的父级id， 否则无法封装当前部门信息
        //resultData.get(0) 当前部门
        result.data(TreeUtil.bulid(resultData, resultData.get(0).getParentId(),(Comparator<DepartTree>)(o1,o2)->{
            try {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            } catch (Exception e) {
                return 0;
            }
        }));
        return result;
    }

    /**
     * 通过部门id获取子部门集
     *
     * @param departId   部门id
     * @param departList 所有部门集
     * @param resultData 结果集部门列表
     */
    private void getDepartSon(String departId, List<Depart> departList, List<DepartTree> resultData,String name) {
        for (Depart depart : departList) {
            if (departId.equals(depart.getParentId())) {
                if(StringUtils.isNotBlank(name)) {
                    if(!this.matchingName(depart.getName(), name)) {
                        continue;
                    }
                }
                resultData.add(this.resetBean(depart));
                this.getDepartSon(depart.getId(), departList, resultData,name);
            }
        }
    }

    /**
     * 分装成部门树节点
     *
     * @param depart 完整部门信息
     * @return DepartTree 部门树节点
     */
    private DepartTree resetBean(Depart depart) {
        DepartTree departTree = new DepartTree();
        if (depart == null) {
            return departTree;
        }
        departTree.setCode(depart.getCode());
        departTree.setLabel(depart.getName());
        departTree.setId(depart.getId());
        departTree.setParentId(depart.getParentId());
        departTree.setOrderNum(depart.getAttr2());
        return departTree;
    }
    /**
     * 模糊匹配部门名称
     * @param name 部门名称
     * @param data 匹配名称
     * @return
     *      true:匹配成功，false:匹配失败
     */
    private boolean matchingName(String name,String data) {
        return Pattern.matches(".*"+data+".*", name);
    }
    
    /**
     * 以部门ID集合批量查询部门信息
     * @param deptIds
     * @return
     */
    public JSONArray getDeptByIds(String deptIds) {
    	JSONArray resultJArray=new JSONArray();
    	
    	Example example = new Example(Depart.class);
    	Criteria criteria = example.createCriteria();
    	criteria.andIn("id", Arrays.asList(deptIds.split(",")));
    	
    	List<Depart> deptList = this.selectByExample(example);
    	
//    	List<Depart> deptList = this.mapper.selectByIds(deptIds);
    	if(deptList!=null && deptList.size()>0) {
    		for (Depart depart : deptList) {
				JSONObject resultJObj=new JSONObject();
				resultJObj.put("id", depart.getId());
				resultJObj.put("name", depart.getName());
				resultJObj.put("code", depart.getCode());
				resultJObj.put("type", depart.getType());
				
				resultJArray.add(resultJObj);
			}
    		
    		return resultJArray;
    	}
    	
    	return new JSONArray();
    }

    /**
     * 查询所有部门，返回List<JSONObject>对象
     * 该方法可用于服务间调用
     * @return
     */
    public List<JSONObject> listAll() {
        Example example = new Example(Depart.class).selectProperties("id","name","parentId","code","type");
        List<Depart> departs = this.selectByExample(example);

        List<JSONObject> result;

        if(departs!=null && !departs.isEmpty()){
            return JSONArray.parseArray(JSONArray.toJSONString(departs)).toJavaList(JSONObject.class);
        }

        return new ArrayList<>();
    }

    /**
     * 按部门预留属性查询记录
     * @param attr
     * @param attrValues
     * @return
     */
    public List<JSONObject> listByAttr(String attr, String attrValues) {
        if(StringUtils.isBlank(attr)||StringUtils.isBlank(attrValues)){
            return new ArrayList<>();
        }

        Set<String> attrValuesSet=new HashSet<>();
        attrValuesSet.addAll(Arrays.asList(StringUtils.split(attrValues, ",")));

        Example example =new Example(Depart.class);
        Criteria criteria = example.createCriteria();
        criteria.andIn(attr, attrValuesSet);

        List<JSONObject> result=new ArrayList<>();
        List<Depart> departs = this.selectByExample(example);
        if(BeanUtils.isNotEmpty(departs)){
            for(Depart depart:departs){
                JSONObject tmpJObj=new JSONObject();
                tmpJObj.put("id", depart.getId());
                tmpJObj.put("name", depart.getName());
                tmpJObj.put("parentId", depart.getParentId());
                tmpJObj.put("code", depart.getCode());
                tmpJObj.put("type", depart.getType());
                tmpJObj.put("attr1", depart.getAttr1());
                tmpJObj.put("attr2", depart.getAttr2());
                tmpJObj.put("attr3", depart.getAttr3());
                tmpJObj.put("attr4", depart.getAttr4());
                result.add(tmpJObj);
            }
        }

        return result;
    }


    public List<Depart> getAll() {
        return this.mapper.getAll();
    }
}