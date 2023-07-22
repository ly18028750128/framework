package org.cloud.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public final class SpringFactoryUtil implements BeanFactoryPostProcessor {


    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;


    //获取applicationContext
    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    //通过name获取 Bean.
    public static <T> T getBean(String name) {
        try {
            return (T) beanFactory.getBean(name);
        } catch (Throwable e) {
            log.error(name + "," + name + "{}{}", "获取BEAN失败，", e.getMessage());
            return null;
        }
    }

    //通过class获取Bean.
    //通过name,以及Clazz返回指定的Bean

    public static <T> T getBean(Class<T> clazz) {
        try {
            return beanFactory.getBean(clazz);
        } catch (Throwable e) {
            log.error(e.getMessage(),e );
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return beanFactory.getBean(name, clazz);
        } catch (Throwable e) {
            log.error(name + "," + clazz.getName() + "{}{}", "获取BEAN失败，", e.getMessage());
            return null;
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringFactoryUtil.beanFactory = beanFactory;
    }
}
