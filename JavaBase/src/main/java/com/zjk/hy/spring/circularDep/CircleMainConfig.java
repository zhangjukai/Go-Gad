package com.zjk.hy.spring.circularDep;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.zjk.hy.spring.circularDep")
public class CircleMainConfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CircleMainConfig.class);
        IndexService indexService = context.getBean(IndexService.class);
        indexService.print();
    }
}
