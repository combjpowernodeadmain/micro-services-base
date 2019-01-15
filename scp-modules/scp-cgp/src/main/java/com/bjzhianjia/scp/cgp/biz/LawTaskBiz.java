package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.LawExecutePerson;
import com.bjzhianjia.scp.cgp.entity.LawPatrolObject;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.LawTaskMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.utils.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * LawTaskBiz 执法任务管理.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月9日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@Service
@Transactional
public class LawTaskBiz extends BusinessBiz<LawTaskMapper, LawTask> {

    @Autowired
    private LawPatrolObjectBiz lawPatrolObjectBiz;

    @Autowired
    private LawExecutePersonBiz lawExecutePersonBiz;

    @Autowired
    private LawTaskMapper lawTaskMapper;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private EnforceCertificateBiz enforceCertificateBiz;

    @Autowired
    private RegulaObjectBiz regulaObjectBiz;

    @Autowired
    private EventTypeBiz eventTypeBiz;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Autowired
    private Environment environment;

    public void createLawTask(LawTask lawTask) throws Exception {
        // 生成巡查记录编号
        LawTask maxOne = this.getMaxOne();
        Result<String> patrolResult = CommonUtil.generateCaseCode(maxOne != null ? maxOne.getLawTaskCode() : null);
        if (!patrolResult.getIsSuccess()) {
            throw new Exception(patrolResult.getMessage());
        }
        lawTask.setLawTaskCode("ZFRW" + patrolResult.getData());
        super.insertSelective(lawTask);
        // 添加关系表记录
        this.addExecutePerso(lawTask);
        this.addPatrolObjects(lawTask);

    }

    /**
     * 更新执法任务
     * 
     * @param lawTask
     */
    public void update(LawTask lawTask) {
        Integer lawTaskId = lawTask.getId();
        // 更新关系表
        LawTask _lawTask = super.selectById(lawTaskId);
        if (_lawTask != null) {
            // 判断巡查对象是否修改
            if (!_lawTask.getPatrolObject().equals(lawTask.getPatrolObject())) {
                lawPatrolObjectBiz.delByLawTaskId(lawTaskId);
                this.addPatrolObjects(lawTask);
            }
            // 判断执法者是否修改
            if (!_lawTask.getExecutePerson().equals(lawTask.getExecutePerson())) {
                lawExecutePersonBiz.delByLawTaskId(lawTask.getId());
                this.addExecutePerso(lawTask);
            }
            super.updateSelectiveById(lawTask);
        }
    }

    /**
     * 添加巡查对象关系表记录
     * 
     * @param lawTask
     *            执法任务
     */
    private void addPatrolObjects(LawTask lawTask) {
        // 监管对象
        JSONObject patrolObjects = JSONUtil.strToJSON(lawTask.getPatrolObject());
        Set<String> patrolObjectsIds = patrolObjects.keySet();
        if (patrolObjectsIds != null && !patrolObjectsIds.isEmpty()) {
            LawPatrolObject lawPatrolObject = null;
            for (String id : patrolObjectsIds) {
                lawPatrolObject = new LawPatrolObject();
                lawPatrolObject.setLawTaskId(lawTask.getId());
                // 监管对象id
                lawPatrolObject.setRegulaObjectId(Integer.valueOf(id));
                // 监管对象名称
                lawPatrolObject.setRegulaObjectName(patrolObjects.get(id).toString());
                lawPatrolObjectBiz.insertSelective(lawPatrolObject);
            }
        }
    }

    /**
     * 添加执法者关系表
     * 
     * @param lawTask
     *            执法任务
     */
    // TODO 优化
    private void addExecutePerso(LawTask lawTask) {
        // 格式：{id:{"name":"","deptId":""},id{}...}
        JSONArray executePerso = JSONArray.parseArray(lawTask.getExecutePerson());
        // 获取分队名称（部门名称）
        if (executePerso != null && !executePerso.isEmpty()) {
            JSONObject obj = null; // 执法者信息
            JSONObject _obj = null; // 执法者信息
            LawExecutePerson lawExecutePerson = null; // 执法者
            for (int i = 0; executePerso != null && i < executePerso.size(); i++) {
                obj = executePerso.getJSONObject(i);
                Set<String> key = obj.keySet();
                for (String userId : key) {
                    _obj = JSONObject.parseObject(obj.getString(userId));
                    // 添加任务执法者关系表
                    lawExecutePerson = new LawExecutePerson();
                    lawExecutePerson.setLawTaskId(lawTask.getId());
                    // 执法者id
                    lawExecutePerson.setUserId(userId);
                    // 执法者姓名
                    lawExecutePerson.setUserName(_obj.getString("userName"));
                    // 执法者分队id
                    lawExecutePerson.setDepartId(_obj.getString("deptId"));
                    lawExecutePersonBiz.insertSelective(lawExecutePerson);
                }

            }
        }
    }

