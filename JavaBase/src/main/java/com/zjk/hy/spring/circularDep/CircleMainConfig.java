package com.zjk.hy.spring.circularDep;

import com.zjk.hy.spring.circularDep.impl.MyBeanFactoryPostProcessor;
import com.zjk.hy.spring.circularDep.impl.MyImportBeanDefinitionRegistrar;
import com.zjk.hy.spring.circularDep.impl.MyImportSelector;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.zjk.hy.spring.circularDep")
/*@Import({MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})*/
public class CircleMainConfig {
    public static void main(String[] args) {
       /* AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(IndexService.class);*/
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
        context.register(CircleMainConfig.class);
        context.refresh();
    }

    /*@Bean
    public IndexService indexService(){
        return new IndexService();
    }

    @Bean
    public UserService userService(){
        indexService();
        return new UserService();
    }*/
}
