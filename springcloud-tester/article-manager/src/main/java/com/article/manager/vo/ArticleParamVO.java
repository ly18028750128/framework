package com.article.manager.vo;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArticleParamVO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @NotNull
    @ApiModelProperty(value = "类型（1分类；2文章）")
    private Integer nodeType;

    @ApiModelProperty(value = "分类时语言类型（1中文，2英文）")
    private Integer languageType;

    @ApiModelProperty(value = "所属分类id(仅创建文章时必填)")
    private Integer parentId;

    @ApiModelProperty(value = "分类编码(仅创建分类时必填)")
    private String classCode;

    @NotNull
    @ApiModelProperty(value = "排序号（越大越靠前）")
    private Integer articleSort;

    @NotBlank
    @ApiModelProperty(value = "标题(分类名或文章标题)")
    private String title;

    @ApiModelProperty(value = "副标题(仅创建文章时必填)")
    private String subTitle;

    @ApiModelProperty(value = "文章封面图(公告可不必填)")
    private String image;

    @ApiModelProperty(value = "文章作者（非必填）")
    private String articleAuthor;

    @NotNull
    @ApiModelProperty(value = "状态（1正常；0禁用）")
    private Integer status;

    @ApiModelProperty(value = "文章内容(仅创建文章时必填)")
    private String articleContent;

}
