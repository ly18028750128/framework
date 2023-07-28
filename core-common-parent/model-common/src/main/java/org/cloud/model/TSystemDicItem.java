package org.cloud.model;

import lombok.Data;

import java.util.Date;

/**
 * Table: t_system_dic_item
 */
@Data
public class TSystemDicItem {
    /**
     * Column: dic_item_id
     * Remark: id
     */
    private Long dicItemId;

    /**
     * Column: dic_master_id
     * Remark: 数据字典主表id
     */
    private Long dicMasterId;

    /**
     * Column: dic_item_code
     * Remark: 字典项编码
     */
    private String dicItemCode;

    /**
     * Column: dic_item_name
     * Remark: 字典项名称
     */
    private String dicItemName;

    /**
     * Column: dic_item_value
     * Remark: 字典项值
     */
    private String dicItemValue;

    /**
     * Column: ext_attribut1
     * Remark: 扩展属性1
     */
    private String extAttribut1;

    /**
     * Column: ext_attribut3
     * Remark: 扩展属性1
     */
    private String extAttribut3;

    /**
     * Column: ext_attribut2
     * Remark: 扩展属性1
     */
    private String extAttribut2;

    /**
     * Column: ext_attribut4
     * Remark: 扩展属性1
     */
    private String extAttribut4;

    /**
     * Column: ext_attribut5
     * Remark: 扩展属性1
     */
    private String extAttribut5;

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

    private Integer seq;

    private String language;

}