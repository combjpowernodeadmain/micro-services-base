package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.party.entity.PartyMember;
import com.bjzhianjia.scp.party.feign.DictFeign;
import com.bjzhianjia.scp.party.mapper.PartyMemberMapper;
import com.bjzhianjia.scp.party.vo.ExportLogVo;
import com.bjzhianjia.scp.party.vo.PartyMemberVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.ExportLog;
import com.bjzhianjia.scp.party.mapper.ExportLogMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 出境记录表
 *
 * @author bo
 * @version 2019-03-27 21:07:17
 * @email 576866311@qq.com
 */
@Service
public class ExportLogBiz extends BusinessBiz<ExportLogMapper, ExportLog> {

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private PartyMemberMapper partyMemberMapper;

    public TableResultResponse<ExportLogVo> getExportLogList(ExportLog params, Integer page, Integer limit) {
        TableResultResponse<ExportLogVo> tableResult = new TableResultResponse<>();

        Example example = new Example(ExportLog.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (BeanUtils.isNotEmpty(params.getPartyMemId())) {
            criteria.andEqualTo("partyMemId", params.getPartyMemId());
        }

        if (BeanUtils.isNotEmpty(params.getExportDate())) {
            criteria.andEqualTo("exportDate", params.getExportDate());
        }

        if (BeanUtils.isNotEmpty(params.getImportDate())) {
            criteria.andEqualTo("importDate", params.getImportDate());
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<ExportLog> exportLogs = this.selectByExample(example);

        if (BeanUtils.isEmpty(exportLogs)) {
            return tableResult;
        }

        List<ExportLogVo> voList = queryAssist(exportLogs);

        tableResult.getData().setRows(voList);
        tableResult.getData().setTotal(pageInfo.getTotal());
        return tableResult;
    }

    private List<ExportLogVo> queryAssist(List<ExportLog> exportLogs) {
        // 收集字典CODE
        Set<String> dictCodeSet = new HashSet<>();

        List<ExportLogVo> voList = new ArrayList<>();
        Set<Integer> partyMemIds = new HashSet<>();

        exportLogs.forEach(exportLog -> {
            dictCodeSet.add(exportLog.getExeportStatus());
            dictCodeSet.add(exportLog.getImportStatus());
            partyMemIds.add(exportLog.getPartyMemId());

            ExportLogVo vo = new ExportLogVo();
            org.springframework.beans.BeanUtils.copyProperties(exportLog, vo);
            voList.add(vo);
        });

        Map<String, String> byCodeIn = null;
        if (BeanUtils.isNotEmpty(dictCodeSet)) {
            byCodeIn = dictFeign.getByCodeIn(StringUtils.join(dictCodeSet, ","));
        }

        Map<Integer, String> partyMemIdNameMap = null;
        if (BeanUtils.isNotEmpty(partyMemIds)) {
            List<PartyMember> partyMembers = partyMemberMapper.selectByIds(StringUtils.join(partyMemIds, ","));
            if (BeanUtils.isNotEmpty(partyMembers)) {
                partyMemIdNameMap = partyMembers.stream().collect(Collectors.toMap(PartyMember::getId, PartyMember::getName));
            }
        }

        if (BeanUtils.isNotEmpty(byCodeIn)) {
            voList.forEach(vo -> {
                vo.setExportStatusName(vo.getExeportStatus());
                vo.setImportStatusName(vo.getImportStatus());
            });
        }

        if (BeanUtils.isNotEmpty(partyMemIdNameMap)) {
            Map<Integer, String> finalPartyMemIdNameMap = partyMemIdNameMap;
            voList.forEach(vo -> vo.setPartyMemName(finalPartyMemIdNameMap.get(vo.getPartyMemId())));
        }

        return voList;
    }
}