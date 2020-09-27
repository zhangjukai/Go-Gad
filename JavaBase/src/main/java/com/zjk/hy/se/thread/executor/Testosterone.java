package com.zjk.hy.se.thread.executor;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Testosterone {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ArrayList list = new ArrayList();
        for (int i = 0; i < 100000; i++) {
            executorService.submit(()->list.add(new Random().nextInt(100)));
        }
        System.out.println(list.size());
        executorService.shutdown();
    }
}
