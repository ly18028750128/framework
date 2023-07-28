package com.unknow.first.imexport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cloud.mybatisplus.annotation.Query;
import org.cloud.mybatisplus.annotation.Query.Type;

@ApiModel("导出模板查询DTO")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FrameExportTemplateQueryDTO {

    /**
     * 模板编码
     */
    @ApiModelProperty("模板编码")
    private String templateCode;

    /**
     * 模板类型：10(excel),20(pdf),30(word)
     */
    @ApiModelProperty("模板类型：10(excel),20(pdf),30(word)")
    private Integer templateType;

    /**
     * 文件ID，存储在mongodb里的文件ObjectId
     */
    @ApiModelProperty("文件ID，存储在mongodb里的文件ObjectId")
    private String fileId;

    /**
     * 状态，1/有效 0/无效
     */
    @ApiModelProperty("状态，1/有效 0/无效")
    private Integer status;


    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    @Query(type = Type.INNER_LIKE)
    private String createByName;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    @Query(type = Type.LONG_TIMESTAMP)
    private List<Long> createDate;


    /**
     * 创建人名称
     */
    @ApiModelProperty("更新日期")
    @Query(type = Type.INNER_LIKE)
    private String updateByName;

    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")
    @Query(type = Type.LONG_TIMESTAMP)
    private List<Long> updateDate;
}
