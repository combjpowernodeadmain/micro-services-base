package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.MessageCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.CLEUrgeRecord;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.mapper.CLEUrgeRecordMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;


/**
 * CleUrgeRecordBiz 案件催办记录表
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月5日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
@Service
public class CLEUrgeRecordBiz extends BusinessBiz<CLEUrgeRecordMapper,CLEUrgeRecord> {
    @Autowired
    private CLEUrgeRecordMapper cleUrgeRecordMapper;

    @Autowired
    private CaseRegistrationBiz caseRegistrationBiz;

    @Autowired
    private MessageCenterBiz messageCenterBiz;
    
    /**
     * 通过立案单id，翻页查询
     * 
     * @param page       页码
     * @param limit      页容量
     * @param cleCaseId  案件id
     * @return
     * 
     */
    public TableResultResponse<CLEUrgeRecord> getList(Integer page, Integer limit, String cleCaseId) {
        Example example = new Example(CLEUrgeRecord.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id DESC");
    
        criteria.andEqualTo("cleCaseId", cleCaseId);

        Page<Object> result = PageHelper.startPage(page, limit);

        List<CLEUrgeRecord> list = cleUrgeRecordMapper.selectByExample(example);
        return new TableResultResponse<CLEUrgeRecord>(result.getTotal(), list);
    }
    
    /**
     * 新增
     */
    public void createSuperviseRecord(CLEUrgeRecord cleUrgeRecord){
        //是否催办（0否| 1是）
        String isUrge = "1";
        //更新事件表督办状态
        String cleCaseId = cleUrgeRecord.getCleCaseId();
        CaseRegistration caseRegistration = caseRegistrationBiz.selectById(cleCaseId);
        if(caseRegistration != null) {
            if(!isUrge.equals(caseRegistration.getIsUrge())){ //没有记录催办时，修改催办状态
                caseRegistration = new CaseRegistration();
                caseRegistration.setId(cleCaseId);
                caseRegistration.setIsUrge(isUrge);
                caseRegistrationBiz.updateSelectiveById(caseRegistration);
            }
            super.insertSelective(cleUrgeRecord);

            /*
             * 添加催办消息
             */
            MessageCenter messageCenter=new MessageCenter();
            messageCenter.setMsgSourceType("case_registration_01");
            messageCenter.setMsgSourceId(String.valueOf(caseRegistration.getId()));
            // 指明按哪个类型进行查询正常的消息记录
            JSONObject sourceJObj=new JSONObject();
            sourceJObj.put("msgSourceType", "case_registration_00");

            messageCenterBiz.addMsgCenterRecord(messageCenter,sourceJObj);
        }
    }
}