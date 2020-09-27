package com.zjk.hy.se.thread.notify;

public class ProduceCustomer_01 {
    private int i = 1;
    final private Object LOCK = new Object();
    private volatile boolean isProduce = false;

    // 生产
    public void produce() {
        synchronized (LOCK) {
            if (isProduce) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                i++;
                System.out.println("P-> " + i);
                isProduce = true;
                LOCK.notify();
            }
        }
    }

    // 消费
    public void custom() {
        synchronized (LOCK) {
            if (isProduce) {
                System.out.println("C-> " + i);
                LOCK.notify();
                isProduce = false;
            } else {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public static void main(String[] args) {
        ProduceCustomer_01 produceCustomer_01 = new ProduceCustomer_01();
        new Thread(() -> {
            while (true){
                produceCustomer_01.produce();
            }
        }).start();
        new Thread(() -> {
            while (true){
                produceCustomer_01.custom();
            }
        }).start();
    }

}
