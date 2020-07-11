package com.zjk.hy.design.proxy.staticProxy;

/**
 * 目标对象
 */
public class TargetImpl implements Target {

    @Override
    public String doSomeThing() {
        System.out.println("say hello ");
        return "hello";
    }
}
