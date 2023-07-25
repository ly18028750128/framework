package org.cloud.vo;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.TimeZone;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.DateTimeFormat;
import org.cloud.utils.JdbcTypeConvertUtil;
import org.cloud.utils.SystemStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Clob;
import java.util.LinkedHashMap;

public class JavaBeanResultMap<V> extends LinkedHashMap<String, V> {

    Logger logger = LoggerFactory.getLogger(JavaBeanResultMap.class);

    private static final long serialVersionUID = 5703867527125507390L;

    public JavaBeanResultMap() {
        super(20, 1L);
    }

    @Override
    public V put(String key, V value) {
        //
        if (!key.contains("_") && key.matches("^\\S+[A-Z]+\\S+$")) {
            return super.put(key, value);
        }

        key = SystemStringUtil.single().camelName(key.toLowerCase());

        if (value instanceof Clob) {
            return super.put(key, (V) JdbcTypeConvertUtil.signle().ClobToString((Clob) value));
        }

        if (value instanceof java.sql.Date) {
            return super.put(key, (V) JdbcTypeConvertUtil.signle().dateToString((java.sql.Date) value));
        }

        if (value instanceof java.util.Date) {
            SimpleDateFormat dateFormat = DateTimeFormat.ISODATE.getDateFormat();
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));

            return super.put(key, (V) dateFormat.format((java.util.Date) value));
        }

        return super.put(key, value);
    }

    public V putNoCamel(String key, V value) {
        return super.put(key, value);
    }

}
