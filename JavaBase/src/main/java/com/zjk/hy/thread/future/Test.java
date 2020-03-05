package com.zjk.hy.thread.future;

public class Test {
    public static void main(String[] args) {
        FutureClient futureClient = new FutureClient();
        Data data = futureClient.submit("2222");
        System.out.println("main.数据发送成功");
        System.out.println("main.执行其他任务");
        String result = data.getRequest();
        System.out.println("main.获取结果："+result);
        System.out.println("main.继续执行");
    }
}
