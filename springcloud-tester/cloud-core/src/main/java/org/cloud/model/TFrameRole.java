package org.cloud.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * t_frame_role
 * @author 
 */
@Data
public class TFrameRole implements Serializable {
    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色类型，  function(功能角色)、data(数据权限)
     */
    private String roleType;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    private Integer status=1;

    /**
     * 更新日期
     */
    private Date updateDate;

//    @JsonIgnore
    Boolean frameRoleResourceUpdateFlag = false;

    private List<TFrameRoleResource> frameRoleResourceList;

    private List<TFrameRoleData> frameRoleDataList;

    private List<TFrameRoleMenu> frameRoleMenuList;

//    @JsonIgnore
    Boolean frameRoleDataInterfaceUpdateFlag = false;

    private List<TFrameRoleDataInterface> frameRoleDataInterfaceList;

    private static final long serialVersionUID = 8044423583553999672L;


}