package org.cloud.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_framework_resource
 * @author 
 */
@Data
public class TFrameworkResource implements Serializable {
    private Long resourceId;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源路径
     */
    private String resourcePath;

    /**
     * 访问方法：get,post
     */
    private String method;

    /**
     * 所属微服务
     */
    private String belongMicroservice;

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

    private static final long serialVersionUID = 1L;
}