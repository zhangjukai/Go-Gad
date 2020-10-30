package com.zjk.hy.spring.ext;

import com.zjk.hy.spring.ioc.Dog;
import com.zjk.hy.spring.ioc.lifeCycle.Car;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;

@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("+++++MyBeanDefinitionRegistryPostProcessor-postProcessBeanDefinitionRegistry+++");
        System.out.println("registry-count:"+registry.getBeanDefinitionCount());
        RootBeanDefinition definition = new RootBeanDefinition(Dog.class);
        registry.registerBeanDefinition("dog",definition);

        AbstractBeanDefinition carBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Car.class).getBeanDefinition();
        registry.registerBeanDefinition("car",carBeanDefinition);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("++++MyBeanDefinitionRegistryPostProcessor-postProcessBeanFactory++++");
        System.out.println("beanFactory:"+beanFactory.getBeanDefinitionCount());
    }
}
