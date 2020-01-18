package org.cloud.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_frame_menu
 * @author 
 */
@Data
public class TFrameMenu implements Serializable {
    private Long menuId;

    private String menuCode;

    private String menuName;

    private String menuUrl;

    private Integer seqNo;

    /**
     * 1:有效 0:无效 -1:过期
     */
    private Integer status;

    /**
     * 父级菜单ID
     */
    private Long parentMenuId;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private static final long serialVersionUID = -5306110464323811821L;
}