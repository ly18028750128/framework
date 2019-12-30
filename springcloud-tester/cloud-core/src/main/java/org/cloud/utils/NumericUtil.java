package org.cloud.utils;

public class NumericUtil {

    private NumericUtil() {
    }

    private static class Handler {
        private Handler() {
        }

        private static NumericUtil handler = new NumericUtil();
    }

    public static NumericUtil single() {
        return Handler.handler;
    }

    public Integer countStep(Integer size, Integer stepSize) {
        return Double.valueOf(Math.ceil((size * 1.00) / (stepSize * 1.00))).intValue();
    }
}
