package org.cloud.model;

import java.io.Serializable;
import lombok.Data;

/**
 * t_frame_role_data_interface
 * @author 
 */
@Data
public class TFrameRoleDataInterface implements Serializable {
    private Long id;

    /**
     * 角色id
     */
    private Integer roleId;

    private String dataInterfaceId;
    private Boolean deleted = false;


    private String belongMicroservice;
    private String dataInterfaceName;


    private static final long serialVersionUID = -900000518015051162L;
}