package org.cloud.mybatis.interceptor;

import static org.cloud.constant.UserDataDimensionConstant.USER_DIMENSION_CACHE_KEY;

import com.alibaba.fastjson.JSON;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.cloud.aop.DataDimensionAuthAspect;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.exception.BusinessException;
import org.cloud.model.DataDimensionCondition;
import org.cloud.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 *
 */
@Component
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Slf4j
public class DataDimensionInterceptor implements Interceptor {

    @Value("${system.sql.slow.time:3000}")
    private long time;

    @Value("${system.DataDimension.open:false}")  // 全局开关，是否开通数据权限，默认为关闭
    private Boolean isSystemDataDimensionOpen;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //通过StatementHandler获取执行的sql
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);

        if (isSystemDataDimensionOpen && DataDimensionAuthAspect.isOpen()) {
            field.set(boundSql, generateDataDimensionSql(sql));
        } else {
            field.set(boundSql, replaceAllDataDimensionSql(sql));
        }

        long start = System.currentTimeMillis();
        Object proceed = invocation.proceed();
        long end = System.currentTimeMillis();
        if ((end - start) > time) {
            log.info("本次数据库操作是慢查询，sql是:" + sql);
        }
        return proceed;

    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private String generateDataDimensionSql(String boundSql) throws Throwable {
        final Pattern pattern = Pattern.compile("^.*<dataDimension>.+</dataDimension>", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(boundSql);

        if (!matcher.find()) {
            return boundSql;
        }

        final Long userId = RequestContextManager.single().getRequestContext().getUser().getId();

        final Map<String, Set<String>> userDataDimensionMap = redisUtil
            .hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + userId, USER_DIMENSION_CACHE_KEY);

        if (CollectionUtil.single().isEmpty(userDataDimensionMap)) {
            throw new BusinessException("请配置数据权限！！");
        }

        String dataDimensionStr = matcher.group();
        while (true) {
            List<DataDimensionCondition> dataDimensionList = JSON
                .parseArray(dataDimensionStr.replaceFirst("<dataDimension>", "").replaceFirst("</dataDimension>", ""),
                    DataDimensionCondition.class);
            boundSql = matcher.replaceFirst(generateSingleDataDimension(dataDimensionList, userDataDimensionMap));
            matcher = pattern.matcher(boundSql);
            if (!matcher.find()) {
                break;
            }
            dataDimensionStr = matcher.group();
        }
        return boundSql;
    }

    /**
     * 替换掉所有的权限代码段
     *
     * @param boundSql
     * @return
     */
    private String replaceAllDataDimensionSql(String boundSql) {
        final Pattern pattern = Pattern.compile("^.*<dataDimension>.+</dataDimension>", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(boundSql);
        while (matcher.find()) {
            boundSql = matcher.replaceFirst("");
            matcher = pattern.matcher(boundSql);
        }
        return boundSql;
    }

    @Autowired
    RedisUtil redisUtil;

    private String generateSingleDataDimension(final List<DataDimensionCondition> dataDimensionConditionList,
        Map<String, Set<String>> userDataDimensionMap) {
        StringBuilder result = new StringBuilder("and (");
        for (int idx = 0; idx < dataDimensionConditionList.size(); idx++) {
            result.append(" ( ");
            final DataDimensionCondition dataDimension = dataDimensionConditionList.get(idx);
            Set<String> dataDimensionValueSet = userDataDimensionMap.get(dataDimension.getDimensionName());

            if (dataDimensionValueSet == null || dataDimensionValueSet.size() == 0) {
                result.append(" (1 = 2) ");
            } else {
                if (dataDimensionValueSet.contains("***")) {  // 如果包含了***,那么说明有全部仅限
                    result.append(" (1 = 1) ");
                } else {
                    if ("in".equalsIgnoreCase(dataDimension.getOperator())) {
                        String mergeString = dataDimensionValueSet.stream().filter(string -> !string.isEmpty())
                            .collect(Collectors.joining("','"));
                        result.append(" (").append(dataDimension.getFieldName()).append(" ").append(dataDimension.getOperator()).append(" ").append("('")
                            .append(mergeString).append("'))");
                    } else {
                        int idx1 = 0;
                        for (String s : dataDimensionValueSet) {
                            result.append(" (").append(dataDimension.getFieldName()).append(" ").append(dataDimension.getOperator()).append(" ").append("'")
                                .append(s).append("')");
                            idx1++;
                            if (idx1 < dataDimensionValueSet.size()) { // 最后一条不加连接符
                                result.append(" or ");
                            }
                        }
                    }
                }
            }
            result.append(" ) ");
            if (idx < dataDimensionConditionList.size() - 1) { // 最后一条不加连接符
                result.append(" ").append(dataDimension.getConnector()).append(" ");
            }
        }
        result.append(" )");
        return result.toString();
    }


}
