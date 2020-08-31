## 简介

CountDownLatch，Java JUC中提供的一种通用的同步工具，允许一个或多个线程等待直到在其他线程中执行的一组操作完成的同步工具，其实也可以实现多个线程等待一个线程。CountDownLatch位于`java.util.concurrent`包下。

## 实现原理

CountDownLatch底层是通过AQS来实现相关功能的，内部类Sync继承自AQS，使用的是AQS的共享功能，初始化传入一个计数，设置为AQS的state，每次调用countDown()方法，state的值就会减一，直到state等于0.

## 具体方法

### 构造方法

```java
// 以给定的数-count设置为锁存器的计数,构造一个CountDownLatch对象
public CountDownLatch(int count);
```

### 具体方法

```java
// 阻塞当前线程直到计数为0，可被打断
public void await() throws InterruptedException;
// 阻塞给定的时间，超时或被打断就被唤醒
public boolean await(long timeout, TimeUnit unit);
// 减少锁存器的计数，如果计数达到零，释放所有等待的线程。 
public void countDown();
// 返回当前计数
public long getCount();
```

## Demo

```java
public class CountDownLatchTest {
    public static ExecutorService executorService = Executors.newScheduledThreadPool(5);
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            executorService.submit(()->{
                System.out.println(Thread.currentThread().getName()+"----执行任务中");
                countDownLatch.countDown();
            });
        }

        try {
            System.out.println("开始等待任务执行");
            countDownLatch.await();
            System.out.println("任务执行完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## 源码分析

### 构造方法

```java
// 构造方法
public CountDownLatch(int count) {
    if (count < 0) throw new IllegalArgumentException("count < 0");
    // 调用内部类Sync的构造方法
    this.sync = new Sync(count);
}
Sync(int count) {
    // 设置AQS的state值
    setState(count);
}
```

### await方法

```java
public void await() throws InterruptedException {
    // AQS方法
    sync.acquireSharedInterruptibly(1);
}
public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    if (tryAcquireShared(arg) < 0)
        doAcquireSharedInterruptibly(arg);
}
// 重写AQS的tryAcquireShared-共享的模式下获取状态
// 如果state为0，返回1，否则返回-1
// 返回 1   #获取锁成功，直接返回 线程可继续操作
// 返回 -1.  #获取锁失败 ，开始进入队列中排队等待。接下来就会继续按照 AQS acquireSharedInterruptibly 方法中的逻辑，执行 doAcquireSharedInterruptibly(int arg)
protected int tryAcquireShared(int acquires) {
    return (getState() == 0) ? 1 : -1;
}
// AQS方法
private void doAcquireSharedInterruptibly(int arg)
    throws InterruptedException {
    // 添加到同步队列
    final Node node = addWaiter(Node.SHARED);
    boolean failed = true;
    try {
        for (;;) { // 自旋
            final Node p = node.predecessor();
            if (p == head) {
                // 如果是头结点尝试获取锁
                int r = tryAcquireShared(arg);
                if (r >= 0) { // 锁获取成功
                    setHeadAndPropagate(node, r);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
            }
            // park前状态判断，自旋一次
            if (shouldParkAfterFailedAcquire(p, node) &&
                // 线程中断
                parkAndCheckInterrupt())
                throw new InterruptedException();
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

### countDown方法

```java
public void countDown() {
    sync.releaseShared(1);
}
public final boolean releaseShared(int arg) {
    // 修改state并判断state是不是0
    if (tryReleaseShared(arg)) {
        // 唤醒线程
        doReleaseShared();
        return true;
    }
    return false;
}
protected boolean tryReleaseShared(int releases) {
    // Decrement count; signal when transition to zero
    for (;;) {
        int c = getState();
        if (c == 0) 
            // 如果state为0 ，说明计数已经全部减完了，直接返回false，此次调用countDown不作任何操作
            return false;
        // state减一
        int nextc = c-1;
        // cas设置state的值
        if (compareAndSetState(c, nextc))
            // state更新成功后，state==0 返回true
            return nextc == 0;
    }
}
private void doReleaseShared() {
    for (;;) {
        Node h = head;
        if (h != null && h != tail) {
            int ws = h.waitStatus;
            if (ws == Node.SIGNAL) {
                if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                    continue;            // loop to recheck cases
                // 唤醒线程，唤醒的是当前线程的下一个非删除节点
                unparkSuccessor(h);
            }
            else if (ws == 0 &&
                     !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                continue;                // loop on failed CAS
        }
        if (h == head)                   // loop if head changed
            break;
    }
}
```

