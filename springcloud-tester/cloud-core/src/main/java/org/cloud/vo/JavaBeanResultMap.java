package org.cloud.vo;

import org.cloud.utils.JdbcTypeConvertUtil;
import org.cloud.utils.SystemStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class JavaBeanResultMap<V> extends LinkedHashMap<String, V> {

    Logger logger = LoggerFactory.getLogger(JavaBeanResultMap.class);

    private static final long serialVersionUID = 5703867527125507390L;

    public JavaBeanResultMap() {
        super(20, 1L);
    }

    @Override
    public V put(String key, V value) {
        key = SystemStringUtil.camelName(key.toLowerCase());

        if (value instanceof Clob) {
            return super.put(key, (V) JdbcTypeConvertUtil.create().ClobToString((Clob) value));
        }

        if (value instanceof  java.sql.Date){
            return super.put(key, (V)JdbcTypeConvertUtil.create().dateToString((java.sql.Date)value));
        }

        return super.put(key, value);
    }

    public V putNoCamel(String key, V value) {
        return super.put(key, value);
    }



}
