# ReentrantLock简介

​	一个可重入互斥Lock，具有与使用Synchronized隐式监视锁相同的基本行为和语义。同时ReentrantLock功能更加强大，使用更加灵活，具有公平锁、非公平锁、等待可中断、绑定多个条件等Synchronized不具备的功能。

**备注：** 不过自从JDK1.6对Synchronized优化过后，Synchronized的效率与ReentrantLock的效率以及差不多了，建议在能使用Synchronized的地方都采用Synchronized的，因为其根据简单、简洁。

## 构造方法

```java
// 创建一个 ReentrantLock的实例,默认创建的是非公平锁
ReentrantLock()
// 根据给定的公平政策创建一个 ReentrantLock的实例
// fair=true表示采用公平锁
ReentrantLock(boolean fair) 
```

## 方法介绍

```java  
// 获取当前线程对锁持有的次数，如果当前线程不持有该锁，则返回0
public int getHoldCount()
// 查询当前线程是否持有锁
// 类似Synchronized中使用Thread.holdsLock(Object)
public boolean isHeldByCurrentThread()
// 判断是否有线程持有该锁
public boolean isLocked()
// 判断此锁是否为公平锁
public final boolean isFair()
// 返回当前拥有此锁的线程，如果不拥有，则返回null
protected Thread getOwner()
// 查询是否有线程正在等待获取此锁
public final boolean hasQueuedThreads()
// 查询给定线程是否等待获取此锁
public final boolean hasQueuedThread(Thread thread)
// 返回等待获取此锁的线程数的估计
public final int getQueueLength()
// 返回包含可能正在等待获取此锁的线程的集合
protected Collection<Thread> getQueuedThreads()
// 查询任何线程是否等待与此锁相关联的给定条件
public boolean hasWaiters(Condition condition)
// 返回与此锁相关联的给定条件等待的线程数的估计
public int getWaitQueueLength(Condition condition)
// 返回包含可能在与此锁相关联的给定条件下等待的线程的集合
protected Collection<Thread> getWaitingThreads(Condition condition)
```

## 简单使用

```java
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    static ReentrantLock lock = new ReentrantLock(true);
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new ThreadDemo(i));
            thread.setName("thread"+i);
            thread.start();
        }
    }

    static class ThreadDemo implements  Runnable {
        Integer id;
        public ThreadDemo(Integer id) {
            this.id = id;
        }
        @Override
        public void run() {
            try {
                TimeUnit.MICROSECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 2; i++) {
                lock.lock();
                System.out.println("HoldsLock:"+Thread.holdsLock(lock));
                System.out.println("获得锁的线程："+id);
                System.out.println(lock.hasQueuedThreads());
                //System.out.println("holdCount:"+lock.getHoldCount());
                lock.unlock();
            }
        }
    }
}

```

```java
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {
    int size;
    ReentrantLock lock = new ReentrantLock();
    LinkedList<E> list = new LinkedList<>();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    public MyBlockingQueue(int size){
        this.size = size;
    }

    public void enqueue(E e) throws InterruptedException {
        lock.lock();
        try{
            while (list.size() == size)
                notFull.await();
            list.add(e);
            System.out.println("入队："+e);
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public E dequeue() throws InterruptedException {
        E e;
        lock.lock();
        try{
            while (list.size() ==0){
                notEmpty.await();
            }
            e = list.removeFirst();
            System.out.println("出队："+e);
            notFull.signal();
        }finally {
            lock.unlock();
        }
        return e;
    }

    public static void main(String[] args) throws InterruptedException {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(4);
        for (int i = 0; i < 10; i++) {
            int data = i;
            new Thread(()->{
                try {
                    queue.enqueue(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for(int i=0;i<10;i++){
            new Thread(()->{
                try {
                    Integer data = queue.dequeue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

