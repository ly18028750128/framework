package org.cloud.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理类
 */
public final class ExceptionUtil {

    private final static ExceptionUtil instance = new ExceptionUtil();

    public static ExceptionUtil single() {
        return instance;
    }

    /**
     * 获取最终引起的异常
     * @param e
     * @return
     */
    public Throwable getFinalCause(Throwable e) {
        if (e.getCause() == null) {
            return e;
        } else {
            return getFinalCause(e.getCause());
        }
    }

    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

    private ExceptionUtil() {

    }
}
