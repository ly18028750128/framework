package com.longyou.aihelper.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="com-longyou-aihelper-model-Responsetable")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Responsetable {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="")
    private Date createtime;

    @ApiModelProperty(value="")
    private Integer isdeleted;

    @ApiModelProperty(value="")
    private Date lastmodifytime;

    @ApiModelProperty(value="")
    private String question;

    @ApiModelProperty(value="")
    private String replay;

    @ApiModelProperty(value="")
    private String label;

    @ApiModelProperty(value="")
    private String copyfield;
}