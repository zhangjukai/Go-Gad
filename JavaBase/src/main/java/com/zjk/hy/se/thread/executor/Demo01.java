package com.zjk.hy.se.thread.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Demo01 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(() -> {
            System.out.println("线程执行中");
            return "卧槽，无情";
        });
        System.out.println("aaaaaaaaaa");
        try {
            System.out.println("返回结果："+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
