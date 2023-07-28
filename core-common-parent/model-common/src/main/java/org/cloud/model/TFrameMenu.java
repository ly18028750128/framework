package org.cloud.model;

import java.util.Date;
import lombok.Data;

/**
 * 系统菜单
 */
@Data
public class TFrameMenu {
    private Long menuId;

    private String menuCode;

    private String menuName;

    private String menuUrl;

    /**
     * vue组件位置
     */
    private String componentPath;

    /**
     * 0：目录 1：菜单
     */
    private Integer type;

    /**
     * 0：有子菜单时显示 1：有权限时显示 2：任何时候都显示
     */
    private Integer showType;

    /**
     * 显示图标
     */
    private String icon;

    /**
     * 权限编码：微服务application.name::resource_path::resource_name 例如：common-service::系统管理/数据字典::刷新缓存
     */
    private String functionResourceCode;

    /**
     * 1:有效 0:无效 -1:过期
     */
    private Integer status;

    private Integer seqNo;

    /**
     * 父级菜单ID
     */
    private Long parentMenuId;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    /**
     * 路由meta参数，必须为json字符串
     */
    private String metaData;
}