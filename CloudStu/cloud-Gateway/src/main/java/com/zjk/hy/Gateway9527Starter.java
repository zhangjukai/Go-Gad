package com.zjk.hy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableEurekaClient
public class Gateway9527Starter {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Gateway9527Starter.class, args);
    }
}
