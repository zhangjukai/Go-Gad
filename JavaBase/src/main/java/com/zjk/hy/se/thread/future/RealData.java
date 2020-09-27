package com.zjk.hy.se.thread.future;

public class RealData extends Data{
    private String result;
    public RealData(String param){
        System.out.println("开始执行............."+param);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("完成执行...........");
        this.result = "结束了....";
    }
    @Override
    public String getRequest() {
        return result;
    }
}
