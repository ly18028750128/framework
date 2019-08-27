package com.longyou.comm.model;

/**
 * Table: t_frame_data_rest_config
 */
public class TFrameDataRestConfig {
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
     * Column: method
     * Remark: GET/POST
     */
    private String method;

    /**
     * Column: must_login
     */
    private String mustLogin;

    /**
     * Column: login_bean
     * Remark: 请继承IFrameDataInterfaceLoginService，返回登录后java.net.HttpURLConnection
     */
    private String loginBean;

    /**
     * Column: uri
     * Remark: 数据访问地址
     */
    private String uri;

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getMustLogin() {
        return mustLogin;
    }

    public void setMustLogin(String mustLogin) {
        this.mustLogin = mustLogin == null ? null : mustLogin.trim();
    }

    public String getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(String loginBean) {
        this.loginBean = loginBean == null ? null : loginBean.trim();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }
}