package org.cloud.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

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
            log.error(name + "," + name + "{}{}", "获取BEAN失败，", e.getMessage());
            return null;
        }
    }

    //通过class获取Bean.
    //通过name,以及Clazz返回指定的Bean

    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (NoUniqueBeanDefinitionException e) {
            log.error("Bean非唯一");
            throw e;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        } catch (Throwable e) {
            log.debug(name + "," + clazz.getName() + "{}{}", "获取BEAN失败，", e.getMessage());
            return null;
        }
    }

}
