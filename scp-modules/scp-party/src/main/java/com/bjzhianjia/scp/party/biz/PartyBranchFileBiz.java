package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.party.vo.PartyBranchFileVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.PartyBranchFile;
import com.bjzhianjia.scp.party.mapper.PartyBranchFileMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 党建品牌附件表
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
@Service
public class PartyBranchFileBiz extends BusinessBiz<PartyBranchFileMapper, PartyBranchFile> {

    /**
     * 添加单个对象
     *
     * @param brandFile
     * @return
     */
    public ObjectRestResponse<Void> addBranchFile(PartyBranchFile brandFile) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        check(brandFile, restResult, false);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        this.insertSelective(brandFile);

        restResult.setMessage("添加党建文件成功");
        return restResult;
    }

    private void check(PartyBranchFile brandFile, ObjectRestResponse<Void> restResult, boolean b) {
    }

    /**
     * 更新单个对象
     *
     * @param brandFile
     * @return
     */
    public ObjectRestResponse<Void> updateBranchFile(PartyBranchFile brandFile) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        check(brandFile, restResult, true);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        this.updateSelectiveById(brandFile);

        restResult.setMessage("更新党建文件成功");
        return restResult;
    }

    /**
     * 删除单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<Void> deleteBranchFile(Integer id) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        PartyBranchFile partyBranch = new PartyBranchFile();

        partyBranch.setId(id);
        partyBranch.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);

        this.updateSelectiveById(partyBranch);

        restResult.setMessage("删除党建品牌文件成功");
        return restResult;
    }

    /**
     * 分页获取列表
     *
     * @param branch
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<PartyBranchFileVo> getPartyBranchFileList(PartyBranchFile branch, Integer page, Integer limit) {
        Example example = new Example(PartyBranchFile.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (BeanUtils.isNotEmpty(branch.getBranchId())) {
            criteria.andEqualTo("branchId", branch.getBranchId());
        }
        if (BeanUtils.isNotEmpty(branch.getFileUploadDate())) {
            criteria.andEqualTo("fileUploadDate", branch.getFileUploadDate());
        }
        if (BeanUtils.isNotEmpty(branch.getFileName())) {
            criteria.andLike("fileName", "%" + branch.getFileName() + "%");
        }
        if (BeanUtils.isNotEmpty(branch.getFileStatus())) {
            criteria.andEqualTo("fileStatus", branch.getFileStatus());
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<PartyBranchFile> branchFiles = this.selectByExample(example);

        if (BeanUtils.isEmpty(branchFiles)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        List<PartyBranchFileVo> voList = queryAssist(branchFiles);

        return new TableResultResponse<>(pageInfo.getTotal(), voList);
    }

    private List<PartyBranchFileVo> queryAssist(List<PartyBranchFile> branchFiles) {
        List<PartyBranchFileVo> voList = new ArrayList<>();

        branchFiles.forEach(branch -> {
            PartyBranchFileVo vo = new PartyBranchFileVo();
            org.springframework.beans.BeanUtils.copyProperties(branch, vo);
            voList.add(vo);
        });

        return voList;
    }

    /**
     * 获取单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<PartyBranchFileVo> getPartyBranchFileById(Integer id) {
        ObjectRestResponse<PartyBranchFileVo> restResult = new ObjectRestResponse<>();

        PartyBranchFile partyBranch = this.selectById(id);

        List<PartyBranchFile> list = new ArrayList<>();
        if (BeanUtils.isNotEmpty(partyBranch)) {
            list.add(partyBranch);
        }

        List<PartyBranchFileVo> storyFileVoList = queryAssist(list);
        if (BeanUtils.isNotEmpty(storyFileVoList)) {
            restResult.setData(storyFileVoList.get(0));
        }

        return restResult;
    }

    /**
     * 按品牌获取附件
     *
     * @param brandId
     * @return
     */
    public List<PartyBranchFile> getpartyBrandFileByBrandId(Integer brandId) {
        Example example = new Example(PartyBranchFile.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("branchId", brandId);

        List<PartyBranchFile> partyBranchFiles = this.selectByExample(example);
        if (BeanUtils.isEmpty(partyBranchFiles)) {
            return new ArrayList<>();
        }

        return partyBranchFiles;
    }
}