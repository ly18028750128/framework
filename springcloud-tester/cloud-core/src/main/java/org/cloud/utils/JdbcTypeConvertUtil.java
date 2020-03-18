package org.cloud.utils;

import org.cloud.constant.CoreConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Date;

public final class JdbcTypeConvertUtil {

    Logger logger = LoggerFactory.getLogger(JdbcTypeConvertUtil.class);


    private JdbcTypeConvertUtil() {
    }

    private static class Handler {
        private static JdbcTypeConvertUtil instance = new JdbcTypeConvertUtil();
    }

    public static JdbcTypeConvertUtil signle() {
        return Handler.instance;
    }

    public String ClobToString(Clob clob) {
        String ret = "";
        Reader read = null;
        BufferedReader br = null;
        try {
            read = clob.getCharacterStream();
            br = new BufferedReader(read);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (s != null) {
                sb.append(s);
                s = br.readLine();
            }
            ret = sb.toString();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (Exception e) {

                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {

                }
            }

        }
        return ret;
    }

    public String dateToString(Date date) {
        return CoreConstant.DateTimeFormat.ISODATE.getDateFormat().format(new java.util.Date(date.getTime()));
    }
}
