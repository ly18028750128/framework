package org.cloud.mybatisplus.vo;

import com.alibaba.fastjson.JSONObject;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.cloud.constant.CoreConstant.DateTimeFormat;
import org.cloud.mybatisplus.utils.JdbcTypeConvertUtil;
import org.cloud.utils.SystemStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaBeanResultMap extends JSONObject {

    Logger logger = LoggerFactory.getLogger(JavaBeanResultMap.class);

    private static final long serialVersionUID = 5703867527125507390L;

    public final static TimeZone defaultTimeZone = TimeZone.getDefault();
    public final static int defaultZoneOffsetSeconds = defaultTimeZone.getRawOffset() / 1000;

    public JavaBeanResultMap() {
        super(20);
    }

    public JavaBeanResultMap(Map value) {
        super(value);
    }


    @Override
    public Object put(String key, Object value) {
        //
        if (!key.contains("_") && key.matches("^\\S+[A-Z]+\\S+$")) {
            return super.put(key, value);
        }

        key = SystemStringUtil.single().camelName(key.toLowerCase());

        if (value instanceof Clob) {
            return super.put(key, JdbcTypeConvertUtil.signle().ClobToString((Clob) value));
        }

        if (value instanceof java.sql.Date) {
            return super.put(key, JdbcTypeConvertUtil.signle().dateToString((java.sql.Date) value));
        }

        if (value instanceof java.util.Date) {
            SimpleDateFormat dateFormat = DateTimeFormat.ISODATE.getDateFormat();
            dateFormat.setTimeZone(defaultTimeZone);
            return super.put(key, dateFormat.format(value));
        }

        if (value instanceof LocalDateTime) {
            SimpleDateFormat dateFormat = DateTimeFormat.ISODATE.getDateFormat();
            dateFormat.setTimeZone(defaultTimeZone);
            long times = ((LocalDateTime) value).toInstant(ZoneOffset.ofTotalSeconds(defaultZoneOffsetSeconds)).toEpochMilli();
            return super.put(key, dateFormat.format(new Date(times)));
        }

        return super.put(key, value);
    }

    public Object putNoCamel(String key, Object value) {
        return super.put(key, value);
    }

}
