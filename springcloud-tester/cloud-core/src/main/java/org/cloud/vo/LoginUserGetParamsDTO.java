package org.cloud.vo;

import java.util.HashMap;
import java.util.Map;

public class LoginUserGetParamsDTO {

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String userName;
    private Integer microAppIndex;

    private Map<String, Object> params;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getMicroAppIndex() {
        return microAppIndex;
    }

    public void setMicroAppIndex(Integer microAppIndex) {
        this.microAppIndex = microAppIndex;
    }

    public Map<String, Object> getParams() {
        if(params==null){
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
