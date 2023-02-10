package com.longyou.comm.dto.imexport;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserExportDTO  {

    @ExcelProperty(value = "id",index = 0)
    private Long id;
    @ExcelProperty(value = {"基本信息","用户名"},index = 1)
    private String userName;

    @ExcelProperty(value = {"基本信息","全称"},index = 2)
    private String fullName;
    @ExcelProperty(value = {"基本信息","性别"},index = 3)
    private String sex;

    @ExcelProperty(value = {"联系方式","邮箱号码"},index = 4)
    private String email;
    @ExcelProperty(value = {"联系方式","手机号码"},index = 5)
    private String mobilePhone;

}
