package com.zjk.hy.spring.ext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// @Component
public class MyBeanFactoryPostProcessor  implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("+++++++++++MyBeanFactoryPostProcessor+++++++++++");
        int count = beanFactory.getBeanDefinitionCount();
        System.out.println("BeanDefinition的数量："+count);
        String[] names = beanFactory.getBeanDefinitionNames();
        System.out.println("BeanDefinitions："+ Arrays.asList(names));
    }
}
