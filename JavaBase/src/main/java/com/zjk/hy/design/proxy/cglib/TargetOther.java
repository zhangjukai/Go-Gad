package com.zjk.hy.design.proxy.cglib;

/**
 * 目标对象2
 */
public class TargetOther {
    public String sayHello() {
        System.out.println("说中文");
        return "你好";
    }
}
