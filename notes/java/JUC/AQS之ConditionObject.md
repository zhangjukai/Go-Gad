## ConditionObject

### Condition

java.util.concurrent.locks.Condition是JUC提供的与java的Object中wait/notify/notifyAll类似功能的一个接口，通过该接口线程可以在某个特定的条件下等待/唤醒。

与wait/notify/notifyAll操作需要获得对象监视器类似，一个Condition实例与某个互斥锁绑定，在此Condition实例进行等待/唤醒操作的调用也需要获得互斥锁，线程被唤醒需要再次获得锁，否则将继续等待。

而与原生的wait/notify/notifyAll等API不同的地方在于，JUC提供的Condition具有更丰富的功能，eg：等待可以响应/不响应中断，可设定超时时间或者等待到某个具体的时间点。**此外一把互斥锁可以绑定多个Condition，这意味着在同一把互斥锁上竞争的线程可以在不同条件下等待，唤醒时可以根据条件来唤醒线程，这是Object中的wait/notify/notifyAll不具备的机制**。

### ConditionObject

JUC中Condition接口的主要实现类是AQS的内部类ConditionObject，它内部维护了一个队列，我们可以称之为条件队列，在某个Condition上等待的线程被signal/signalAll后，ConditionObject会将对应的节点转移到外部类AQS的等待队列中，线程需要获取到AQS等待队列的锁，才可以继续恢复执行后续的用户代码。

大体流程：

await流程:

+ 创建节点加入到条件队列
+ 释放互斥锁
+ 只要没有转移到同步队列就阻塞(等待其他线程调用signal/signalAll或是被中断)
+ 重新获取互斥锁

signal流程:

+ 将队列中第一个节点转移到同步队列
+ 根据情况决定是否要唤醒对应线程

如果一个节点通过ConditionObject#await等方法调用初始化后，在被唤醒之后，会将状态切换至0，也即无状态，随后进入AQS的同步队列，此后就与一般的争锁无异了。