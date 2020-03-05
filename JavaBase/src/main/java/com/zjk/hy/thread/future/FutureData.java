package com.zjk.hy.thread.future;

public class FutureData extends Data {
    private Boolean FLAG = false;
    private RealData realData;

    @Override
    public synchronized String getRequest() {
        while (!FLAG){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.getRequest();
    }

    public synchronized void setRealData(RealData realData){
        if (FLAG) {
            return ;
        }
        this.realData = realData;
        FLAG = true;
        notify();
    }
}
