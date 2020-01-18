package org.cloud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.cloud.deserializer.GrantedAuthorityDeserializer;
import org.cloud.deserializer.GrantedFrameRoleDeserializer;
import org.cloud.model.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class LoginUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<TFrameRole> roles = new ArrayList<>();
    // 菜单列表
    private Collection<TFrameMenu> frameMenuList = new ArrayList<>();
    private Collection<GrantedAuthority> authorities ;
    private String token;
    public String userType;
    private String defaultRole;
    private String email;
    private String fullName;
    private String userRegistSource;
    private String sessionKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<TFrameRole> roles) {
        this.roles = roles;
    }

    public Collection<TFrameRole> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return roles;
    }

    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        List<String> roleList = new ArrayList<>();
        for (TFrameRole frameRole : getRoles()) {
            roleList.add(frameRole.getRoleCode());
        }
        authorities = AuthorityUtils.createAuthorityList(roleList.toArray(new String[]{}));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserRegistSource() {
        return userRegistSource;
    }

    public void setUserRegistSource(String userRegistSource) {
        this.userRegistSource = userRegistSource;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Collection<TFrameMenu> getFrameMenuList() {
        if (frameMenuList == null) {
            frameMenuList = new ArrayList<>();
        }
        return frameMenuList;
    }

    public void setFrameMenuList(Collection<TFrameMenu> frameMenuList) {
        this.frameMenuList = frameMenuList;
    }

    private static final long serialVersionUID = -6997588753870506318L;
}
