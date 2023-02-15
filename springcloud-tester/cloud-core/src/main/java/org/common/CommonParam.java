package org.common;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

@Data
public class CommonParam implements Serializable {

    @ApiModelProperty(value = "关键字", example = "名称")
    private String search;
    @ApiModelProperty(value = "页数", required = true, example = "1")
    @Min(value = 1, message = "不能小于1")
    private Integer page = 1;
    @ApiModelProperty(value = "条数", required = true, example = "10")
    private Integer limit;

    {
        limit = 10;
    }

    /**
     * 获取起始行数
     *
     * @return
     */
    @ApiIgnore
    public Integer getStart() {
        return (page - 1) * limit;
    }

    @ApiModelProperty(value = "排序参数（多个用,号分开）", example = "id asc")
    private String sorts;

}
