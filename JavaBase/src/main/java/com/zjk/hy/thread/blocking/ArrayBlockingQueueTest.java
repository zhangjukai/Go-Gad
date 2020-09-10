package com.zjk.hy.thread.blocking;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {
    public static void main(String[] args) {

        ArrayBlockingQueue queue = new ArrayBlockingQueue(4,true, Arrays.asList(1,2,3,4,5,6));
        Object[] objects = queue.toArray();
        for (int i = 0; i < objects.length; i++) {
            System.out.println(objects[i]);
        }
    }
}
