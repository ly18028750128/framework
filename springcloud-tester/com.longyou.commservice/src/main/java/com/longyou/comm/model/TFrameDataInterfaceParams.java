package com.longyou.comm.model;

/**
 * Table: t_frame_data_interface_params
 */
public class TFrameDataInterfaceParams {
    /**
     * Column: data_code
     * Remark: 数据编号
     */
    private String dataCode;

    /**
     * Column: seq
     */
    private Integer seq;

    /**
     * Column: param_name
     */
    private String paramName;

    /**
     * Column: id
     */
    private Long id;

    /**
     * Column: param_type
     */
    private String paramType;

    /**
     * Column: param_desc
     */
    private String paramDesc;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode == null ? null : dataCode.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType == null ? null : paramType.trim();
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc == null ? null : paramDesc.trim();
    }
}