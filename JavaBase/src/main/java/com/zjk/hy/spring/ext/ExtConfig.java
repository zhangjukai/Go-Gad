package com.zjk.hy.spring.ext;

import com.zjk.hy.spring.ioc.Dog;
import com.zjk.hy.spring.ioc.lifeCycle.Car;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.zjk.hy.spring.ext")
@Configuration
public class ExtConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExtConfig.class);

        /*Dog dog = context.getBean(Dog.class);
        Car car = context.getBean(Car.class);
        System.out.println(dog);
        System.out.println(car);*/
        // context.publishEvent(new ApplicationEvent(new String("我也不知道咋回事")) {});
        context.close();
    }
}
