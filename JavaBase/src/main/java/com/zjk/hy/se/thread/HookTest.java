package com.zjk.hy.se.thread;

/**
 * @Author zhangjukai
 * @Date 2021-11-02 14:48
 * @Description
 */
public class HookTest {
    public static void main(String[] args) {
        System.out.println("starting working.......");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("执行钩子程序");
            }
        });
        System.out.println("working ending");
    }
}
