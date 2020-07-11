package com.zjk.hy.design.proxy.dynamic;

public class Client {
    public static void main(String[] args) {
        Target target = new TargetImpl();
        Target proxy = (Target) new ProxyObject(target).getProxyInstance();
        System.out.println("main---代理对象执行结果：" + proxy.doSomeThing());
    }
}
