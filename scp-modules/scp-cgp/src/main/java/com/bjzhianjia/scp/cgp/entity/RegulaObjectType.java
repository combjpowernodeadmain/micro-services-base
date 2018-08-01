
package com.bjzhianjia.scp.cgp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by mengfanguang  on 2018-07-31 14:19:12
 */
@ApiModel("监管对象类型表")
@Table(name = "regula_object_type")
@Data
public class RegulaObjectType {

    @Id
	@OrderBy("desc")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private java.lang.Integer id;

    @ApiModelProperty(value = "监管对象类型编码", required = true)
    private java.lang.String objectTypeCode;

    @ApiModelProperty(value = "监管对象类型名称", required = true)
    private java.lang.String objectTypeName;

    @ApiModelProperty(value = "所属监管对象类型")
    private java.lang.String parentObjectTypeId;

    @ApiModelProperty(value = "是否可用；关联数据字典")
    private java.lang.String isEnable;

    @ApiModelProperty(value = "是否删除；1：是；0: 否", required = true)
    private java.lang.String isDeleted;

    @ApiModelProperty(value = "创建人")
    private java.lang.String crtUserName;

    @ApiModelProperty(value = "创建人ID")
    private java.lang.String crtUserId;

    @ApiModelProperty(value = "创建时间")
    private java.time.LocalDateTime crtTime;

    @ApiModelProperty(value = "最后更新人")
    private java.lang.String updUserName;

    @ApiModelProperty(value = "最后更新人ID")
    private java.lang.String updUserId;

    @ApiModelProperty(value = "最后更新时间")
    private java.time.LocalDateTime updTime;

    @ApiModelProperty(value = "租户ID")
    private java.lang.String tenantId;

    @ApiModelProperty(value = "部门ID")
    private java.lang.String departId;

}

