package org.cloud.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.cloud.validator.GroupForAdd;
import org.cloud.validator.GroupForUpdate;
import org.hibernate.validator.constraints.Range;

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
    @NotNull(groups = {GroupForAdd.class})
    private Long dataAuthListId;

    /**
    * 10:角色 20:用户数据权限
    */
    @ApiModelProperty(value="10:角色 20:用户数据权限")
    @NotNull(message = "data.dimension.type.notnull",groups = {GroupForAdd.class, GroupForUpdate.class})
    private Integer dataDimensionType;

    /**
    * 根据权限类型来关系不用的ID
    */
    @ApiModelProperty(value="根据权限类型来关系不用的ID")
    @NotNull(message = "data.dimension.referId.notnull",groups = {GroupForAdd.class, GroupForUpdate.class})
    private Long referId;

    /**
    * 权限维度:： org（组织机构） /  micro 微服务
    */
    @ApiModelProperty(value="权限维度:： org（组织机构） /  micro 微服务")
    @NotBlank(message = "data.dimension.notnull",groups = {GroupForAdd.class, GroupForUpdate.class})
    private String dataDimension;

    /**
    * 权限值
    */
    @ApiModelProperty(value="权限值")
    @NotBlank(message = "data.dimension.value.notnull",groups = {GroupForAdd.class, GroupForUpdate.class})
    private String dataDimensionValue;

    /**
    * 状态，1/有效 0/无效 -1/过期
    */
    @ApiModelProperty(value="状态，1/有效 0/无效 -1/过期")
    @Range(max = 1,min = -1,groups = {GroupForAdd.class, GroupForUpdate.class})
    private Integer status;

    @ApiModelProperty(value="")
//    @NotBlank(message = "data.dimension.createBy.notnull",groups = {GroupForAdd.class, GroupForUpdate.class})
    private String createBy;

    @ApiModelProperty(value="")
    private Date createDate;

    @ApiModelProperty(value="")
//    @NotBlank(message = "data.dimension.updateBy.notnull",groups = {GroupForAdd.class, GroupForUpdate.class})
    private String updateBy;

    @ApiModelProperty(value="")
    private Date updateDate;
}