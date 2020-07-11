package com.zjk.hy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring中bean的工具类
 * @author zjk
 *
 */
@Component
public final class SpringTool implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringTool.applicationContext == null) {
            SpringTool.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据名称获取具体的bean
     * @param name bean的名称
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
    
    /**
     * 根据class获取bean
     * @param c
     * @return
     */
    public static <T> T getBean(Class<T> c) {
    	return getApplicationContext().getBean(c);
    }
    
}
