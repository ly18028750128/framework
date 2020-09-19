package com.longyou.comm.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FrameUserRefVO implements Serializable {
    private static final long serialVersionUID = -4527913620064052583L;
    private Long id;

    private String userId;

    private String attributeName;

    private String attributeValue;

    private String remark;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;
}
