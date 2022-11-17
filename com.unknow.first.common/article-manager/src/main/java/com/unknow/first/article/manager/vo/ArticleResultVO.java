package com.unknow.first.article.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class ArticleResultVO {
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "所属分类id")
    private Integer parentId;

    @ApiModelProperty(value = "语言类型（1中文，2英文）")
    private Integer languageType;

    @ApiModelProperty(value = "分类编码（当前为分类时不能为空）")
    private String classCode;

    @ApiModelProperty(value = "排序号（越大越靠前）")
    private Integer articleSort;

    @ApiModelProperty(value = "标题(分类名或文章标题)")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "文章封面图")
    private String image;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

}
