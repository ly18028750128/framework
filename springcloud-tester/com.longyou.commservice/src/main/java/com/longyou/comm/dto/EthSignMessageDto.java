package com.longyou.comm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("签名登录DTO")
@Data
public class EthSignMessageDto {
    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("登录网址")
    private String webUrl;

    @ApiModelProperty("登录时间戳，单位ms")
    private Long loginTime;

    @ApiModelProperty("信息")
    private String message;





}
