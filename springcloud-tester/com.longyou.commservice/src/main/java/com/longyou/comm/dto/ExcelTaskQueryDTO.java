package com.longyou.comm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import org.cloud.annotation.Query;
import org.cloud.annotation.Query.Type;

@ApiModel("excel任务查询DTO")
@Data
public class ExcelTaskQueryDTO {

    @ApiModelProperty(value = "任务ID")
    private Long id;

    /**
     * 类型：1-导入,2-导出
     */
    @ApiModelProperty(value = "类型：1-导入,2-导出")
    private Integer type;

    /**
     * 状态：0-初始,1-进行中,2-完成,3-失败
     */
    @ApiModelProperty(value = "状态：0-初始,1-进行中,2-完成,3-失败")
    private Integer status;



    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    @Query(type = Type.INNER_LIKE)
    private String fileName;

    /**
     * 成功文件路径
     */
    @ApiModelProperty(value = "成功文件路径")
    @Query(type = Type.INNER_LIKE)
    private String fileUrl;

    /**
     * 失败文件路径
     */
    @ApiModelProperty(value = "失败文件路径")
    @Query(type = Type.INNER_LIKE)
    private String failedFileUrl;

    /**
     * 失败消息
     */
    @ApiModelProperty(value = "失败消息")
    @Query(type = Type.INNER_LIKE)
    private String failedMessage;

    /**
     * 导入开始时间
     */
    @ApiModelProperty(value = "导入开始时间范围")
    @Query(type = Type.UNIX_TIMESTAMP)
    private List<Long> startTime;

    /**
     * 导入结束时间
     */
    @ApiModelProperty(value = "导入结束时间范围")
    @Query(type = Type.UNIX_TIMESTAMP)
    private List<Long> endTime;

//    /**
//     * 租户编码，用于权限控制
//     */
//    private String tenantCode;

    /**
     * 用户编码，用于权限控制
     */
    @ApiModelProperty(value = "用户编码")
    private String createUserCode;

    /**
     * 用户ID，用于权限控制
     */
    @ApiModelProperty(value = "用户ID，用于权限控制")
    private Long createUserId;

    /**
     * 业务编码 例如user,product,用于区分不同模块的导入
     */
    @ApiModelProperty(value = "businessCode")
    private String businessCode;
}
