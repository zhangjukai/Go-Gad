package com.zjk.hy.spring.ioc.lifeCycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Car implements InitializingBean, DisposableBean {
    private String name;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("car初始化完成");
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("car销毁");
    }
    public void init(){
        System.out.println("car初始化完成--通过注解配置");
    }
    public void destroyMethod(){
        System.out.println("car销毁--通过注解配置");
    }

    @PostConstruct
    public void post(){
        System.out.println("car初始化完成--PostConstruct");
    }
    @PreDestroy
    public void pre(){
        System.out.println("car销毁----PreDestroy");
    }
}

