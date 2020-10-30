package com.zjk.hy.spring.ioc;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {

    public MyAnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
        super();
        // 关闭循环依赖
        setAllowCircularReferences(false);
        register(annotatedClasses);
        refresh();
    }

    @Override
    protected void initPropertySources() {
        System.out.println("===================================");
        super.initPropertySources();
        System.out.println("===================================");
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        super.postProcessBeanFactory(beanFactory);
        System.out.println("自定义的");
    }
}
