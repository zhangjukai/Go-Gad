package com.zjk.hy.se.thread.notify;

import java.util.stream.Stream;

public class ProduceCustomer_02 {
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
                LOCK.notifyAll();
            }
        }
    }

    // 消费
    public void custom() {
        synchronized (LOCK) {
            if (isProduce) {
                System.out.println("C-> " + i);
                LOCK.notifyAll();
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
        ProduceCustomer_02 produceCustomer_01 = new ProduceCustomer_02();
        Stream.of("P1", "P2", "P3").forEach(n ->
                new Thread(n) {
                    @Override
                    public void run() {
                        while (true) {
                            produceCustomer_01.produce();
                        }
                    }
                }.start());

        Stream.of("C1", "C2", "C3").forEach(n ->
                new Thread(n) {
                    @Override
                    public void run() {
                        while (true) {
                            produceCustomer_01.custom();
                        }
                    }
                }.start());
    }

}
