package org.cloud.model;

import java.io.Serializable;
import lombok.Data;

/**
 * t_frame_role_menu
 * @author 
 */
@Data
public class TFrameRoleMenu implements Serializable {
    /**
     * 系统角色id
     */
    private Long roleMenuId;

    /**
     * 角色id
     */
    private Integer roleId;

    private Long menuId;

    private String roleCode;

    private TFrameMenu frameMenu ;

    private static final long serialVersionUID = -4232949552934901485L;
}