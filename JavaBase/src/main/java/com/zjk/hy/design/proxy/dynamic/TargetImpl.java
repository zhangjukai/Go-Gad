package com.zjk.hy.design.proxy.dynamic;

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
