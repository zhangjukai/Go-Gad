package com.zjk.hy.design.proxy.cglib;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 代理对象
 */
public class ProxyObject implements MethodInterceptor {
    private Object target;

    public ProxyObject(Object target) {
        this.target = target;
    }

    public Object getProxyInstance(){
        // 创建一个工具类
        Enhancer enhancer = new Enhancer();
        // 设置父类
        enhancer.setSuperclass(target.getClass());
        // 设置回调函数
        enhancer.setCallback(this);
        // 创建代理对象（子类对象）
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("Cglib代理开始。。。。。");
        Object result = method.invoke(target, args);
        System.out.println("Cglib代理结束。。。。。");
        return result;
    }
}
