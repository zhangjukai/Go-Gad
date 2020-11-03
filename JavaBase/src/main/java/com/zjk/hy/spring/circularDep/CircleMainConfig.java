package com.zjk.hy.spring.circularDep;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.zjk.hy.spring.circularDep")
public class CircleMainConfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CircleMainConfig.class);
        for (int i = 0; i < 5; i++) {
            UserService indexService = context.getBean(UserService.class);
            indexService.print();
        }

    }
}
