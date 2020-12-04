package com.zjk.hy.se.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyServiceTest {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        ProxyService instance = (ProxyService) Proxy.newProxyInstance(ProxyServiceTest.class.getClassLoader(),
                new Class[]{ProxyService.class},
                new ProxyServiceInvocationHandler(new DefaultProxyServiceImpl()));
        instance.print("瞎几把搞");
    }
}

class ProxyServiceInvocationHandler implements InvocationHandler {
    private Object target;

    public ProxyServiceInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("==========JDK动态代理打印日志=========");
        method.invoke(target,args);
        System.out.println("==========JDK动态代理打印日志输出完毕=========");
        return null;
    }
}
