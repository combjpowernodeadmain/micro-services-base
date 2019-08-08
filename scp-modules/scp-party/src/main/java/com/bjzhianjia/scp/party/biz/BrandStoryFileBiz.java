package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.party.entity.PartyBranch;
import com.bjzhianjia.scp.party.vo.BrandStoryFileVo;
import com.bjzhianjia.scp.party.vo.PartyBranchVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.BrandStoryFile;
import com.bjzhianjia.scp.party.mapper.BrandStoryFileMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 党建品牌故事附件表
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
@Service
public class BrandStoryFileBiz extends BusinessBiz<BrandStoryFileMapper, BrandStoryFile> {

    /**
     * 添加单个对象
     *
     * @param storyFile
     * @return
     */
    public ObjectRestResponse<Void> addBrandStoryFile(BrandStoryFile storyFile) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        check(storyFile, restResult, false);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        this.insertSelective(storyFile);

        restResult.setMessage("添加文件成功");
        return restResult;
    }

    private void check(BrandStoryFile storyFile, ObjectRestResponse<Void> restResult, boolean b) {
    }

    /**
     * 更新单个对象
     *
     * @param storyFile
     * @return
     */
    public ObjectRestResponse<Void> updateBrandStoryFile(BrandStoryFile storyFile) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        check(storyFile, restResult, true);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        this.updateSelectiveById(storyFile);

        restResult.setMessage("更新文件成功");
        return restResult;
    }

    /**
     * 删除单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<Void> deleteBrandStoryFile(Integer id) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        BrandStoryFile storyFile = new BrandStoryFile();

        storyFile.setId(id);
        storyFile.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);

        this.updateSelectiveById(storyFile);

        restResult.setMessage("删除文件成功");
        return restResult;
    }

    /**
     * 分布获取列表
     *
     * @param branch
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<BrandStoryFileVo> getPartyBranchList(BrandStoryFile branch, Integer page, Integer limit) {
        Example example = new Example(BrandStoryFile.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (BeanUtils.isNotEmpty(branch.getStoryId())) {
            criteria.andEqualTo("storyId", branch.getStoryId());
        }
        if (BeanUtils.isNotEmpty(branch.getFileUploadDate())) {
            criteria.andEqualTo("fileUploadDate", branch.getFileUploadDate());
        }
        if (BeanUtils.isNotEmpty(branch.getFileName())) {
            criteria.andLike("fileName", "%" + branch.getFileName() + "%");
        }
        if (BeanUtils.isNotEmpty(branch.getFileState())) {
            criteria.andEqualTo("fileState", branch.getFileState());
        }

        example.orderBy("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<BrandStoryFile> storyFileList = this.selectByExample(example);

        if (BeanUtils.isEmpty(storyFileList)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        List<BrandStoryFileVo> voList = queryAssist(storyFileList);

        return new TableResultResponse<>(pageInfo.getTotal(), voList);
    }

    private List<BrandStoryFileVo> queryAssist(List<BrandStoryFile> storyFileList) {
        List<BrandStoryFileVo> voList = new ArrayList<>();

        storyFileList.forEach(branch -> {
            BrandStoryFileVo vo = new BrandStoryFileVo();
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
    public ObjectRestResponse<BrandStoryFileVo> getBrandStoryFileById(Integer id) {
        ObjectRestResponse<BrandStoryFileVo> restResult = new ObjectRestResponse<>();

        BrandStoryFile partyBranch = this.selectById(id);

        List<BrandStoryFile> list = new ArrayList<>();
        if (BeanUtils.isNotEmpty(partyBranch)) {
            list.add(partyBranch);
        }

        List<BrandStoryFileVo> storyFileVoList = queryAssist(list);
        if (BeanUtils.isNotEmpty(storyFileVoList)) {
            restResult.setData(storyFileVoList.get(0));
        }

        return restResult;
    }

    /**
     * 按故事ID获取故事附件
     *
     * @param storyId
     * @return
     */
    public List<BrandStoryFile> getByStoryId(Integer storyId) {
        Example example = new Example(BrandStoryFile.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storyId", storyId);

        List<BrandStoryFile> brandStoryFiles = this.selectByExample(example);
        if (BeanUtils.isEmpty(brandStoryFiles)) {
            return new ArrayList<>();
        }

        return brandStoryFiles;
    }
}