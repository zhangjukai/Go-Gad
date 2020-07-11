package com.zjk.hy.design.proxy.cglib;

/**
 * 目标对象
 */
public class Target {
    public String doSomeThing() {
        System.out.println("say hello ");
        return "hello";
    }
}


