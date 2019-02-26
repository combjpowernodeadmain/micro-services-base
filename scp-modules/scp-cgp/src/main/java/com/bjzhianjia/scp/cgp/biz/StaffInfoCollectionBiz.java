package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.entity.StaffInfoCollection;
import com.bjzhianjia.scp.cgp.mapper.StaffInfoCollectionMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2019-02-26 21:39:06
 */
@Service
public class StaffInfoCollectionBiz extends BusinessBiz<StaffInfoCollectionMapper,StaffInfoCollection> {

    /**
     * 人员信息采集
     * @param staffInfoCollection
     * @return
     */
    public ObjectRestResponse<Void> userInfoCollect(StaffInfoCollection staffInfoCollection) {
        ObjectRestResponse<Void> restResult=new ObjectRestResponse<>();

        // 验证手机号格式
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher m = p.matcher(staffInfoCollection.getMibilePhone());
        boolean matches = m.matches();
        if(!matches){
            restResult.setMessage("手机格式错误");
            restResult.setStatus(400);
            return restResult;
        }

        // 验证手机标识号是否存在
        ObjectRestResponse<StaffInfoCollection> staffCheck = checkUserMobilePhont(staffInfoCollection);
        if(staffCheck.getStatus()==400){
            restResult.setStatus(400);
            restResult.setMessage(staffCheck.getMessage());
            return restResult;
        }

        if(staffCheck.getStatus()==-1){
            // 未不存在与user.getMobilePhone对应的手机号
            staffInfoCollection.setCrtTime(new Date());
            this.mapper.insertSelective(staffInfoCollection);
        }else{
            staffInfoCollection.setUpdTime(new Date());
            this.mapper.updateByPrimaryKeySelective(staffInfoCollection);
        }

        restResult.setStatus(200);
        restResult.setMessage("信息采集成功");
        return restResult;
    }

    private ObjectRestResponse<StaffInfoCollection> checkUserMobilePhont(StaffInfoCollection staffInfoCollection) {
        ObjectRestResponse<StaffInfoCollection> restResult=new ObjectRestResponse<>();

        try {
            // 手机标识
            StaffInfoCollection queryStaff=new StaffInfoCollection();
            queryStaff.setPhoneCode(staffInfoCollection.getPhoneCode());
            StaffInfoCollection staffInDB = this.selectOne(queryStaff);
            if(BeanUtils.isNotEmpty(staffInDB)){
                restResult.setData(staffInDB);
                restResult.setStatus(1);
            }else{
                restResult.setStatus(-1);
            }

//            // 手机号
//            queryStaff=new StaffInfoCollection();
//            queryStaff.setMibilePhone(staffInfoCollection.getMibilePhone());
//            staffInDB = this.selectOne(queryStaff);
//            if(BeanUtils.isNotEmpty(staffInDB)){
//                restResult.setMessage("该手机号已添加，请核实。");
//                restResult.setStatus(400);
//                return restResult;
//            }
//
//            // 手机号
//            queryStaff=new StaffInfoCollection();
//            queryStaff.setStaffName(staffInfoCollection.getStaffName());
//            staffInDB = this.selectOne(queryStaff);
//            if(BeanUtils.isNotEmpty(staffInDB)){
//                restResult.setMessage("该姓名已添加，请核实。");
//                restResult.setStatus(400);
//                return restResult;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setMessage("服务器异常，请联系管理员。");
            restResult.setStatus(400);
        }

        return restResult;
    }


    public TableResultResponse<StaffInfoCollection> getStaffInfoCollectionList(StaffInfoCollection info,int page,
                                                                               int limit) {
        Example example=new Example(StaffInfoCollection.class).selectProperties("id","staffName","mibilePhone"
                ,"terminalPhone","departId","phoneCode");
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        if(StringUtils.isNotBlank(info.getStaffName())){
            criteria.andLike("staffName", info.getStaffName());
        }
        if(StringUtils.isNotBlank(info.getMibilePhone())){
            criteria.andLike("mibilePhone", info.getMibilePhone());
        }
        if(StringUtils.isNotBlank(info.getTerminalPhone())){
            criteria.andLike("mibilePhone", info.getMibilePhone());
        }
        if(StringUtils.isNotBlank(info.getDepartId())){
            criteria.andEqualTo("departId", info.getDepartId());
        }
        if(StringUtils.isNotBlank(info.getPhoneCode())){
            criteria.andEqualTo("phoneCode", info.getPhoneCode());
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<StaffInfoCollection> staffInfoCollections = this.selectByExample(example);
        if(BeanUtils.isNotEmpty(staffInfoCollections)){
            return new TableResultResponse<>(pageInfo.getTotal(),staffInfoCollections);
        }
        return new TableResultResponse<>(0,new ArrayList<>());
    }
}