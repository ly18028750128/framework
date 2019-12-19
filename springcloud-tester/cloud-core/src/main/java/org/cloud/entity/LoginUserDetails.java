package org.cloud.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cloud.deserializer.GrantedAuthorityDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class LoginUserDetails implements UserDetails {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String username;
    //    @JsonIgnore
    private String password;
    private Collection<String> roles = new ArrayList<>();
    private String token;


    private Collection<GrantedAuthority> authorities = new ArrayList<>();

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    @Override
    @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
    public Collection<GrantedAuthority> getAuthorities() {
        if (roles != null && !roles.isEmpty()) {
            authorities = AuthorityUtils.createAuthorityList(roles.toArray(new String[]{}));
        }
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

    public Collection<String> getRoles() {
        return roles;
    }

    public String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String defaultRole;

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }
}
