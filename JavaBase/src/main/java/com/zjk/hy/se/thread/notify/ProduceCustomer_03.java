package com.zjk.hy.se.thread.notify;

import java.util.LinkedList;

public class ProduceCustomer_03 {
    public static void main(String[] args) {
        PCcontainer container = new PCcontainer();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                container.put("aaa");
            },"PUT-"+i);
            new Thread(()->{
                container.get();
            },"GET-"+i).start();
        }
    }
}

class PCcontainer {
    private  LinkedList<String> list = new LinkedList();
    private  long MAX_SIZE = 5;
    private volatile  long count = 0;
    public synchronized void put(String e) {
        while (count==MAX_SIZE){
            try {
                this.wait();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        list.add(e);
        count++;
        System.out.println(Thread.currentThread().getName()+"插入元素："+e);
        this.notifyAll();
    }

    public synchronized String get() {
        while (count == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String first = list.getFirst();
        count--;
        System.out.println(Thread.currentThread().getName()+"取出元素："+first);
        this.notifyAll();
        return first;
    }

}