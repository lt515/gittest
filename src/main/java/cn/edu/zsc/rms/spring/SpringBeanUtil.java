package cn.edu.zsc.rms.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author hsj
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass){
        return applicationContext.getBean(beanClass);
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
