package com.bjzhianjia.scp.security.admin.biz;

import com.bjzhianjia.scp.security.admin.entity.BaseAreainfo;
import com.bjzhianjia.scp.security.admin.mapper.BaseAreainfoMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}