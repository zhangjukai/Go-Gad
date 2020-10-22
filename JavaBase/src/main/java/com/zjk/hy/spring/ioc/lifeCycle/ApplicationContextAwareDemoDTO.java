package com.zjk.hy.spring.ioc.lifeCycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextAwareDemoDTO implements ApplicationContextAware, BeanNameAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContextAwareDemoDTO() {
        System.out.println("ApplicationContextAwareDemoDTO 构造方法被调用");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }
}
