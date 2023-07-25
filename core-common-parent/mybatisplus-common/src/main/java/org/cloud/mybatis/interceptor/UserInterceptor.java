package org.cloud.mybatis.interceptor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;


@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class UserInterceptor implements Interceptor {

    @Override
    public Object intercept(final Invocation invocation) throws Exception {
        if (invocation.getTarget() instanceof Executor && invocation.getArgs().length == 2) {
            final Executor executor = (Executor) invocation.getTarget();
            // 获取第一个参数
            final MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            final Object paramObj = invocation.getArgs()[1];
            if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
                return executeInsert(executor, ms, paramObj);
            } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
                return executeUpdate(executor, ms, paramObj);
            } else if (ms.getSqlCommandType() == SqlCommandType.DELETE) {
                return executeDelete(executor, ms, paramObj);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(final Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(final Properties properties) {

    }

    // ------ 私有方法 --------

    /**
     * 更新操作
     *
     * @param executor executor
     * @param ms       ms
     * @param paramObj 参数
     * @return 返回执行结果
     */
    private Object executeInsert(final Executor executor, final MappedStatement ms, final Object paramObj) throws Exception {
        if (paramObj == null) {
            return executor.update(ms, null);
        }

        LoginUserDetails currentUser = RequestContextManager.single().getRequestContext().getUser();
        if (currentUser == null) {
            return executor.update(ms, paramObj);
        }

        final Field[] fields = paramObj.getClass().getDeclaredFields();

        List<Field> updateOrCreateField = Arrays.stream(fields).filter(
            item -> item.getName().equals("createBy") || item.getName().equals("createdBy") || item.getName().equals("updateBy") || item.getName()
                .equals("updatedBy")).collect(Collectors.toList());

        for (final Field field : updateOrCreateField) {
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(paramObj, currentUser.getUsername());
            } else {
                field.set(paramObj, currentUser.getId());
            }
        }

        List<Field> updateOrCreateNameField = Arrays.stream(fields).filter(
            item -> item.getName().equals("createByName") || item.getName().equals("createdByName") || item.getName().equals("updateByName") || item.getName()
                .equals("updatedByName")).collect(Collectors.toList());

        for (final Field field : updateOrCreateNameField) {
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(paramObj, currentUser.getUsername());
            }
        }

        return executor.update(ms, paramObj);
    }

    /**
     * 新增操作
     *
     * @param executor executor
     * @param ms       ms
     * @param paramObj 参数
     * @return 返回执行结果
     */
    private Object executeUpdate(final Executor executor, final MappedStatement ms, final Object paramObj) throws Exception {

        if (paramObj == null) {
            return executor.update(ms, null);
        }

        LoginUserDetails currentUser = RequestContextManager.single().getRequestContext().getUser();
        if (currentUser == null) {
            return executor.update(ms, paramObj);
        }

        final Field[] fields = paramObj.getClass().getDeclaredFields();

        List<Field> updateField = Arrays.stream(fields).filter(item -> item.getName().equals("updateBy") || item.getName().equals("updatedBy"))
            .collect(Collectors.toList());

        for (final Field field : updateField) {
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(paramObj, currentUser.getUsername());
            } else {
                field.set(paramObj, currentUser.getId());
            }
        }
        return executor.update(ms, paramObj);
    }

    /**
     * 逻辑删除操作
     *
     * @param executor executor
     * @param ms       ms
     * @param paramObj 参数
     * @return 返回执行结果
     */
    private Object executeDelete(final Executor executor, final MappedStatement ms, final Object paramObj) throws Exception {

        if (paramObj == null) {
            return executor.update(ms, null);
        }

        final Field[] fields = paramObj.getClass().getDeclaredFields();
        List<Field> statusField = Arrays.stream(fields).filter(item -> item.getName().equals("status")).collect(Collectors.toList());
        for (final Field field : statusField) {
            field.setAccessible(true);
            if (field.getType().equals(Integer.class)) {
                field.set(paramObj, -1);
            }
        }
        return executor.update(ms, paramObj);
    }
}
