package org.cloud.model;

import java.io.Serializable;
import lombok.Data;

/**
 * t_frame_role_resource
 * @author 
 */
@Data
public class TFrameRoleResource implements Serializable {
    /**
     * 角色菜单资源ID
     */
    private Long roleResourceId;

    /**
     * 角色id
     */
    private Integer roleId;

    private Long resourceId;

    private TFrameworkResource frameworkResource;

    private static final long serialVersionUID = -7839174763276951281L;
}