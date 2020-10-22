package com.zjk.hy.spring.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Demo02 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name:names) {
            System.out.println(name);
        }
    }
}
