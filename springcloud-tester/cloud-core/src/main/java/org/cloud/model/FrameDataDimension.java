package org.cloud.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 数据权限列表
角色类型为功能权限之外的权限统一为功能权限
    */
@ApiModel(value="com-longyou-comm-model-FrameDataDimension")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrameDataDimension {
    @ApiModelProperty(value="")
    private Long dataAuthListId;

    /**
    * 10:角色 20:用户数据权限
    */
    @ApiModelProperty(value="10:角色 20:用户数据权限")
    private Integer dataDimensionType;

    /**
    * 根据权限类型来关系不用的ID
    */
    @ApiModelProperty(value="根据权限类型来关系不用的ID")
    private Long referId;

    /**
    * 权限维度:： org（组织机构） /  micro 微服务
    */
    @ApiModelProperty(value="权限维度:： org（组织机构） /  micro 微服务")
    private String dataDimension;

    /**
    * 权限值
    */
    @ApiModelProperty(value="权限值")
    private String dataDimensionValue;

    /**
    * 状态，1/有效 0/无效 -1/过期
    */
    @ApiModelProperty(value="状态，1/有效 0/无效 -1/过期")
    private Integer status;

    @ApiModelProperty(value="")
    private String createBy;

    @ApiModelProperty(value="")
    private Date createDate;

    @ApiModelProperty(value="")
    private String updateBy;

    @ApiModelProperty(value="")
    private Date updateDate;
}