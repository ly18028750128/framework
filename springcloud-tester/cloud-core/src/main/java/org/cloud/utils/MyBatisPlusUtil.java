package org.cloud.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

public class MyBatisPlusUtil {

    private MyBatisPlusUtil() {

    }

    private final static MyBatisPlusUtil myBatisPlusUtil = new MyBatisPlusUtil();

    public static MyBatisPlusUtil single() {
        return myBatisPlusUtil;
    }

    public <T> QueryWrapper<T> createQueryWrapperByParams(Map<String, Object> params) {

        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        params.forEach((column, value) -> {

            List<?> listValue = null;

            if (value == null) {
                queryWrapper.isNotNull(column);
            } else if (value instanceof Object[]) {
                listValue = Arrays.asList((Object[]) value);
            } else if (value instanceof List<?>) {
                listValue = (List<?>) value;
            } else {
                queryWrapper.eq(column, value);
            }

            if (listValue == null) {
                return;
            }
            if (listValue.size() == 1) {
                queryWrapper.eq(column, value);
                return;
            }

            String[] columns = column.split("::");
            if (columns.length > 1) {
                if ("BETWEEN".equalsIgnoreCase(columns[1])) {
                    queryWrapper.between(column, listValue.get(0), listValue.get(1));
                } else {
                    queryWrapper.in(column, listValue);
                }
            } else {
                queryWrapper.in(column, listValue);
            }
        });

        return queryWrapper;

    }
}
