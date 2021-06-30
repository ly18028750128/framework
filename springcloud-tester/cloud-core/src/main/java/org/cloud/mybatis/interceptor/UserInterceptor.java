package org.cloud.mybatis.interceptor;

import java.lang.reflect.Field;
import java.util.Properties;
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
import org.springframework.stereotype.Component;

@Component
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
        final Field[] fields = paramObj.getClass().getDeclaredFields();

        LoginUserDetails currentUser = RequestContextManager.single().getRequestContext().getUser();

        if (currentUser == null) {
            return executor.update(ms, paramObj);
        }

        for (final Field field : fields) {
            field.setAccessible(true);
            final String fieldName = field.getName();
            switch (fieldName) {
                case "createBy":
                case "updateBy":
                    if (field.get(paramObj) instanceof String) {
                        field.set(paramObj, currentUser.getUsername());
                    } else if (field.get(paramObj) instanceof Long) {
                        field.set(paramObj, currentUser.getId());
                    }
                    break;
                default:
                    break;
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

        LoginUserDetails currentUser = RequestContextManager.single().getRequestContext().getUser();

        if (currentUser == null) {
            return executor.update(ms, paramObj);
        }

        final Field[] fields = paramObj.getClass().getDeclaredFields();
        for (final Field field : fields) {
            field.setAccessible(true);
            final String fieldName = field.getName();
            switch (fieldName) {
                case "updateBy":
                    if (field.getDeclaringClass().equals(String.class)) {
                        field.set(paramObj, currentUser.getUsername());
                    } else if (field.getDeclaringClass().equals(Long.class)) {
                        field.set(paramObj, currentUser.getId());
                    }
                    break;
                default:
                    break;
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
        final Field[] fields = paramObj.getClass().getDeclaredFields();
        for (final Field field : fields) {
            field.setAccessible(true);
            final String fieldName = field.getName();
            switch (fieldName) {
                case "status":

                    if (field.getDeclaringClass().equals(Integer.class)) {
                        field.set(paramObj, -1);
                    }
                    break;
                default:
                    break;
            }
        }
        return executor.update(ms, paramObj);
    }
}
