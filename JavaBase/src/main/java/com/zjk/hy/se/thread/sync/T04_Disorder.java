package com.zjk.hy.se.thread.sync;

/**
 * 指令重排序验证,在这个案例代码中，可能出现场景如下：
 * 1.one线程执行完毕后执行other 线程结果：a=1,x=0;b=1,y=1; xy值为：01组合
 * 2.other线程执行完毕后执行one 线程结果：a=1,x=1;b=1,y=0; xy值为：10组合
 * 3.other线程和one线程同时执行 线程结果：a=1,x=1;b=1,y=1; xy值为：11组合
 * 按照正常逻辑，不可能出现 xy值为00的组合
 *
 *
 * 出现这种情况，只有一种可能，出现乱序执行了。
 * @author LYs
 *
 */
public class T04_Disorder {

    private static int x = 0,y=0;
    private static int a= 0,b=0;

    public static void main(String[] args) throws Exception {
        int i=0;
        for (; ;) {
            i++;
            x=0;y=0;
            a=0;b=0;
            Thread one = new Thread(new Runnable() {

                @Override
                public void run() {
                    a=1;
                    x=b;
                }
            });
            Thread other = new Thread(new Runnable() {

                @Override
                public void run() {
                    b=1;
                    y=a;
                }
            });

            one.start();other.start();
            one.join();other.join();
            String result = "第"+i+"次("+x+","+y+")";
            if (x==0 && y==0) {
                System.err.println(result);
                break;
            }
        }
    }
}