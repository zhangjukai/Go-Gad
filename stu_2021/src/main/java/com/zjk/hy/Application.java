package com.zjk.hy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author: zhangjukai
 * @CreateDate: 2021/9/27 10:28
 * @Description:
 **/
@SpringBootApplication
public class Application {
    public static ConfigurableApplicationContext APPLICATION_CONTEXT;
    public static void main(String[] args) {
        APPLICATION_CONTEXT = SpringApplication.run(Application.class, args);
    }
}
