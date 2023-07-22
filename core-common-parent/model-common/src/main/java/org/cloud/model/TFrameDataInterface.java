package org.cloud.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Table: t_frame_data_interface
 */
public class TFrameDataInterface implements Serializable {
    /**
     * Column: data_code
     * Remark: 数据编号
     */
    private String dataCode;

    /**
     * Column: id
     */
    private Long id;

    /**
     * Column: data_name
     * Remark: 数据名称
     */
    private String dataName;

    /**
     * Column: data_type
     * Remark: 数据类型：SQL(SQL)/存储过程(SP)/REST/（OTHER）其它
     */
    private String dataType;

    /**
     * Column: data_execute_name
     * Remark: 必须继承IFrameDataInterfaceService，             并在应用中正确的注册，可以不填，             默认为util.[接口类型].FrameDataInterfaceMainService             如果需要指定那么该接口必须继承IFrameDataInterfaceService
     */
    private String dataExecuteName;

    /**
     * Column: create_by
     * Remark: 创建人
     */
    private String createBy;

    /**
     * Column: create_date
     * Remark: 创建日期
     */
    private Date createDate;

    /**
     * Column: update_by
     * Remark: 更新人
     */
    private String updateBy;

    /**
     * Column: update_date
     * Remark: 更新日期
     */
    private Date updateDate;

    /**
     * Column: description
     * Remark: 描述
     */
    private String description;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode == null ? null : dataCode.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName == null ? null : dataName.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public String getDataExecuteName() {
        return dataExecuteName;
    }

    public void setDataExecuteName(String dataExecuteName) {
        this.dataExecuteName = dataExecuteName == null ? null : dataExecuteName.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    private static final long serialVersionUID = 5889491168601391668L;
}