package org.cloud.common.service;


import org.cloud.config.AESConfig;
import org.cloud.utils.AES128Util;


public class AESService {

  private final AESConfig aesConfig;

  public AESService(AESConfig aesConfig) {
    this.aesConfig = aesConfig;
  }

  public String encrypt(String str) throws Exception {
    return AES128Util.single().encrypt(str, aesConfig.getKey(), aesConfig.getVi());
  }

  public String decrypt(String str) throws Exception {
    return AES128Util.single().decrypt(str, aesConfig.getKey(), aesConfig.getVi());
  }

}
