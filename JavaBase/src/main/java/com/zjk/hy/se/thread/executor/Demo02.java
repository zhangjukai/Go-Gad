package com.zjk.hy.se.thread.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Demo02 {
    public static void main(String[] args) {
        FutureTask<String> future = new FutureTask<>(() -> {
            System.out.println("执行future中的任务");
            return "future";
        });
        new Thread(future).start();
        try {
            System.out.println("future执行结果："+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
