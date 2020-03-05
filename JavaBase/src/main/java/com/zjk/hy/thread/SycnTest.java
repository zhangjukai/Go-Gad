package com.zjk.hy.thread;

/**
 * @author zjk
 * @date 2019/5/28 - 21:40
 */
public class SycnTest {
    public static void main(String[] args) {
        SycnTest01 sycnTest01 = new SycnTest01();
        Thread thread1 = new Thread(sycnTest01);
        Thread thread2 = new Thread(sycnTest01);
        thread1.start();
        thread2.start();
    }
}

class SycnTest01 extends Thread {

    public static String obj = "key";

    @Override
    public void run() {
        synchronized(obj){
            print();
        }
    }

    public void print(){
        synchronized(obj){
            for (int i = 0; i < 10; i++) {
                System.out.println("i:"+i);
            }
        }
    }

}
