package org.cloud.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_frame_role_data
 * @author 
 */
@Data
public class TFrameRoleData implements Serializable {
    private Integer dataAuthListId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限维度:： org（组织机构） /  micro 微服务
     */
    private String dataDimension;

    /**
     * 权限值
     */
    private String dataDimensionValue;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private static final long serialVersionUID = -5595578242036542445L;
}