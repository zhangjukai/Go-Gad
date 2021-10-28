## CountDownLatch

CountDownLatch类位于java.util.concurrent包下 ，是一个同步工具类，它允许一个或多个线程一直等待，直到其它线程执行完毕。

eg:

```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ",子线程开始执行...");
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + ",子线程结束执行...");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ",子线程开始执行...");
                countDownLatch.countDown();//计数器值每次减去1
                System.out.println(Thread.currentThread().getName() + ",子线程结束执行...");
            }
        }).start();
        countDownLatch.await();// 減去为0,恢复任务继续执行
        System.out.println("两个子线程执行完毕....");
        System.out.println("主线程继续执行.....");
        for (int i = 0; i <10; i++) {
            System.out.println("main,i:"+i);
        }
    }
}
```

### CountDownLatch原理分析

+ CountDownLatch内部依赖Sync实现，而Sync继承AQS

+ CountDownLatch主要内容：

  ```java
  // 1.通过创建对象指定需要执行的线程个数（计数器）
  CountDownLatch countDown = new CountDownLatch(10);
  // 2.使当前线程进入阻塞队列，将资源让给其它线程执行，直到计数器为0
  countDown.await();
  // 3.计数器-1
  countDown.countDown();
  ```

### 源码分析

#### **CountDownLatch的静态内部类 Sync** 

```java
// CountDownLatch的同步控制器
// 使用AQS的State来记录线程数
// 继承自AQS
private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

    	// 构造函数，调用了AQS的setState方法来设置同步线程数
        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();
        }

    	// 重写AQS的tryAcquireShared-共享的模式下获取状态
    	// 如果state为0，返回1，否则返回-1
    	// 返回 1   #获取锁成功，直接返回 线程可继续操作
    	// 返回 -1.  #获取锁失败 ，开始进入队列中排队等待。接下来就会继续按照 AQS acquireSharedInterruptibly 方法中的逻辑，执行 doAcquireSharedInterruptibly(int arg)
        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }
		// 重写AQS的tryReleaseShared，共享的模式下释放状态 
        protected boolean tryReleaseShared(int releases) {
            // Decrement count; signal when transition to zero
            // 通过自旋的方式
            for (;;) {
                // 获取锁状态
                int c = getState();
                if (c == 0)
                    // 计数器为0的时候 说明释放锁成功 直接返回
                    return false;
                // 将计数器减一 
                int nextc = c-1;
                // 调用AQS的compareAndSetState方法，使用CAS更新计算器的值
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }
    }
```

```java
// 构造函数，通过调用Sync的构造函数直到线程数
public CountDownLatch(int count) {
    if (count < 0) throw new IllegalArgumentException("count < 0");
    this.sync = new Sync(count);
}

```

```java
// 等待计数器为0,计数器不为0时会一直阻塞，除非线程中断
public void await() throws InterruptedException {
    // 调用的是AQS的方式
    sync.acquireSharedInterruptibly(1);
}
```

```java
// 释放状态 
public void countDown() {
    sync.releaseShared(1);
}
```

