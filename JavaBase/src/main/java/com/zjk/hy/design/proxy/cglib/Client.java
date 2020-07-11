package com.zjk.hy.design.proxy.cglib;

public class Client {
    public static void main(String[] args) {
        Target target = new Target();
        Target proxyInstance = (Target) new ProxyObject(target).getProxyInstance();
        String s = proxyInstance.doSomeThing();
        System.out.println("执行结果："+s);
        System.out.println("======================");
        TargetOther targetOther = new TargetOther();
        TargetOther targetOtherPro = (TargetOther)new ProxyObject(targetOther).getProxyInstance();
        String s1 = targetOtherPro.sayHello();
        System.out.println("执行结果："+s1);
    }
}
