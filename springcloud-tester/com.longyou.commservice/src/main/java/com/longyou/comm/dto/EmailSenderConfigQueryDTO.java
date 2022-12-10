package com.longyou.comm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2022-12-10
 */
@Data
@ApiModel(value = "EmailSenderConfig查询对象", description = "")
public class EmailSenderConfigQueryDTO {


    private Integer emailSenderId;

    @ApiModelProperty(value = "邮箱用户名")
    private String userName;

    @ApiModelProperty(value = "状态")
    private Integer status;


}
