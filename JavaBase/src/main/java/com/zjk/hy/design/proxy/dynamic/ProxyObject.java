package com.zjk.hy.design.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理对象
 */
public class ProxyObject {
    private Object target;

    public ProxyObject(Target target) {
        this.target = target;
    }

    public Object getProxyInstance(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("JDK代理开始。。。。。。");
                        Object result = method.invoke(target, args);
                        System.out.println("JDK代理结束。。。。。。");
                        return result;
                    }
                });
    }
}
