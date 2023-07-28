package org.cloud.scheduler.config;


import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.cloud.scheduler.annotation.JobTaskLog;
import org.cloud.scheduler.aop.advisor.AnnotationPointcutAdvisor;
import org.cloud.scheduler.interceptor.DefaultJobTaskMethodInterceptor;
import org.cloud.scheduler.interceptor.JobTaskLogCustomizer;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Description: 控制器切点配置
 * @Author Emily
 * @Version: 1.0
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@ConditionalOnProperty(prefix = "system.jobTask.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class JobTaskLogAutoConfiguration implements BeanFactoryPostProcessor, InitializingBean, DisposableBean {


    /**
     * Mybatis请求日志拦截切面增强类 checkInherited:是否验证父类或接口集成的注解，如果注解用@Inherited标注则自动集成
     *
     * @return 组合切面增强类
     * @since 4.0.5
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor mybatisLogAdvisor(ObjectProvider<JobTaskLogCustomizer> jobTaskLogCustomizers) {
        //限定类级别的切点
        Pointcut cpc = new AnnotationMatchingPointcut(JobTaskLog.class, true);
        //限定方法级别的切点
        Pointcut mpc = new AnnotationMatchingPointcut(null, JobTaskLog.class, true);
        //组合切面(并集)，即只要有一个切点的条件符合，则就拦截
        Pointcut pointcut = new ComposablePointcut(cpc).union(mpc);
        //mybatis日志拦截切面
        MethodInterceptor interceptor = jobTaskLogCustomizers.orderedStream().findFirst().get();
        //切面增强类
        AnnotationPointcutAdvisor advisor = new AnnotationPointcutAdvisor(interceptor, pointcut);
        //切面优先级顺序
        advisor.setOrder(10);
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean
    public DefaultJobTaskMethodInterceptor defaultJobTaskMethodInterceptor(MongoTemplate mongoTemplate) {
        return new DefaultJobTaskMethodInterceptor(mongoTemplate);
    }

    /**
     * 将指定的bean 角色标记为基础设施类型，相关提示类在 org.springframework.context.support.PostProcessorRegistrationDelegate
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanNamesForType(QuartzJobBean.class);
        if (beanNames.length > 0 && beanFactory.containsBeanDefinition(beanNames[0])) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanNames[0]);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("<== 【销毁--自动化配置】----定时任务日志拦截组件【JobTaskLogAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("==> 【初始化--自动化配置】----定时任务日志拦截组件【JobTaskLogAutoConfiguration】");
    }

    /**
     * 配置Quartz独立数据源的配置
     */
    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource.quartz")
    public DataSource quartzDataSource() {
        return new DruidDataSource();
    }
}
