package org.cloud.utils;

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

    private ExceptionUtil() {

    }
}
