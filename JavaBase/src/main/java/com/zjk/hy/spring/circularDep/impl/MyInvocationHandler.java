package com.zjk.hy.spring.circularDep.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("66666,这就有点高级喽");
        return null;
    }
}
