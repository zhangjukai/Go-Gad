package com.zjk.hy.spring.ioc.lifeCycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.zjk.hy.spring.ioc.lifeCycle")
@PropertySource(value={"classpath:demo.properties"},encoding = "GB2312")
public class LifeCycleMainConfig {
    @Bean(initMethod = "init",destroyMethod = "destroyMethod")
    public Car car () {
        return new Car();
    }
}
