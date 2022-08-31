package com.unknow.first.article.manager.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "t_article")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_article")
public class Article {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键id",example = "1")
    private Integer id;

    /**
     * 类型（1分类；2文章）
     */
    @TableField(value = "node_type")
    @ApiModelProperty(value = "类型（1分类；2文章）",example = "1")
    private Integer nodeType;

    /**
     * 分类时语言类型（1中文，2英文）
     */
    @TableField(value = "language_type")
    @ApiModelProperty(value = "分类时语言类型（1中文，2英文）",example = "1")
    private Integer languageType;

    /**
     * 所属分类id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "所属分类id",example = "0")
    private Integer parentId;

    /**
     * 分类编码（当前为分类时不能为空）
     */
    @TableField(value = "class_code")
    @ApiModelProperty(value = "分类编码（当前为分类时不能为空）")
    private String classCode;

    /**
     * 排序号（越大越靠前）
     */
    @TableField(value = "article_sort")
    @ApiModelProperty(value = "排序号（越大越靠前）",example = "1")
    private Integer articleSort;

    /**
     * 标题(分类名或文章标题)
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "标题(分类名或文章标题)")
    private String title;

    /**
     * 副标题
     */
    @TableField(value = "sub_title")
    @ApiModelProperty(value = "副标题")
    private String subTitle;

    /**
     * 文章封面图
     */
    @TableField(value = "image")
    @ApiModelProperty(value = "文章封面图")
    private String image;

    /**
     * 文章内容
     */
    @TableField(value = "article_content")
    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    /**
     * 文章作者
     */
    @TableField(value = "article_author")
    @ApiModelProperty(value = "文章作者")
    private String articleAuthor;

    /**
     * 状态（1正常；0禁用）
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "状态（1正常；0禁用）",example = "1")
    private Integer status;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 修改人
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    public static final String COL_ID = "id";

    public static final String COL_NODE_TYPE = "node_type";

    public static final String COL_LANGUAGE_TYPE = "language_type";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_CLASS_CODE = "class_code";

    public static final String COL_ARTICLE_SORT = "article_sort";

    public static final String COL_TITLE = "title";

    public static final String COL_SUB_TITLE = "sub_title";

    public static final String COL_IMAGE = "image";

    public static final String COL_ARTICLE_CONTENT = "article_content";

    public static final String COL_ARTICLE_AUTHOR = "article_author";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}