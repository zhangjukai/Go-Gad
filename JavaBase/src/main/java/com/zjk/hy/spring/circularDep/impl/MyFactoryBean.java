package com.zjk.hy.spring.circularDep.impl;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class MyFactoryBean implements FactoryBean {
    private Class clazz;
    public MyFactoryBean(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object getObject() throws Exception {
        Class[] classes = {clazz};
        Object proxyInstance = Proxy.newProxyInstance(this.getClass().getClassLoader(), classes, new MyInvocationHandler());
        return proxyInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }
}
