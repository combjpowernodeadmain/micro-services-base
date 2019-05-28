package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.PatrolRecord;
import lombok.Data;

/**
 * @author By 尚
 * @date 2019/5/26 17:51
 */
@Data
public class PatrolRecordVo extends PatrolRecord {

    // 巡查状态名称
    private String patrolStatusName;
    // 巡查时长
    private String patrolTimeLength;

}
