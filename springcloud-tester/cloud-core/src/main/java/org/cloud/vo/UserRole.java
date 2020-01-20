package org.cloud.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class UserRole implements Serializable {

    private Integer roleId;
    private String roleName;
    private String roleCode;

    private static final long serialVersionUID = -8926664098708239611L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(roleId, userRole.roleId) &&
                Objects.equals(roleName, userRole.roleName) &&
                Objects.equals(roleCode, userRole.roleCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName, roleCode);
    }
}
