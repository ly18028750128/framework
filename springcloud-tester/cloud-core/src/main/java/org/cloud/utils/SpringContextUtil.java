package org.cloud.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringContextUtil implements ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static <T> T getBean(String name) {
        try {
            return (T) getApplicationContext().getBean(name);
        } catch (Throwable e) {
            logger.debug(name + "," + name + "{}{}", "获取BEAN失败，", e.getMessage());
            return null;
        }
    }

    //通过class获取Bean.
    //通过name,以及Clazz返回指定的Bean

    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (Throwable e) {
            logger.debug(clazz.getName() + "{}{}", "获取BEAN失败，", e.getMessage());
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        } catch (Throwable e) {
            logger.debug(name + "," + clazz.getName() + "{}{}", "获取BEAN失败，", e.getMessage());
            return null;
        }
    }

}
