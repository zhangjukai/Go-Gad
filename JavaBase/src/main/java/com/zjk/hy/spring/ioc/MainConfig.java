package com.zjk.hy.spring.ioc;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * 配置文件-等同于以前的xml配置文件
 */
@Configuration
@PropertySource(value={"classpath:demo.properties"},encoding = "GB2312")
@ComponentScan(value = "com.zjk.hy.spring.ioc",useDefaultFilters = false,includeFilters={
        /*@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {RestController.class}),*/
        @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyFilterType.class})
})
/*@ComponentScan(value = "com.zjk.hy.spring.annotation",excludeFilters={
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {RestController.class}),
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Service.class})
})*/
@Import(Dog.class)
public class MainConfig {
    @Autowired
    Environment env;

   // @Scope(value = "prototype")
    @Bean(value = "person",autowire = Autowire.BY_NAME)
    @Lazy
    /*@Conditional()*/
    public Person person(){
        return new Person("Tom",120);
    }

    @Bean
    public Company company(){
        return new Company("世界之心");
    }

}
