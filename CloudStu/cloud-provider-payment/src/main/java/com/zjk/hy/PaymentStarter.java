package com.zjk.hy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(value = "com.zjk.hy.dao")
public class PaymentStarter {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PaymentStarter.class, args);
    }
}
