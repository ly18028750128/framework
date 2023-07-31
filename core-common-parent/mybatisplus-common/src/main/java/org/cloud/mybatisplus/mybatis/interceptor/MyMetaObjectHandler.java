package org.cloud.mybatisplus.mybatis.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;

public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUserDetails currentUser = RequestContextManager.single().getRequestContext().getUser();
        if (currentUser == null) {
            return;
        }
        setCreateOrUpdateBy(metaObject, "createBy", currentUser);
        setCreateOrUpdateBy(metaObject, "createdBy", currentUser);
        setCreateOrUpdateBy(metaObject, "updateBy", currentUser);
        setCreateOrUpdateBy(metaObject, "updatedBy", currentUser);

        setCreateOrUpdateUsername(metaObject, "createByName", currentUser);
        setCreateOrUpdateUsername(metaObject, "createdByName", currentUser);
        setCreateOrUpdateUsername(metaObject, "updateByName", currentUser);
        setCreateOrUpdateUsername(metaObject, "updatedByName", currentUser);
    }

    @Override
    public void updateFill(MetaObject metaObject) {



        final LoginUserDetails currentUser = RequestContextManager.single().getRequestContext().getUser();
        if (currentUser == null) {
            return;
        }
        setCreateOrUpdateBy(metaObject, "updateBy", currentUser);
        setCreateOrUpdateBy(metaObject, "updatedBy", currentUser);

        setCreateOrUpdateUsername(metaObject, "updateByName", currentUser);
        setCreateOrUpdateUsername(metaObject, "updatedByName", currentUser);
    }

    private void setCreateOrUpdateBy(MetaObject metaObject, String name, LoginUserDetails currentUser) {
        if (!metaObject.hasGetter(name)) {
            return;
        }
        if (String.class.equals(metaObject.getSetterType(name))) {
            metaObject.setValue(name, currentUser.getUsername());
        } else {
            metaObject.setValue(name, currentUser.getId());
        }
    }

    private void setCreateOrUpdateUsername(MetaObject metaObject, String name, LoginUserDetails currentUser) {
        if (!metaObject.hasGetter(name)) {
            return;
        }
        metaObject.setValue(name, currentUser.getUsername());
    }

}
