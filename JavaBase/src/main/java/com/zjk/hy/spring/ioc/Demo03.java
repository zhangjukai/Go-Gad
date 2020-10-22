package com.zjk.hy.spring.ioc;

import com.zjk.hy.spring.ioc.lifeCycle.LifeCycleMainConfig;
import com.zjk.hy.spring.ioc.lifeCycle.ProBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Demo03 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(LifeCycleMainConfig.class);
        ProBean car = applicationContext.getBean(ProBean.class);
        System.out.println(car.toString());
        applicationContext.close();
    }
}