    /**
     * 执法任务翻页查询
     * 
     * @param userName
     *            执法者姓名
     * @param regulaObjectName
     *            巡查对象名称
     * @param state
     *            任务状态
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @param page
     *            页码
     * @param limit
     *            页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> getLawTaskList(String userName, String regulaObjectName,
        String state, Date startTime, Date endTime, int page, int limit) {
        Page<Object> result = PageHelper.startPage(page, limit);
        List<Map<String, Object>> list =
            lawTaskMapper.selectLawTaskList(userName, regulaObjectName, state, startTime, endTime);
        if (list == null) {
            return new TableResultResponse<Map<String, Object>>(0, null);
        }

        Map<String, String> dictData = dictFeign.getByCode(LawTask.ROOT_BIZ_LAWTASKS);
        if (list.size() > 0) {
            // 执法者分队（部门名称）
            //收集需要进行查询的部门ID
            List<String> excutePersonList = list.stream().map(o->String.valueOf(o.get("executePerson"))).distinct().collect(Collectors.toList());
            
            Set<String> deptIdList=new HashSet<>();
            if(BeanUtil.isNotEmpty(excutePersonList)) {
                for (String excutePersonTmp : excutePersonList) {
                    /*
                     * [{"f8d0d626935b48baad56ddc30b26aa6d":{"deptId":"9250f76e502e4c99985520c06f5378fc","userName":"综合执法项目管理员"}},
                     * {"2147a930090546c1ad9d4ef98330e97c":{"deptId":"9250f76e502e4c99985520c06f5378fc","userName":"licheng"}}]
                     */
                    JSONArray excutePersonJArray = JSONArray.parseArray(excutePersonTmp);
                    for(int i=0;i<excutePersonJArray.size();i++) {
                        /*
                         * {"f8d0d626935b48baad56ddc30b26aa6d":{"deptId":"9250f76e502e4c99985520c06f5378fc","userName":"综合执法项目管理员"}}
                         */
                        JSONObject executePersonJObj = excutePersonJArray.getJSONObject(i);
                        for(String excutePersonKey:executePersonJObj.keySet()) {
                            //"deptId":"9250f76e502e4c99985520c06f5378fc"
                            String deptId = CommonUtil.getValueFromJObjStr(executePersonJObj.getString(excutePersonKey), "deptId");
                            deptIdList.add(deptId);
                        }
                    }
                }
            }
            
            JSONArray deptList = adminFeign.getDeptByIds(String.join(",", deptIdList));
            Map<String, String> dept_ID_NAME_Map=new HashMap<>();
            if(BeanUtil.isNotEmpty(deptList)) {
                for(int i=0;i<deptList.size();i++) {
                    JSONObject deptJObj = deptList.getJSONObject(i);
                    dept_ID_NAME_Map.put(deptJObj.getString("id"), deptJObj.getString("name"));
                }
            }
            
            for (Map<String, Object> map : list) {

                String executePerson = map.get("executePerson").toString();
                String _executePerson = this.getDeptName(executePerson,dept_ID_NAME_Map);
                map.put("executePerson", _executePerson);
            }
            // 任务状态（数据字典）
            if (dictData != null) {
                String _state = null;
                for (Map<String, Object> map : list) {
                    _state = dictData.get(map.get("state"));
                    map.put("state", _state);
                }
            }
        }

        return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
    }

    /**
     * 通过json获取执法分队名称（部门名称）
     * 
     * @param executePerson
     *            执法者信息
     *            [{'userid':{'userName':'xxxx','deptId':'xxxx'}},{'userid':{'userName':'xxx','deptId':'xxxx'}}]
     * @return [{'userid':{'userName':'xxxx','deptId':'deptName','':'xxx'}},{'userid':{'userName':'xxx','deptId':'xxxx','deptName':'xxxx'}}]
     */
    private String getDeptName(String executePerson,Map<String, String> dept_ID_NAME_Map) {
        // 执法者json信息
        JSONArray userObjs = JSONArray.parseArray(executePerson);
        JSONArray _userObjs = new JSONArray(); // 封装执法者信息
        JSONObject obj = null; // 执法者
        if (userObjs != null) {
            for (int i = 0; userObjs != null && i < userObjs.size(); i++) {
                JSONObject object = userObjs.getJSONObject(i);
                Set<String> userIds = object.keySet();
                for (String userId : userIds) {
                    String json = object.getString(userId);
                    obj = JSONObject.parseObject(json);
                    if (obj != null) {
                        // 法者分队（部门名称）
                        obj.put("deptName", dept_ID_NAME_Map.get(obj.getString("deptId")));
                        _userObjs.add(obj);
                    }
                }

            }
        }
        return _userObjs.toJSONString();
    }

    /**
     * 查询ID最大的那条记录
     * 
     * @author chenshuai
     * @return
     */
    public LawTask getMaxOne() {
        Example example = new Example(CaseInfo.class);
        example.setOrderByClause("id desc");
        PageHelper.startPage(1, 1);
        List<LawTask> lawTaskList = super.selectByExample(example);
        if (lawTaskList == null || lawTaskList.isEmpty()) {
            return null;
        }
        return lawTaskList.get(0);
    }

    /**
     * 双随机生成执法任务
     * 
     * @param objType
     *            监管对象类型ids
     * @param griIds
     *            网格源ids
     * @param peopleNumber
     *            每组人数
     * @param regulaObjNumber
     *            每组巡查数
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @param info
     *            任务要求
     * @param bizTypeCode
     *            业务条线
     * @param eventTypeId
     *            事件类别id
     * @param lawTitle
     *            执法任务名称
     * @throws Exception
     */
    public Result<List<JSONObject>> randomLawTask(String objType, String griIds, int peopleNumber, int regulaObjNumber,
        Date startTime, Date endTime, String info, String bizTypeCode, String eventTypeId, String lawTitle)
        throws Exception {

        Result<List<JSONObject>> result = new Result<>();
        // 执法者列表
        List<EnforceCertificate> fakeUserList = enforceCertificateBiz.getEnforceCertificateList();

        List<List<EnforceCertificate>> devideEnforceToDept = devideEnfoecerToDept(fakeUserList, peopleNumber);

        if(BeanUtil.isEmpty(devideEnforceToDept)){
            result.setIsSuccess(false);
            result.setMessage("执法者为空！");
            return result;
        }

        // 监管对象列表
        List<RegulaObject> regulaObjects = regulaObjectBiz.selectByTypeAndGri(objType, griIds);
        if (regulaObjects == null || regulaObjects.isEmpty()) {
            result.setIsSuccess(false);
            result.setMessage("监管对象类型或者网格中，不存在监管对象！");
            return result;
        }

        List<JSONObject> _regulaObjects = this.getRegulaObject(regulaObjNumber, regulaObjects);

        Random ran=new Random();

        boolean isLoop=true;
        JSONArray executePerson = null;
        JSONObject patrolObject = null;
        List<JSONObject> resultForReturn=new ArrayList<>();
        do{
            int eIndex = ran.nextInt(devideEnforceToDept.size());
            int rIndex = ran.nextInt(_regulaObjects.size());

            List<EnforceCertificate> enforceCertificates = devideEnforceToDept.get(eIndex);
            patrolObject = _regulaObjects.get(rIndex);

            List<JSONArray> _userList = this.getUserList(peopleNumber, enforceCertificates);

            executePerson = _userList.get(0);


            // 将生成的执法人与监管对象组合返回到前端
            Set<String> userNameList = new HashSet<>();
            Set<String> userIdList = new HashSet<>();
            Set<String> patrolObjectNameList = new HashSet<>();
            for (int m = 0; m < executePerson.size(); m++) {
                JSONObject executePersonJObj = executePerson.getJSONObject(m);
                for (JSONObject.Entry e : executePersonJObj.entrySet()) {
                    JSONObject jsonObject = JSONObject.parseObject(String.valueOf(e.getValue()));
                    userNameList.add(jsonObject.getString("userName"));
                    userIdList.add(String.valueOf(e.getKey()));
                }
            }
            for (JSONObject.Entry e : patrolObject.entrySet()) {
                patrolObjectNameList.add(String.valueOf(e.getValue()));
            }
            JSONObject resultTmp = new JSONObject();
            resultTmp.put("executePerson", StringUtils.join(userNameList, ","));
            resultTmp.put("executePersonId", StringUtils.join(userIdList, ","));
            resultTmp.put("patrolObject", StringUtils.join(patrolObjectNameList, ","));
            resultForReturn.add(resultTmp);
            
            LawTask lawTask = new LawTask();
            lawTask.setExecutePerson(executePerson.toJSONString());
            lawTask.setPatrolObject(patrolObject.toJSONString());
            lawTask.setStartTime(startTime);
            lawTask.setEndTime(endTime);
            lawTask.setState(LawTask.ROOT_BIZ_LAWTASKS_TODO);
            lawTask.setInfo(info);
            lawTask.setBizTypeCode(bizTypeCode);
            lawTask.setEventTypeId(eventTypeId);
            lawTask.setLawTitle(lawTitle);
            this.createLawTask(lawTask);

            devideEnforceToDept.remove(eIndex);
            _regulaObjects.remove(rIndex);

            if(devideEnforceToDept.size()<=0 || _regulaObjects.size()<=0){
                isLoop=false;
            }
        }while(isLoop);

        _randomLawTestGetUserPic(resultForReturn);

        result.setIsSuccess(true);
        result.setData(resultForReturn);
        return result;
    }

    /**
     * 将人员按部门进行分组
     * @param fakeUserList
     * @param peopleNumber
     * @return
     */
    private List<List<EnforceCertificate>> devideEnfoecerToDept(List<EnforceCertificate> fakeUserList,
                                                                     int peopleNumber) {
        if(BeanUtil.isEmpty(fakeUserList)){
            return new ArrayList<>();
        }

        Set<String> usrIdList = new HashSet<>();
        Map<String,EnforceCertificate> enforcerMap=new HashMap<>();
        fakeUserList.forEach(enforceCertificate -> {
            usrIdList.add(enforceCertificate.getUsrId());
            enforcerMap.put(enforceCertificate.getUsrId(), enforceCertificate);
        });

        if(BeanUtil.isEmpty(usrIdList)){
            // 执法证记录不为空，但没配人员
            return new ArrayList<>();
        }

        JSONArray specifyUserJArray = adminFeign.getInfoByUserIds(StringUtils.join(usrIdList, ","));

        /*
         * zhongdui.deptcode1=010804
         * zhongdui.deptcode2=010805
         * zhongdui.deptcode3=010806
         */

        List<EnforceCertificate> zh1=new ArrayList<>();
        List<EnforceCertificate> zh2=new ArrayList<>();
        List<EnforceCertificate> zh3=new ArrayList<>();
        // deptId
        if (BeanUtil.isNotEmpty(specifyUserJArray)) {
            // 一个人有可能属于多个部门,但不一定都是中队部门
            for (int i = 0; i < specifyUserJArray.size(); i++) {
                JSONObject specifyUserJObj = specifyUserJArray.getJSONObject(i);
                if (environment.getProperty("zhongdui.deptcode1")
                        .equals(specifyUserJObj.getString("deptCode"))) {
                    // 该条记录对应的部门是1中队
                    zh1.add(enforcerMap.get(specifyUserJObj.getString("userId")));
                }
                if (environment.getProperty("zhongdui.deptcode2")
                        .equals(specifyUserJObj.getString("deptCode"))) {
                    // 该条记录对应的部门是2中队
                    zh2.add(enforcerMap.get(specifyUserJObj.getString("userId")));
                }
                if (environment.getProperty("zhongdui.deptcode3")
                        .equals(specifyUserJObj.getString("deptCode"))) {
                    // 该条记录对应的部门是3中队
                    zh3.add(enforcerMap.get(specifyUserJObj.getString("userId")));
                }
            }
        }

        List<List<EnforceCertificate>> result=new ArrayList<>();
        _devideEnfoecerToDeptAssist(peopleNumber, zh1,result);
        _devideEnfoecerToDeptAssist(peopleNumber, zh2,result);
        _devideEnfoecerToDeptAssist(peopleNumber, zh3,result);

        return result;
    }

    /**
     * 将zh中的人按分组放入rr中
     * @param peopleNumber
     * @param zh
     * @param rr
     */
    private void _devideEnfoecerToDeptAssist(int peopleNumber, List<EnforceCertificate> zh, List<List<EnforceCertificate>> rr) {
        while(zh.size()>=peopleNumber){
            List<EnforceCertificate> tt=new ArrayList<>();
            for(int i=0;i<zh.size();i++){
                tt.add(zh.get(i));
                zh.remove(i);
                i--;
                if(tt.size()>=peopleNumber){
                    break;
                }
            }
            rr.add(tt);
        }
    }

    /**
     * 获取每个执法队员的照片
     * 
     * @param resultForReturn
     */
    private void _randomLawTestGetUserPic(List<JSONObject> resultForReturn) {
        List<String> executePersonIdList =
            resultForReturn.stream().map(o -> o.getString("executePersonId")).distinct()
                .collect(Collectors.toList());
        Map<String, String> usersByUserIds = null;
        if (BeanUtil.isNotEmpty(executePersonIdList)) {
            usersByUserIds = adminFeign.getUsersByUserIds(String.join(",", executePersonIdList));
        }
        if (BeanUtil.isEmpty(usersByUserIds)) {
            usersByUserIds = new HashMap<>();
        }

        for (JSONObject tmp : resultForReturn) {
            String executePersonId = tmp.getString("executePersonId");
            if (StringUtils.isNotBlank(executePersonId)) {
                // executePersonId保存了多个人的ID，用逗号隔开
                List<String> attr1List=new ArrayList<>();
                for (String uId : executePersonId.split(",")) {
                    JSONObject userJObj = JSONObject.parseObject(usersByUserIds.get(uId));
                    attr1List.add(String.valueOf(userJObj.get("attr1")));
                }
                tmp.put("attr1", String.join(",", attr1List));
            }

        }
    }

    /**
     * 获取分组执法者
     * 
     * @param peopleNumber
     *            每组人数
     * @param userList
     *            执法者列表
     * @return
     */
    private List<JSONArray> getUserList(int peopleNumber, List<EnforceCertificate> userList) {
        Collections.shuffle(userList);
        List<JSONArray> list = new ArrayList<>();
        int count = 1;
        JSONArray array = null;
        JSONObject json = null;
        EnforceCertificate enforceCertificate = null;
        for (int i = 0; i < userList.size(); i++) {
            enforceCertificate = userList.get(i);
            if (count == 1) {
                array = new JSONArray();
            }
            // 封装执法者json信息串
            StringBuilder userJson = new StringBuilder();
            userJson.append("{\"userName\":\"").append(enforceCertificate.getHolderName()).append("\",")
                .append("\"deptId\":\"").append(enforceCertificate.getDepartId()).append("\"}");

            json = new JSONObject();
            json.put(enforceCertificate.getUsrId(), JSONObject.parseObject(userJson.toString()));
            array.add(json);
            count++;
            if (count > peopleNumber) { // 每 peopleNumber 个为一组
                list.add(array);
                count = 1;
            } else if (count > userList.size()) { // 最后一个直接添加
                list.add(array);
            }
        }
        return list;
    }

    /**
     * 获取分组监管对象
     * 
     * @param regulaObjNumber
     * @param regulaObjects
     * @return
     */
    private List<JSONObject> getRegulaObject(int regulaObjNumber, List<RegulaObject> regulaObjects) {
        Collections.shuffle(regulaObjects);

        List<JSONObject> list = new ArrayList<>();
        int count = 1;
        JSONObject json = null;
        RegulaObject regulaObject = null;
        for (int i = 0; i < regulaObjects.size(); i++) {
            regulaObject = regulaObjects.get(i);
            if (count == 1) {
                json = new JSONObject();
            }
            json.put(regulaObject.getId().toString(), regulaObject.getObjName());
            count++;
            if (count > regulaObjNumber) { // 每 regulaObjNumber 个为一组
                list.add(json);
                count = 1;
            } else if (count > regulaObjects.size()) { // 最后一个直接添加
                list.add(json);
            }
        }
        return list;
    }

    /**
     * 通过id查询详情页
     * 
     * @param id
     * @return
     */
    public JSONObject getById(Integer id) {
        LawTask lawTask = this.selectById(id);
        JSONObject reslutObj = null;
        if (lawTask != null) {
            reslutObj = JSONObject.parseObject(JSONObject.toJSONString(lawTask));
            // 任务状态
            Map<String, String> stateMap = dictFeign.getByCode(reslutObj.getString("state"));
            if (stateMap != null && !stateMap.isEmpty()) {
                reslutObj.put("stateName", stateMap.get(reslutObj.getString("state")));
            }

            // 封装业务条线
            Map<String, String> bizMap = dictFeign.getByCodeIn(reslutObj.getString("bizTypeCode"));
            JSONArray array = new JSONArray();
            Set<String> bizKey = null;
            if (bizMap != null && !bizMap.isEmpty()) {
                bizKey = bizMap.keySet();
                for (String key : bizKey) {
                    JSONObject obj = new JSONObject();
                    obj.put("bizTypeKey", key);
                    obj.put("bizTypeName", bizMap.get(key));
                    array.add(obj);
                }
            }

            // 事件类别
            // "id1,id2;-;id1" - 代表为空，每组ids 已; 区分
            String[] eventTypeIds = reslutObj.getString("eventTypeId").split(";");
            // "id1,id2" "-" "id1"
            List<String> eventTypeid = new ArrayList<>();
            if (eventTypeIds != null && eventTypeIds.length > 0) {
                for (int i = 0; i < eventTypeIds.length; i++) {
                    eventTypeid.add(eventTypeIds[i]);
                }
            }
            // 封装事件名称 "bizTypeKey":"eventTypeName1,eventTypeName2.."
            Map<String, String> eventMap = new HashMap<>();
            if (!eventTypeid.isEmpty()) {
                for (String eventId : eventTypeid) {
                    // 事件类别ids通过in查询数据
                    if (!"-".equals(eventId)) {
                        List<EventType> list = eventTypeBiz.getByEventTypeIds(eventId);
                        String bizType = "";
                        StringBuilder typeName = new StringBuilder();
                        // 封装数据 "code":"name1,name2.."
                        for (int i = 0; i < list.size(); i++) {
                            EventType eventType = list.get(i);
                            if (i == list.size() - 1) {
                                typeName.append(eventType.getTypeName());
                                // biz相同，所以添加一次
                                bizType = eventType.getBizType();
                            } else {
                                typeName.append(eventType.getTypeName()).append(",");
                            }
                        }
                        eventMap.put(bizType, typeName.toString());
                    }
                }
            }

            // 两个集合，数据拼接
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String eventTypeName = eventMap.get(obj.getString("bizTypeKey"));
                obj.put("eventTypeName", eventTypeName == null ? "" : eventTypeName);
                obj.put("bizTypeKey", "");
                array.set(i, obj);
            }
            // 业务条线和事件类别
            reslutObj.put("group", array);
        }
        return reslutObj;
    }

    /**
     * 执法任务翻页查询
     * 
     * @param userName
     *            执法者姓名
     * @param regulaObjectName
     *            巡查对象名称
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @param page
     *            页码
     * @param limit
     *            页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> getLawTaskToDoList(String userName, String regulaObjectName,
        Date startTime, Date endTime, int page, int limit) {

        Page<Object> result = PageHelper.startPage(page, limit);
        // 可发起案件的执法任务状态为进行中的执法任务
        List<Map<String, Object>> list =
            lawTaskMapper.selectLawTaskList(userName, regulaObjectName, propertiesConfig.getLawTasksDoing(), startTime,
                endTime);
        if (list == null) {
            return new TableResultResponse<Map<String, Object>>(0, null);
        }

        Map<String, String> dictData = dictFeign.getByCode(LawTask.ROOT_BIZ_LAWTASKS);
        if (list.size() > 0) {
            // 执法者分队（部门名称）
            for (Map<String, Object> map : list) {
                String executePerson = map.get("executePerson").toString();
                String _executePerson = this.getDeptNameToDo(executePerson);
                map.put("executePerson", _executePerson);
            }
            // 任务状态（数据字典）
            if (dictData != null) {
                String _state = null;
                for (Map<String, Object> map : list) {
                    _state = dictData.get(map.get("state"));
                    map.put("state", _state);
                }
            }
        }

        return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
    }
    
    /**
     * 执法任务翻页查询
     * 
     * @param userName
     *            执法者姓名
     * @param regulaObjectName
     *            巡查对象名称
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @param page
     *            页码
     * @param limit
     *            页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> listSimple(JSONObject queryJObj, int page, int limit) {
        /*
         * 添加按状态查询记录
         * 将请求参数使用JSONObject封装
         */
        String userName = queryJObj.getString("userName");
        String regulaObjectName = queryJObj.getString("regulaObjectName");
        Date startTime = queryJObj.getDate("startTime");
        Date endTime = queryJObj.getDate("endTime");
        String state = queryJObj.getString("state");

        Page<Object> result = PageHelper.startPage(page, limit);
        List<Map<String, Object>> list =
            lawTaskMapper.selectLawTaskList(userName, regulaObjectName, state, startTime,
                endTime);
        if (list == null) {
            return new TableResultResponse<Map<String, Object>>(0, null);
        }

        List<String> statusCodeList =
            list.stream().map(o -> String.valueOf(o.get("state"))).distinct().collect(Collectors.toList());
        Map<String, String> byCodeIn = new HashMap<>();
        if (BeanUtil.isNotEmpty(statusCodeList)) {
            byCodeIn = dictFeign.getByCodeIn(String.join(",", statusCodeList));
        }

        List<Map<String, Object>> resultMapList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", map.get("lawTaskId"));
            resultMap.put("patrolObject", map.get("patrolObject"));
            resultMap.put("lawTitle", map.get("lawTitle"));
            resultMap.put("statusName", byCodeIn.get(map.get("state")));
            resultMap.put("lawTaskCode", map.get("lawTaskCode"));
            resultMapList.add(resultMap);
        }

        return new TableResultResponse<Map<String, Object>>(result.getTotal(), resultMapList);
    }
    
    /**
     * 通过json获取执法分队名称（部门名称）
     * 
     * @param executePerson
     *            执法者信息
     *            [{'userid':{'userName':'xxxx','deptId':'xxxx'}},{'userid':{'userName':'xxx','deptId':'xxxx'}}]
     * @return [{'userid':{'userName':'xxxx','userId':'xxx'}},{'userid':{'userName':'xxx','userId':'xxxx'}}]
     */
    private String getDeptNameToDo(String executePerson) {
        // 执法者json信息
        JSONArray userObjs = JSONArray.parseArray(executePerson);
        JSONArray _userObjs = new JSONArray(); // 封装执法者信息
        JSONObject obj = null; // 执法者
        if (userObjs != null) {
            for (int i = 0; userObjs != null && i < userObjs.size(); i++) {
                JSONObject object = userObjs.getJSONObject(i);
                Set<String> userIds = object.keySet();
                for (String userId : userIds) {
                    String json = object.getString(userId);
                    obj = JSONObject.parseObject(json);
                    
                    JSONObject newJObj=new JSONObject();
                    if (obj != null) {
                        newJObj.put("userName", obj.getString("userName"));
                        newJObj.put("userId", userId);
                        _userObjs.add(newJObj);
                    }
                }
            }
        }
        return _userObjs.toJSONString();
    }
    /**
     * 指定用户（执法队员userId）和指定执法任务状态，获取执法任务列表
     *
     * @param userId 用户id
     * @param state  执法任务状态
     * @param limit  页容量
     * @param page   页码
     * @return
     */
    public TableResultResponse<Map<String, Object>> getLawTaskByUserId(String userId, String state,
                                                                       int limit,int page) {
        List<Map<String, Object>> result = null;
        Page<Object> pageHelper = PageHelper.startPage(page,limit);
        result = lawTaskMapper.selectLawTaskByUserId(userId, state);
        if (BeanUtil.isEmpty(result)) {
            return new TableResultResponse<>(0, result);
        }
        return new TableResultResponse<>(pageHelper.getTotal(), result);
    }
}