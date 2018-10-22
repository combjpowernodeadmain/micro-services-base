package com.bjzhianjia.scp.security.admin.biz;

import com.bjzhianjia.scp.security.admin.entity.BaseAreainfo;
import com.bjzhianjia.scp.security.admin.mapper.BaseAreainfoMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * BaseAreainfoBiz 区县行政编码字典表.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年10月12日          bo      chenshuai            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@Service
public class BaseAreainfoBiz extends BusinessBiz<BaseAreainfoMapper, BaseAreainfo> {

    /**
     * 通过区域id和等级获取子区域列表
     *
     * @param id    区域id
     * @param level 区域等级
     * @return
     */
    public List<BaseAreainfo> getByParentIdAndLevel(Integer id, Integer level) {
        BaseAreainfo baseAreainfo = new BaseAreainfo();
        baseAreainfo.setArealevel(level);
        baseAreainfo.setParentId(id);
        List<BaseAreainfo> list = this.selectList(baseAreainfo);
        return list == null ? new ArrayList<>() : list;
    }

    /**
     * 通过ids获取名称
     *
     * @param ids 区域id集
     * @return
     */
    public Map<String, String> getNameByIds(List ids) {
        if (ids == null) {
            return new HashMap<String, String>();
        }

        Example example = new Example(BaseAreainfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);

        List<BaseAreainfo> list = this.selectByExample(example);
        Map<String, String> resultData = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (BaseAreainfo baseAreainfo : list) {
                resultData.put(baseAreainfo.getId().toString(), baseAreainfo.getName());
            }
        }
        return resultData;
    }

    /**
     * 通过区域id，获取区域子集
     *
     * @return
     */
    public Map<String, String> getSonNameById(Integer id) {
        Example example = new Example(BaseAreainfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", id);
        List<BaseAreainfo> list = this.selectByExample(example);
        Map<String, String> resultData = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (BaseAreainfo baseAreainfo : list) {
                resultData.put(baseAreainfo.getId().toString(), baseAreainfo.getName());
            }
        }
        return resultData;
    }
}