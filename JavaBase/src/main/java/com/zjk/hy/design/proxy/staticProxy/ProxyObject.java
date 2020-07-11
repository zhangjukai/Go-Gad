package com.zjk.hy.design.proxy.staticProxy;

/**
 * 代理对象
 */
public class ProxyObject implements Target {

    private Target target;

    public ProxyObject(Target target) {
        this.target = target;
    }

    @Override
    public String doSomeThing() {
        System.out.println("开始代理 完成某些操作......");
        String res = target.doSomeThing();
        System.out.println("代理结束 ..... ");
        return res;
    }
}
