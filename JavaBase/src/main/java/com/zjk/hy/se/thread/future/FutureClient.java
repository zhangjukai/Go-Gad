package com.zjk.hy.se.thread.future;

public class FutureClient {
    public Data submit(String param){
        FutureData futureData = new FutureData();
        new Thread(()->{
            RealData realData = new RealData("11");
            futureData.setRealData(realData);
        }).start();
        return futureData;
    }
}
