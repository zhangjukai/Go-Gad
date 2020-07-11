package com.zjk.hy.design.proxy.staticProxy;

public class Client {
    public static void main(String[] args) {
        Target target = new TargetImpl();
        Target proxy = new ProxyObject(target);
        System.out.println("main---代理对象执行结果：" + proxy.doSomeThing());
    }
}
