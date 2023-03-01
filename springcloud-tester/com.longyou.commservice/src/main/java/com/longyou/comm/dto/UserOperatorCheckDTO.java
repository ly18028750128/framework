package com.longyou.comm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("用户操作权限校验结果")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOperatorCheckDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("权限点权限结果")
    private Map<String,Boolean> checkResult=new HashMap<>();

}
