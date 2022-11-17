package com.unkow.first.photo.manager.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author wjl
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TPhoto对象", description = "")
@JsonInclude(Include.NON_NULL)
public class TPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "启动页-LAUNCH_PAGE；首页-BANNER")
    private String photoType;

    @ApiModelProperty(value = "排序号（越大越靠前）")
    private Integer sort;

    @ApiModelProperty(value = "图片资源地址")
    private String imageUrl;

    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;

    @ApiModelProperty(value = "状态（1正常；0禁用）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "图片说明")
    private String photoDesc;

    @ApiModelProperty(value = "语言类型")
    private Integer languageType;
}
