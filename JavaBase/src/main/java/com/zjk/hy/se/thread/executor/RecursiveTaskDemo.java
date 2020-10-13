package com.zjk.hy.se.thread.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class RecursiveTaskDemo extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10000;
    private long start;
    private long end;

    public RecursiveTaskDemo(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;
        boolean canCompute = (end - start) < THRESHOLD;
        if(canCompute) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else { //分成100个小任务
            long step = (end + start) / 100;
            List<RecursiveTaskDemo> subTasks = new ArrayList<>();
            long pos = start;
            for (int i = 0; i < 100; i++) {
                long lastOne = pos + step;
                if(lastOne>end){
                    lastOne = end;
                }
                RecursiveTaskDemo demo = new RecursiveTaskDemo(pos, lastOne);
                pos+=step+1;
                subTasks.add(demo);
                demo.fork();
            }
            for (RecursiveTaskDemo demo:subTasks) {
                sum+=demo.join();
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        RecursiveTaskDemo taskDemo = new RecursiveTaskDemo(0, 200000L);
        ForkJoinTask<Long> result = forkJoinPool.submit(taskDemo);
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long sum = 0;
        for (long i = 1; i <= 200000L; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}
