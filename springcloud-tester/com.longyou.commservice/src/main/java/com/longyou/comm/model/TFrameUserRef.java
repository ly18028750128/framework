package com.longyou.comm.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TFrameUserRef implements Serializable {
    private Long id;

    private Long userId;

    private String attributeName;

    private String attributeValue;

    private String remark;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;
}
