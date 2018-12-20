package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.mapper.EnforceCertificateMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执法证管理
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Service
public class EnforceCertificateBiz extends BusinessBiz<EnforceCertificateMapper, EnforceCertificate> {

    @Autowired
    private EnforceCertificateMapper enforceCertificateMapper;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private LawEnforcePathBiz lawEnforcePathBiz;

    /**
     * 根据证件编号获取
     * 
     * @param certCode
     *            证件编号
     * @return
     */
    public EnforceCertificate getByCertCode(String certCode) {

        Example example = new Example(EnforceCertificate.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("certCode", certCode);

        List<EnforceCertificate> list = enforceCertificateMapper.selectByExample(example);

        if (list == null || list.size() == 0) {
            return null;
        }

        EnforceCertificate enforceCertificate = list.get(0);

        if (enforceCertificate.getIsDeleted().equals("1")) {
            return null;
        }

        return enforceCertificate;
    }

    /**
     * 根据持证人获取
     * 
     * @param holderName
     *            持证人
     * @return
     */
    public EnforceCertificate getByHolderName(String holderName) {
        Example example = new Example(EnforceCertificate.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("holderName", holderName);

        List<EnforceCertificate> list = enforceCertificateMapper.selectByExample(example);

        if (list == null || list.size() == 0) {
            return null;
        }

        EnforceCertificate enforceCertificate = list.get(0);

        if (enforceCertificate.getIsDeleted().equals("1")) {
            return null;
        }

        return enforceCertificate;
    }

    /**
     * 根据查询条件搜索
     * 
     * @param eventType
     * @return
     */
    public TableResultResponse<EnforceCertificate> getList(int page, int limit, EnforceCertificate enforceCertificate,
        String deptId) {
        Example example = new Example(EnforceCertificate.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(enforceCertificate.getHolderName())) {
            criteria.andLike("holderName", "%" + enforceCertificate.getHolderName() + "%");
        }
        if (StringUtils.isNotBlank(enforceCertificate.getCertCode())) {
            criteria.andLike("certCode", "%" + enforceCertificate.getCertCode() + "%");
        }
        if (StringUtils.isNotBlank(deptId)) {
            JSONArray feignDepartUsers = adminFeign.getFeignDepartUsers(deptId, null);
            List<String> userIdList = new ArrayList<>();
            if (BeanUtil.isNotEmpty(feignDepartUsers)) {
                for (int i = 0; i < feignDepartUsers.size(); i++) {
                    JSONObject jsonObject = feignDepartUsers.getJSONObject(i);
                    userIdList.add(jsonObject.getString("id"));
                }
            }
            criteria.andIn("usrId", userIdList);
        }

        example.setOrderByClause("id desc");

        Page<Object> result = PageHelper.startPage(page, limit);
        List<EnforceCertificate> list = enforceCertificateMapper.selectByExample(example);
        return new TableResultResponse<EnforceCertificate>(result.getTotal(), list);
    }

    /**
     * 根据编号获取终端
     * 
     * @param id
     *            编号
     * @return
     */
    public EnforceCertificate getById(Integer id) {

        EnforceCertificate enforceCertificate = enforceCertificateMapper.selectByPrimaryKey(id);

        if (enforceCertificate != null && enforceCertificate.getIsDeleted().equals("1")) {
            return null;
        }

        return enforceCertificate;
    }

    /**
     * 批量删除
     * 
     * @param ids
     *            id列表
     */
    public void deleteByIds(Integer[] ids) {
        enforceCertificateMapper.deleteByIds(ids, BaseContextHandler.getUserID(), BaseContextHandler.getName(),
            new Date());
    }

    /**
     * 获取所有执法者用户
     * 
     * @return
     */
    public List<EnforceCertificate> getEnforceCertificateList() {
        List<EnforceCertificate> list = enforceCertificateMapper.selectAllUserInfo();
        return list;
    }

    /**
     * 通过用户id查询执法证管理者
     * 
     * @param userId
     *            用户接入
     * @return
     */
    public EnforceCertificate getEnforceCertificateByUserId(String userId) {
        EnforceCertificate enforceCertificate = new EnforceCertificate();
        enforceCertificate.setUsrId(userId);
        enforceCertificate = enforceCertificateMapper.selectOne(enforceCertificate);
        return enforceCertificate;
    }

    /**
     * 执法人员全部定位
     * 
     * @return
     */
    public TableResultResponse<JSONObject> allPosition() {
        List<String> userIdList = this.mapper.distinctUsrId();

        Map<String, JSONObject> lawMap = new HashMap<>();
        if (userIdList != null && userIdList.size() > 0) {
            lawMap = lawEnforcePathBiz.getByUserIds(String.join(",", userIdList));
        }

        // usrId={"telPhone":"17600334757","id":"9aa5ffd9b2584f87a54ff73e4c279b59"}
        // 整合与前端目前解析的数据结构相同
        JSONArray resultJArray = new JSONArray();
        for (String usrId : userIdList) {
            JSONObject usrIdJObjOut = new JSONObject();
            JSONObject usrIdJObjIn = new JSONObject();
            usrIdJObjIn.put("id", usrId);
            usrIdJObjOut.put("usrId", usrIdJObjIn.toJSONString());
            resultJArray.add(usrIdJObjOut);
        }

        List<JSONObject> resultJObjList = new ArrayList<>();
        for (int i = 0; i < resultJArray.size(); i++) {
            JSONObject jsonObject = resultJArray.getJSONObject(i);
            if (lawMap != null && !lawMap.isEmpty()) {
                jsonObject.put("mapInfo",
                    lawMap.get(jsonObject.getJSONObject("usrId").getString("id")));
            }
            resultJObjList.add(jsonObject);
        }

        return new TableResultResponse<>(resultJObjList.size(), resultJObjList);
    }

    /**
     * 获取指定用户ids集的执法执法证
     *
     * @param userId 用户ids
     * @return
     */
    public List<EnforceCertificate> getEnforceByUserIds(List<String> userId) {
        if(BeanUtils.isEmpty(userId)){
            return new ArrayList<>();
        }
        Example example = new Example(EnforceCertificate.class).selectProperties("id", "usrId", "certCode");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        // criteria.andEqualTo("isDeleted", "0");
        criteria.andIn("usrId", userId);
        List<EnforceCertificate> result = this.mapper.selectByExample(example);
        return BeanUtils.isEmpty(result) ? new ArrayList<>() : result;
    }
}