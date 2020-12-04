package com.zjk.hy.se.proxy;

public class DefaultProxyServiceImpl implements ProxyService {
    @Override
    public void print(String msg) {
        System.out.println("DefaultProxyService输出内容："+msg);
    }
}
