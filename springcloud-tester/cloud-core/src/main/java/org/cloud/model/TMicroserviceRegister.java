package org.cloud.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_microservice_register
 * @author 
 */
@Data
public class TMicroserviceRegister implements Serializable {
    private Integer microserviceId;

    /**
     * 微服务名称
     */
    private String microserviceName;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private static final long serialVersionUID = 6410542977386540758L;
}