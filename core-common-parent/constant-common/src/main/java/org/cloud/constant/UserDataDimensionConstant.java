package org.cloud.constant;


import lombok.AllArgsConstructor;

public final class UserDataDimensionConstant {

  private UserDataDimensionConstant() {
  }

  public final static String USER_DIMENSION_CACHE_KEY = "dataDimension";

  /**
   * 权限维度，这里显示的默认维度，实际系统的维度从数据字典中获取，diccode为data_dimension
   */
  @AllArgsConstructor
  public enum UserDataDimension implements BasicEnum<String> {
    CHILD_SEARCH_PATH("child_search_path", "用户查询路径", "用户查询路径"),
    BIND_COMPANY_USER_ID("bind_company_user_id", "绑定的公司用户ID", "绑定的公司用户ID"),
    ;

    private String dimensionName;
    private String description;
    private String i18nValue;

    @Override
    public String value() {
      return dimensionName;
    }

    @Override
    public String description() {
      return description;
    }

    @Override
    public String i18nValue() {
      return i18nValue;
    }
  }

}
