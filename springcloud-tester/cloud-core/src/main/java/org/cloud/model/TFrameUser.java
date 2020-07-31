package org.cloud.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * t_frame_user
 *
 * @author
 */
@Data
public class TFrameUser implements Serializable {
    private Long id;

    private String userName;

    private String password;

    private String email;

    private String fullName;

    private String sex;

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

    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 1:有效 0:无效 -1:过期
     */
    private Integer status;

    /**
     * 默认角色
     */
    private String defaultRole;

    /**
     * 用户类型：后台管理用户(admin)/app的名称（用户注册时带必填上来源）
     */
    private String userType;

    /**
     * 用户注册来源：后台(background)/其它appid
     */
    private String userRegistSource;

    /**
     * session_key
     */
    private String sessionKey;

    private String mobilePhone;

    private List<TFrameUserRole> frameUserRoleList = new ArrayList<>();

    private boolean updated = false;

    private static final long serialVersionUID = 1L;
}