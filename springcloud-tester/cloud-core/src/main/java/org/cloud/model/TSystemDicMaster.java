package org.cloud.model;

import java.util.Date;
import lombok.Data;

/**
 * Table: t_system_dic_master
 */
@Data
public class TSystemDicMaster {
    /**
     * Column: dic_master_id
     * Remark: id
     */
    private Long dicMasterId;

    /**
     * Column: dic_code
     * Remark: 字典编码
     */
    private String dicCode;

    /**
     * Column: dic_name
     * Remark: 字典名称
     */
    private String dicName;

    /**
     * Column: remark
     * Remark: 备注
     */
    private String remark;

    /**
     * Column: belong_micro_service
     * Remark: 所属微服务，默认为General(通用)
     */
    private String belongMicroService;

    /**
     * Column: parent_master_id
     * Remark: 父级id
     */
    private Long parentMasterId;

    /**
     * Column: status
     * Remark: 状态，1/有效 0/无效 -1/过期
     */
    private Integer status;

    /**
     * Column: create_by
     * Remark: 创建人
     */
    private Long createBy;

    /**
     * Column: create_date
     * Remark: 创建日期
     */
    private Date createDate;

    /**
     * Column: update_by
     * Remark: 更新人
     */
    private Long updateBy;

    /**
     * Column: update_date
     * Remark: 更新日期
     */
    private Date updateDate;
}