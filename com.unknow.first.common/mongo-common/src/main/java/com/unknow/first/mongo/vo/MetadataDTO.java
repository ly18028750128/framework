package com.unknow.first.mongo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel("mongodb文件元数据")
public class MetadataDTO {

    @ApiModelProperty("文件类型（支持正则）")
    private String contentType;
    @ApiModelProperty("文件所有者ID")
    private Long owner;
    @ApiModelProperty("文件所有者")
    private String ownerName;
    @ApiModelProperty("文件所有者全称")
    private String ownerFullName;
    @ApiModelProperty("文件后缀（支持正则）")
    private String suffix;
    @ApiModelProperty("文件备注（支持正则）")
    private String remark;
    @ApiModelProperty("文件标签（支持正则）")
    private String tag;
    @ApiModelProperty(value = "文件拥有权多选")
    private List<String> fileAuthRangeList;
    @ApiModelProperty(value = "文件拥有权")
    private String fileAuthRange;

}