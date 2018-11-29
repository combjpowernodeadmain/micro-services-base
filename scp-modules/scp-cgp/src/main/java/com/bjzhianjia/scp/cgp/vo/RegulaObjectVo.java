package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;

/**
 * 
 * @author 尚
 *
 */
public class RegulaObjectVo extends RegulaObject {

    /**
     * 
     */
    private static final long serialVersionUID = 6624378212384321901L;

    private String objTypeName;

    private Integer patrolCount;// 该监管对象被巡查的次数
    private Integer pCountWithProblem;// 该监管对象被巡查出现问题的次数

    private EnterpriseInfo enterpriseInfo;

    public String getObjTypeName() {
        return objTypeName;
    }

    public void setObjTypeName(String objTypeName) {
        this.objTypeName = objTypeName;
    }

    public EnterpriseInfo getEnterpriseInfo() {
        return enterpriseInfo;
    }

    public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        this.enterpriseInfo = enterpriseInfo;
    }

    public Integer getPatrolCount() {
        return patrolCount;
    }

    public void setPatrolCount(Integer patrolCount) {
        this.patrolCount = patrolCount;
    }

    public Integer getpCountWithProblem() {
        return pCountWithProblem;
    }

    public void setpCountWithProblem(Integer pCountWithProblem) {
        this.pCountWithProblem = pCountWithProblem;
    }
}
