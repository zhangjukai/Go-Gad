package com.zjk.hy.spring.circularDep.impl;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return null;
    }


    public static void main(String[] args) {
        ProxyFactory proxyFactory=new ProxyFactory();
    }
}
