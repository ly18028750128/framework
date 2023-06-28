/*
 * Copyright (C) 2019-2022 All rights reserved, Designed By www.lianziyou.com
 */

package com.unknow.first.mongo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel("mongodb文件查询参数DTO")
public class MongoGridFsQueryDTO {


    @ApiModelProperty("ObjectId")
    private String _id;
    @ApiModelProperty("文件名称（支持正则）")
    private String filename;
    @ApiModelProperty("文件大小范围最小值")
    private Long minSize;
    @ApiModelProperty("文件大小范围最大值")
    private Long maxSize;
    @ApiModelProperty("上传日期")
    private List<Date> uploadDate;
    @ApiModelProperty("文件元数据")
    private MetadataDTO metadata = new MetadataDTO();
}
