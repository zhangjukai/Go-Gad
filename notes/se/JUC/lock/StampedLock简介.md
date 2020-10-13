## StampedLock

### 简介

StampedLock是JDK1.8 引入的，也是一种读写锁，与ReentrantReadWriteLock相比优化了读锁的性能，提供了乐观读和悲观读两种读锁的功能，乐观读是无锁的，悲观读与写操作也是互斥的。

该类的设计初衷是作为一个内部工具类，用于辅助开发其它线程安全组件，用得好，该类可以提升系统性能，用不好，容易产生死锁和其它莫名其妙的问题。

StampedLock的主要特点概括一下，有以下几点：

1. 所有获取锁的方法，都返回一个邮戳（Stamp），Stamp为0表示获取失败，其余都表示成功；
2. 所有释放锁的方法，都需要一个邮戳（Stamp），这个Stamp必须是和成功获取锁时得到的Stamp一致；
3. StampedLock是不可重入的；（如果一个线程已经持有了写锁，再去获取写锁的话就会造成死锁）
4. StampedLock有三种访问模式：
   ①Reading（读模式）：功能和ReentrantReadWriteLock的读锁类似
   ②Writing（写模式）：功能和ReentrantReadWriteLock的写锁类似
   ③Optimistic reading（乐观读模式）：这是一种优化的读模式。
5. StampedLock支持读锁和写锁的相互转换
   我们知道RRW中，当线程获取到写锁后，可以降级为读锁，但是读锁是不能直接升级为写锁的。
   StampedLock提供了读锁和写锁相互转换的功能，使得该类支持更多的应用场景。
6. 无论写锁还是读锁，都不支持Conditon等待

ReentrantReadWriteLock写锁是互斥的

+ 读-写互斥
+ 写-写互斥

StampedLock

+ 乐观读并不会阻塞写锁
+ 悲观读-写互斥

乐观读存在的问题：

+ 不支持条件变量
+ 不支持重入

详细分析参照：https://segmentfault.com/a/1190000015808032