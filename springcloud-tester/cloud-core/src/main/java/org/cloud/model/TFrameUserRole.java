package org.cloud.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.cloud.deserializer.IsoDateJsonDateDeserializer;

import java.io.Serializable;
import java.util.Date;

/**
 * t_frame_user_role
 *
 * @author
 */
@Data
public class TFrameUserRole implements Serializable {
    private Long userRoleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 有效开始时间
     */

    private Date validatorStart;
    /**
     * 有效结束时间
     */
    private Date validatorEnd;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    @JsonDeserialize(using = IsoDateJsonDateDeserializer.class)
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新日期
     */
    @JsonDeserialize(using = IsoDateJsonDateDeserializer.class)
    private Date updateDate;

    private TFrameRole frameRole;

    private static final long serialVersionUID = -5944841549418566952L;
}