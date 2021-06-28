package com.longyou.paycenter.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cloud.utils.CollectionUtil;
import org.springframework.util.Assert;

/**
 * 支付商户
 */
@ApiModel(value = "com-longyou-paycenter-model-PayMerchant")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayMerchant {

  @ApiModelProperty(value = "")
  private Long payMerchantId;

  /**
   * 商户编码
   */
  @ApiModelProperty(value = "商户编码")
  private String merchantCode;

  /**
   * 商户名称
   */
  @ApiModelProperty(value = "商户名称")
  private String merchantName;

  /**
   * 最大支付账户数，最大可配置的支付账户的数量
   */
  @ApiModelProperty(value = "最大支付账户数，最大可配置的支付账户的数量")
  private Integer maxPayAccount;

  /**
   * 到期日期
   */
  @ApiModelProperty(value = "到期日期")
  private Date expireDate;

  /**
   * 状态，1/有效 0/无效 -1/过期
   */
  @ApiModelProperty(value = "状态，1/有效 0/无效 -1/过期")
  private Integer status;

  /**
   * aes密码，通过系统的aes进行二次加密后的存储到数据库中
   */
  @ApiModelProperty(value = "aes密码，通过系统的aes进行二次加密后的存储到数据库中")
  private String aesKey;

  /**
   * aes vi val，通过系统的aes进行二次加密后的存储到数据库中
   */
  @ApiModelProperty(value = "aes vi val，通过系统的aes进行二次加密后的存储到数据库中")
  private String aesViVal;

  /**
   * 创建人
   */
  @ApiModelProperty(value = "创建人")
  private String createBy;

  /**
   * 创建日期
   */
  @ApiModelProperty(value = "创建日期")
  private Date createDate;

  /**
   * 更新人
   */
  @ApiModelProperty(value = "更新人")
  private String updateBy;

  /**
   * 更新日期
   */
  @ApiModelProperty(value = "更新日期")
  private Date updateDate;

  public void validate() throws Exception{
    Assert.isTrue(CollectionUtil.single().isNotEmpty(getMerchantCode()), "payMerchant.merchantCode.not.null");
    Assert.isTrue(CollectionUtil.single().isNotEmpty(getAesKey()), "payMerchant.merchantCode.not.null");
  }

}