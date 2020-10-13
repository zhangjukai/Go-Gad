package com.zjk.hy.se.thread.notify;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProduceCustomer_04 {
    public static void main(String[] args) {
        PCcontainer2 container = new PCcontainer2();
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (int j = 0; j < 10; j++) {
                    container.put("p-"+j);
                }
            },"PUT-"+i).start();
            new Thread(()->{
                for (int j = 0; j < 10; j++) {
                    container.get();
                }
            },"GET-"+i).start();
        }
    }
}

class PCcontainer2 {
    private  LinkedList<String> list = new LinkedList();
    private  long MAX_SIZE = 5;
    private volatile  long count = 0;

    private ReentrantLock lock = new ReentrantLock();
    private Condition customer = lock.newCondition();
    private Condition produce = lock.newCondition();


    public void put(String e) {
        try {
            lock.lock();
            while (count==MAX_SIZE){
                try {
                    produce.await();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            list.add(e);
            count++;
            System.out.println(Thread.currentThread().getName()+"插入元素："+e);
            customer.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public  String get() {
        try{
            lock.lock();
            while (count == 0) {
                try {
                    customer.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String first = list.pollFirst();
            count--;
            System.out.println(Thread.currentThread().getName()+"取出元素："+first);
            produce.signalAll();
            return first;
        } finally {
            lock.unlock();
        }
    }

}