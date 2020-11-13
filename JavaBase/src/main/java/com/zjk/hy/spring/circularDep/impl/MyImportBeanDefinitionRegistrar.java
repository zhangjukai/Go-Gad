package com.zjk.hy.spring.circularDep.impl;

import com.zjk.hy.spring.circularDep.TestBaseService;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition("MIBDR-baseService",new RootBeanDefinition(TestBaseServiceImpl.class));
        // 注入一个代理对象
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(TestBaseService.class);
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) builder.getBeanDefinition();
        System.out.println(beanDefinition.getBeanClassName());
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
        beanDefinition.setBeanClass(MyFactoryBean.class);
        registry.registerBeanDefinition("proxy-baseService",beanDefinition);

    }
}
