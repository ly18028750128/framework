package org.cloud.vo;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class UserRoleTest {

    @Test
    public void testEquals() {

        Set<UserRole> roles = new HashSet<>();
        UserRole  role1 = new UserRole();
        UserRole  role2 = new UserRole();
        role1.setRoleId(1);
        role2.setRoleId(1);
        role1.setRoleCode("role1");
        role2.setRoleCode("role2");
        role1.setRoleName("setRoleName1");
        role2.setRoleName("setRoleName2");
        roles.add(role1);
        Assert.assertEquals(role1,role2);
        Assert.assertEquals(roles.contains(role2),true);
        roles.add(role2);
        Assert.assertEquals(roles.size(),1);
        role2.setRoleName("setRoleName2");
        roles.add(role2);
        Assert.assertEquals(roles.size(),2);



    }
}