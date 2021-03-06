# JVM简介

jvm跨语言的平台，java跨平台的语言，jvm是一种规范，官方文档：

https://docs.oracle.com/javase/specs/index.html

java执行流程：

<img src="./res/java从编译到执行.png" style="float:left;" />

## JVM常见的实现

+ Hotspot

+ Jrockit(BEA)

+ J9(IBM)

+ Microsoft VM

+ TaobaoVM

  Hotspot深度定制版

+ LiquidVM

  直接针对硬件

+ azul zing

  最新垃圾回收，业界标杆

+ GraalVM
  oracle新开发的一款虚拟机，有可能会替代Hotspot，Spring最新版本有对这个进行支持

JDK内容：

<img src="./res/JDK.png" style="float:left;" />

## Class文件格式

二进制字节流

### 类加载器

![](./res/ClassLoader.png)

为什么会有双亲委派机制？

主要为了安全，

如何打破双亲委派机制？

重写loadClass方法

### 加载过程

+ Loading

+ Linking

  + Verification-校验

    验证文件是否符合JVM规范

  + Preparation-准备，预备

    静态变量赋默认值

  + Resolution-解析

    将类、方法、属性等符号引用解析为直接引用，常量池中的各种符号引用解析为指针，偏移量等内存地址的直接引用

+ Initialzing-初始化

### 编译器

+ -Xmixed 混合模式（默认）
+ -Xint 使用解释模式，启动很快，执行稍慢
+ -Xcomp，使用纯编译模式，执行很快，启动很慢

### JVM Runtime Data  Area( 运行时数据区)

![](./res/JVM Runtime Data Area.png)



#### PC 程序计数器

存放指令的位置，<span style="color:red">线程私有</span>

#### JVM Stack 虚拟机栈

<span style="color:red">线程私有</span>

Frame - 每个方法对应一个栈帧

+ 局部变量表（Local Variable Table）

+ 操作数栈（Operand Stack）

  对于long的处理（store and load），多数虚拟机的实现都是原子的，没必要加volatile

+ 动态链接（Dynamic Linking）

  https://blog.csdn.net/qq_41813060/article/details/88379473 

  动态链接是一个将符号引用解析为直接引用的过程

+ return address

```java
public class Test01 {
    public static void main(String[] args) {
        int i = 8;
         i = i++;
        //i = ++i;
        System.out.println(i);
    }
}
// 对应字节码
 0 bipush 8
 2 istore_1
 3 iload_1
 4 iinc 1 by 1
 7 istore_1
 8 getstatic #2 <java/lang/System.out>
11 iload_1
12 invokevirtual #3 <java/io/PrintStream.println>
15 return
```

```java
public class Test01 {
    public static void main(String[] args) {
        int i = 8;
         //i = i++;
        i = ++i;
        System.out.println(i);
    }
}
// 对应字节码
 0 bipush 8
 2 istore_1
 3 iinc 1 by 1
 6 iload_1
 7 istore_1
 8 getstatic #2 <java/lang/System.out>
11 iload_1
12 invokevirtual #3 <java/io/PrintStream.println>
15 return
```

#### Native Method Stack

本地方法栈，<span style="color:red">线程私有</span>

#### Method Area

方法区，method area是jvm的规范，具体的虚拟机可以有不同的实现

+ Perm Space（<1.8）方法区，Hotspot-永久代
+ Meta Space(>=1.8) 元空间

两者的区别：

| 方法区     | 大小                         | 是否FGC       | 对字符串的处理               |
| ---------- | ---------------------------- | ------------- | ---------------------------- |
| Perm Space | 启动时指定，不能变           | FGC不会清理   | 字符串常量位于Perm Space     |
| Meta Space | 如果不设定，最大就是物理内存 | 会触发FGC清理 | 字符串常量位于堆空间（Heap） |

如何证明JDK1.7字符串常量位于Perm，而JDK1.8位于堆空间(Heap)？

提示：结合GC，一直创建字符串常量，观察堆和Meta Space的情况

#### Heap

堆空间

#### Runtime Constant Pool

运行时常量池

#### Direct Memory

直接内存(堆外内存)，JVM可以直接访问的内核空间的内存（OS管理的内存），JDK1.5之后可以通过未公开的unsafe和NIO包下的ByteBuffer操作堆外内存

NIO，提高效率，实现zero copy

### JVM Instructions (JVM指令集)

#### 分类

+ 基于寄存器的指令集

+ 基于栈的指令集

  Hotspot中的LocalVariable Table=JVM中的寄存器

#### 常用指令

+ store：将操作数栈最上面的值弹出，然后存储到（赋值）局部变量表中指定下标的局部变量上
+ load：从局部变量表中加载指定的内容，然后放入操作数栈中
+ pop：从操作数栈中弹出
+ mul：
+ sub：
+ invoke：执行方法
  + InvokeStatic：执行静态方法
  + InvokeVirtual：执行一般方法，支持多态
  + InvokeInterface：执行接口方法
  + InvokeSpecial：执行可以直接定位，不需要多态的方法，比如：private方法，构造方法等
  + InvokeDynamic：lambda表达式或者反射其他的动态语言Scala、kotlin或者CGLib ASM，动态产生的class，会用到该指令

### GC

熟悉GC常用算法，熟悉常见垃圾收集器，具有JVM调优实战经验

#### 如何定位垃圾

+ 引用计数法（Reference Count）

  存在循环引用的问题

+ 根可达算法（Root Searching）

  哪些对象是根？线程栈变量、静态变量、常量池、JNI指针

  深入理解Java虚拟机书中，

  > JVM  stack、native method stack、run-time constant pool，static reference，In method Area 、clazz

#### 常见的垃圾回收算法

+ 标记清除（mark sweep）

  位置不连续、产生碎片，效率偏低（两遍扫描，标记-清除两个阶段）；

  算法相对简单，存活对象比较多的情况下效率较高

+ 拷贝（复制）算法（coping）

  适用于存活对象较少的情况，只扫描一次，效率提高，没有碎片；

  浪费空间，移动复制对象，需要调整对象引用

+ 标记压缩算法（mark compact）

  没有碎片，方便对象分配

  扫描两次，需要移动对象，效率偏低

#### JVM内存分代模型

用于分代垃圾回收算法，堆内存逻辑分区如下：

![](./res/堆内存逻辑分区.png)



+ 部分垃圾回收器使用的模型

  除Epsilon、ZGC、Shenandoah之外的GC都是使用逻辑分代模型

  G1是逻辑分代、物理不分代，除此之外的垃圾回收器不仅逻辑分代，同时也是物理分代

+ 新生代+老年代+永久代（1.7）Perm Generation/元空间(1.8)Meta space

  + 永久代/元空间  - Class
  + 永久代必须指定大小限制，元空间可以设置、也可以不设置（最大空间受限于物理内存大小）
  + 字符串常量 1.7永久代，1.8堆空间
  + MethodArea是JVM规范，一个逻辑概念，其具体实现是永久代、元空间

+ 新生代=Eden+2个survivor区

  + YGC回收之后，大多数对象会被回收，活着的进入s0
  + 再次YGC，活着的对象eden+s0 -> s1
  + 再次YGC，eden+s1 -> s0
  + 年龄足够 -> 老年代（15 CMS 6）
  + s区装不下 -> 老年代

+ 老年代

  + 顽固分子
  + 老年代满了-FGC（Full GC）

+ GC Tuning

  + 尽量减少FGC

  + MinorGC=YGC

    年轻代空间耗尽时触发

  + MajorGC=FGC  FullGC

    在老年代无法继续分配空间时触发，新生代老年代同时进行回收

#### 栈上分配

+ 线程私有小对象
+ 无逃逸
+ 支持标量替换

<span style="color:red">无需调整</span>

#### 线程本地分配

TLAB—Thread Local Allocation Buffer

并发场景中，解决如何安全地分配内存的一种方案，每个线程在Java堆中预先分配一小块内存，然后再给对象分配内存的时候，直接在自己这块"私有"内存中分配，当这部分区域用完之后，再分配新的"私有"内存。

+ 占用eden，默认1%
+ 多线程的时候不用竞争eden就可以申请空间，提高效率
+ 小对象

<span style="color:red">无需调整</span>

#### 对象何时进入老年代

+ 超过XX:MaxTenuringThreshold指定的次数（YGC）

  + Parallel Scavenge 15
  + CMS 6
  + G1 15

  为什么最大是15，因为对象头中的markword里面，年龄代占4个字符，

+ 动态年龄

  + s1->s2超过50%，把年龄最大的放入old区

#### 常见的垃圾回收器

JDK诞生时就有了Serial，为了提高效率诞生了Parallel Scavenge；jDK1.4版本后期引入了CMS，为了配合CMS，由诞生了ParNew，CMS是里程碑式的GC，它开启了并发回收的过程，但是CMS问题还是比较多，因此目前没有任何一个JDK版本默认的垃圾回收器都是CMS的。

为什么会有并发垃圾回收器，是因为<span style="color:red">STW</span>（stop the word）

+ Serial 

  年轻代 串行回收，所有收集器里额外内存消耗（Memory Footprint）最小的  ，基于标记-复制算法实现

+ Serial Old 

  老年代、串行化回收，Mark-Compact 标记整理算法,也是stw:暂停所有线程进行垃圾回收;

+ Parallel Scavenge 

  年轻代、并行回收(STW)、基于标记-复制算法实现，目标是达到一个可控制的吞吐量，Parallel Scavenge提供了两个参数用于精准控制吞吐量，

  + -XX: MaxGCPauseMillis   控制最大垃圾收集停顿时间
  + -XX: GCTimeRatio 直接设置吞吐量大小

+ ParNew 年轻代  配合CMS的并行回收器

  ParNew收集器实质上是Serial收集器的多线程并行版本， 除了同时使用多条线程进行垃圾收集之
  外， 其余的行为包括Serial收集器可用的所有控制参数（例如： -XX： SurvivorRatio、 -XX：
  PretenureSizeThreshold、 -XX： HandlePromotionFailure等） 、 收集算法、 Stop The World、 对象分配规
  则、 回收策略等都与Serial收集器完全一致，  

+ ParallelOld

  老年代，并行收集，基于标记-整理算法实现 ，JDK1.6开始提供

+ Concurrent MarkSweep 

  老年代、并发收集、以最短停顿时间为目标，提高响应时间，基于标记-清除算法

  包括：初始标记（stw）、并发标记、重新标记（stw），并发清除

  存在的问题：

  + 对资源敏感（因为并发）
  + 无法处理浮动垃圾（浮动垃圾：并发清理阶段应用程序还在执行，此时产生的垃圾），可能导致Concurrent Mode Failure，只能使用SerialOld进行老年代回收（一次更久的FullGC）
  + 空间碎片太多（标记-清除算法），还有空间，大对象却没法分配

  垃圾回收和应用程序同时运行，降低STW的时间（200ms）

+ G1(10ms)

  算法：三色标记+SATB

  JDK9 G1宣告取代Parallel Scavenge加Parallel Old组合  

  采用的是标记复制算法

+ ZGC(1ms) pk C++

  算法：ColoredPointers + LoadBarrier

+ Shenandoah

  算法：ColorPointers + WriteBarrier

+ Eplison 

+ PS 和 PN区别的延伸阅读：
  https://docs.oracle.com/en/java/javase/13/gctuning/ergonomics.html

<span style="color:red">JDK8默认使用的是parallel Scavenge+Serial Old</span>

<span style="color:red">JDK14默认使用的是G1</span>

#### 垃圾回收器跟内存大小的关系

+ Serial 几十兆
+ Parallel Scavenge 上百兆-几个G
+ CMS-20G
+ G1 上百G
+ ZGC 4T-16T（JDK13）

JDK1.8默认的垃圾回收器是Parallel Scavenge+ParallelOld

#### 常见垃圾回收器组合参数设定（JDK1.8）

+ -XX:+UseSerialGC 

  等同于Serial New+Serial Old；适用于小型程序，默认情况下不会是这种选项，HotSpot会根据计算机以及配置和JDK版本自动选择收集器

+ -XX:+UseParNewGC

  =ParNew+SerialOld；这个组合已经很少用了，在某些版本中已经废弃，JDK9废除了这个指令

+ -XX:+UseConcMarkSweepGC = ParNew + CMS + Serial Old
+ -XX:+UseParallelGC = Parallel Scavenge + Parallel Old（1.8默认）【PS+SerialOld】
+ -XX:+UseParallelOldGC =  Parallel Scavenge + Parallel Old
+ -XX:+UseG1GC = G1

* Linux中没找到默认GC的查看方法，而windows中会打印UseParallelGC 
  * java +XX:+PrintCommandLineFlags -version
  * 通过GC的日志来分辨

* Linux下1.8版本默认的垃圾回收器到底是什么？

  * 1.8.0_181 默认（看不出来）Copy MarkCompact
  * 1.8.0_222 默认 PS + PO 

### JVM调优

#### JVM常用命令行参数

命令行参数参考：https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html

HotSpot参数分类：

+ 标准：-开头，所有的HotSpot都支持
+ 非标准：-X开头，特定版本HotSpot支持的特定命令
+ 不稳定：-XX开头，下个版本可能取消

memory leak：内存泄漏

out of memory：内存溢出

#### 示例演示

```java
import java.util.List;
import java.util.LinkedList;

public class HelloGC {
    public static void main(String[] args) {
        System.out.println("HelloGC!");
        List list = new LinkedList();
        for(;;) {
            byte[] b = new byte[1024*1024];
            list.add(b);
        }
    }
}
```

+ 打印jvm默认配置信息

  参数：-XX:+PrintCommandLineFlags 

  JDK1.8-jvm参数：

  > -XX:InitialHeapSize=67100544 
  >
  > -XX:MaxHeapSize=1073608704 
  >
  > -XX:+PrintCommandLineFlags 
  >
  > -XX:+UseCompressedClassPointers 
  >
  > -XX:+UseCompressedOops 
  >
  > -XX:-UseLargePagesIndividualAllocation 
  >
  > -XX:+UseParallelGC 

  JDK14-jvm参数：

  > // 并发优化线程(Remenbered Set)数量
  >
  > -XX:G1ConcRefinementThreads=4 
  >
  > -XX:GCDrainStackTargetSize=64 
  >
  > -XX:InitialHeapSize=67100544 
  >
  > -XX:MaxHeapSize=1073608704 
  >
  > -XX:MinHeapSize=6815736 
  >
  > -XX:+PrintCommandLineFlags 
  >
  > -XX:ReservedCodeCacheSize=251658240 
  >
  > -XX:+SegmentedCodeCache 
  >
  > -XX:+UseCompressedClassPointers 
  >
  > -XX:+UseCompressedOops 
  >
  > -XX:+UseG1GC 
  >
  > -XX:-UseLargePagesIndividualAllocation

+ 设置堆大小

  参数：-Xmn10M -Xms40M -Xmx60M -XX:+PrintCommandLineFlags -XX:+PrintGC

  JDK1.8-jvm参数：

  > -XX:InitialHeapSize=41943040 
  >
  > -XX:MaxHeapSize=62914560 
  >
  > -XX:MaxNewSize=10485760 
  >
  > -XX:NewSize=10485760 
  >
  > -XX:+PrintCommandLineFlags 
  >
  > -XX:+PrintGC 
  >
  > -XX:+UseCompressedClassPointers 
  >
  > -XX:+UseCompressedOops 
  >
  > -XX:-UseLargePagesIndividualAllocation 
  >
  > -XX:+UseParallelGC

  Parallel Scavenge+Serial Old的GC日志：

  > [GC (Allocation Failure)  7840K->5397K(39936K), 0.0071157 secs]
  > [GC (Allocation Failure)  12722K->12598K(39936K), 0.0107224 secs]
  > [GC (Allocation Failure)  19917K->19766K(39936K), 0.0197214 secs]
  > [GC (Allocation Failure)  27172K->26933K(39936K), 0.0096329 secs]
  > [Full GC (Ergonomics)  26933K->26575K(53248K), 0.0179287 secs]
  > [GC (Allocation Failure)  33954K->33839K(52736K), 0.0098533 secs]
  > [GC (Allocation Failure)  41356K->41040K(49664K), 0.0090939 secs]
  > [Full GC (Ergonomics)  41040K->40886K(56832K), 0.0241719 secs]
  > [GC (Allocation Failure)  44116K->44054K(58368K), 0.0041669 secs]
  > [Full GC (Ergonomics)  44054K->43959K(58368K), 0.0161410 secs]
  > [GC (Allocation Failure)  47155K->47095K(58368K), 0.0065456 secs]
  > [Full GC (Ergonomics)  47095K->47036K(58368K), 0.0134711 secs]
  > [GC (Allocation Failure)  50204K->50204K(58368K), 0.0028560 secs]
  > [Full GC (Ergonomics)  50204K->50110K(58368K), 0.0162234 secs]
  > [Full GC (Ergonomics)  53263K->53182K(58368K), 0.0147719 secs]
  > [Full GC (Ergonomics)  54254K->54231K(58368K), 0.0145046 secs]
  > [Full GC (Allocation Failure)  54231K->54212K(58368K), 0.0339220 secs]

  日志说明：

  ![](./res/PSGCLog.png)

  

  对空间dump信息：

  > Heap
  >  PSYoungGen      total 7168K, used 3255K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
  >   eden space 4096K, 79% used [0x00000000ff600000,0x00000000ff92de08,0x00000000ffa00000)
  >   from space 3072K, 0% used [0x00000000ffa00000,0x00000000ffa00000,0x00000000ffd00000)
  >   to   space 3072K, 0% used [0x00000000ffd00000,0x00000000ffd00000,0x0000000100000000)
  >  ParOldGen       total 51200K, used 51165K [0x00000000fc400000, 0x00000000ff600000, 0x00000000ff600000)
  >   object space 51200K, 99% used [0x00000000fc400000,0x00000000ff5f77d8,0x00000000ff600000)
  >  Metaspace       used 3668K, capacity 4536K, committed 4864K, reserved 1056768K
  >   class space    used 397K, capacity 428K, committed 512K, reserved 1048576K

  ![](./res/PSGCDumpInfo.png)

  JDK14-jvm参数：

  > -XX:G1ConcRefinementThreads=4 
  >
  > -XX:GCDrainStackTargetSize=64 
  >
  > -XX:InitialHeapSize=41943040 
  >
  > -XX:MaxHeapSize=62914560 
  >
  > -XX:MaxNewSize=10485760 
  >
  > -XX:MinHeapSize=41943040 
  >
  > -XX:NewSize=10485760 
  >
  > -XX:+PrintCommandLineFlags 
  >
  > -XX:+PrintGC 
  >
  > -XX:ReservedCodeCacheSize=251658240 
  >
  > -XX:+SegmentedCodeCache 
  >
  > -XX:+UseCompressedClassPointers 
  >
  > -XX:+UseCompressedOops 
  >
  > -XX:+UseG1GC 
  >
  > -XX:-UseLargePagesIndividualAllocation

G1GC日志信息：

> [0.025s][info ][gc] Using G1
> [0.077s][info ][gc] Periodic GC disabled
> HelloGC!
> [0.363s][info ][gc] GC(0) Pause Young (Concurrent Start) (G1 Humongous Allocation) 24M->22M(40M) 9.236ms
> [0.363s][info ][gc] GC(1) Concurrent Cycle
> [0.370s][info ][gc] GC(1) Pause Remark 30M->30M(54M) 0.879ms
> [0.399s][info ][gc] GC(1) Pause Cleanup 40M->40M(54M) 0.066ms
> [0.402s][info ][gc] GC(1) Concurrent Cycle 39.154ms
> [0.407s][info ][gc] GC(2) Pause Young (Concurrent Start) (G1 Humongous Allocation) 44M->44M(54M) 3.643ms
> [0.407s][info ][gc] GC(3) Concurrent Cycle
> [0.412s][info ][gc] GC(3) Pause Remark 56M->56M(60M) 0.905ms
> [0.417s][info ][gc] GC(4) Pause Young (Normal) (G1 Humongous Allocation) 56M->56M(60M) 1.744ms
> [0.419s][info ][gc] GC(5) Pause Young (Normal) (G1 Evacuation Pause) 58M->58M(60M) 1.280ms
> [0.422s][info ][gc] GC(3) Pause Cleanup 58M->58M(60M) 0.063ms
> [0.424s][info ][gc] GC(6) Pause Young (Normal) (G1 Humongous Allocation) 58M->58M(60M) 1.200ms
> [0.430s][info ][gc] GC(7) Pause Full (G1 Humongous Allocation) 58M->57M(60M) 5.517ms
> [0.430s][info ][gc] GC(3) Concurrent Cycle 22.943ms
> [0.431s][info ][gc] GC(8) Pause Young (Normal) (G1 Evacuation Pause) 59M->59M(60M) 0.596ms
> [0.436s][info ][gc] GC(9) Pause Full (G1 Evacuation Pause) 59M->59M(60M) 5.520ms
> [0.441s][info ][gc] GC(10) Pause Full (G1 Evacuation Pause) 59M->59M(60M) 4.591ms
> [0.442s][info ][gc] GC(11) Pause Young (Concurrent Start) (G1 Evacuation Pause) 59M->59M(60M) 0.619ms
> [0.442s][info ][gc] GC(13) Concurrent Cycle
> [0.448s][info ][gc] GC(12) Pause Full (G1 Evacuation Pause) 59M->59M(60M) 5.730ms
> [0.452s][info ][gc] GC(14) Pause Full (G1 Evacuation Pause) 59M->59M(60M) 4.380ms
> [0.453s][info ][gc] GC(15) Pause Young (Normal) (G1 Evacuation Pause) 59M->59M(60M) 0.444ms
> [0.458s][info ][gc] GC(16) Pause Full (G1 Evacuation Pause) 59M->59M(60M) 4.568ms
> [0.463s][info ][gc] GC(17) Pause Full (G1 Evacuation Pause) 59M->59M(60M) 5.288ms
> [0.467s][info ][gc] GC(18) Pause Young (Normal) (G1 Evacuation Pause) 59M->59M(60M) 3.423ms
> [0.475s][info ][gc] GC(19) Pause Full (G1 Evacuation Pause) 59M->5M(40M) 8.073ms
> [0.475s][info ][gc] GC(13) Concurrent Cycle 33.329ms

#### 调优前的基础概念

+ 吞吐量

  用户代码执行时间/(用户代码执行时间+垃圾回收时间)

+ 响应时间

  STW越短，响应时间越好

所谓调优，首先确定，追求啥，吞吐量优先，还会响应时间优先，还是在满足一定的响应时间的情况下，要求达到多大的吞吐量：

+ 吞吐量优先

  科学计算、数据挖掘、thrput等，一般采用PS+PO

+ 响应时间

  网站、GUI、API等，JDK1.8 推荐G1

#### 什么是调优

+ 根据需求进行JVM规划和预调优
+ 优化运行JVM环境（慢、卡顿）
+ 解决JVM运行过程中出现的各种问题(OOM)

#### 从规划开始

+ 从业务场景出发，要落实到具体的业务

+ 无监控，不调优（GC日志，压力测试等，调优前要能发现问题，调优后能看到效果）

+ 调优步骤：

  + 熟悉业务步骤（没有最好的垃圾收集器，只有最合适的）

    要求响应时间、停顿时间【CMS G1 ZGC】（需要给用户作响应）

    要求吞吐量【PS】

  + 选择垃圾回收器组合

  + 计算内存需求（结合业务、经验值去估算，最后进行压测）

  + 选定CPU（越高越好）

  + 设定年代大小，升级年龄

  + 设置日志参数

    > -Xlogg /usr/local/java/pro/jvm/logs/gc-%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=20M -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause

    日志文件不能搞成一个，可以按照日期规定文件大小，或者每天生产一个文件

  + 观察日志情况

+ 案例

  + 垂直电商，最高每日百万订单，处理订单系统需要什么样的服务器配置？

    这个问题比较业余，因为很多不同的服务器配置都能支撑(1.5G 16G)，具体的思路是：判断每日高峰阶段，比如晚上8-10点间产生绝大部分订单，那么一个小时对应多少，从而计算出每秒的订单量；另外找高峰阶段的峰值，假如是1000订单/秒，因此需要考虑到这个峰值去设计，其实也是一个经验值。

    如果非要计算：一个订单产生多少内存？512K*1000 = 500M 内存

    专业一点的问法是：要求响应时间100ms

    那么就需要根据设计好后，进行压测！

    CPU的算率和实际内存大小有很大的关系

  + 12306大规模抢票应该如何支撑

    12306应该是中国并发量最大的秒杀网站，号称并发量100W，今天双11，淘宝好像是45W

    CDN-LVS-NGINX-业务系统-每台机器1w并发（单机10k问题），100台机器

#### 实际优化

1. 文件管理系统(从磁盘提取文档到内存)，服务器 64位，2G内存，JVM参数？比较缓慢，系统升级，调大堆内存过后反而更卡，

   + 为什么会慢？

     很多用户浏览数据，很多数据Load到内存，内存不足，频繁GC，STW时间长，响应时间变慢

   + 为什么会卡

     内存越大，FGC时间越长

   + 怎么优化

     Parallel Scavenger + Parallel Old 调整为 PN+ CMS 或者G1

2. 系统CPU经常100%，如何调优

   CPU 100% 那么一定有线程在占用系统资源

   + 找出哪个进程CPU高（top）
   + 该进程中的哪个线程CPU高（top-Hp）
   + 导出该线程的堆栈（jstack）
   + 查找哪个方法（栈帧）消耗时间(jstack)
   + 工作线程占比高 | 垃圾回收线程占比高

3. 系统内存飙升，如何查找问题

   + 导出堆内存(jmap)
   + 分析（jhat jvisualvm mat jprofiler）

4. 如何监控JVM

   jstat jvisualvm jprifiler arthas top

## JVM 运行中的问题

一般是运维团队首先收到报警信息（CPU Memory）

### 认识常用工具

#### 测试代码

```java
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 从数据库中读取信用数据，套用模型，并把结果进行记录和传输
 */

public class FullGC_Problem01 {

    private static class CardInfo {
        BigDecimal price = new BigDecimal(0.0);
        String name = "张三";
        int age = 5;
        Date birthdate = new Date();

        public void m() {}
    }

    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) throws Exception {
        executor.setMaximumPoolSize(50);

        for (;;){
            modelFit();
            Thread.sleep(100);
        }
    }

    private static void modelFit(){
        List<CardInfo> taskList = getAllCardInfo();
        taskList.forEach(info -> {
            // do something
            executor.scheduleWithFixedDelay(() -> {
                //do sth with info
                info.m();

            }, 2, 3, TimeUnit.SECONDS);
        });
    }

    private static List<CardInfo> getAllCardInfo(){
        List<CardInfo> taskList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CardInfo ci = new CardInfo();
            taskList.add(ci);
        }

        return taskList;
    }
}
```

#### 测试过程

+ 命令

  > java -Xms200M -Xmx200M -XX:+PrintGC FullGC_Problem01

+ top命令，观察到：内存不断增长，CPU占用率居高不下

+ top -Hp PID(进程ID)，查看进程中线程的情况：哪个线程CPU和内存占用率高

+ jps定位具体java进程

  jstack定位线程状态，重点关注：WAITINGBLOCKED

  为什么阿里规范里规定，线程的名称，线程池的名称都要写有意义的名称

  怎么样自定义线程池里的线程名称（自定义ThreadFactory）

+ jinfo pid

+ jstat -gc 动态观察GC的情况，阅读GC日志（发现频繁GC）/arthas观察/jconsole/jvisualVM/Jprofiler(最好用)

  jstat -gc 4655 500;每个500个毫秒打印GC的情况

  怎么定位OOM问题？

  + 已经上线的系统不用图形界面，用<span style="color:red">cmdline arthas</span>
  + 图形界面用于测试阶段，压测观察

+ jmap - histo 4655 | head -20 ,查找有多少对象产生

+ jmap -dump：format=b,file=xxx pid

  线上系统，内存特别大，jmap执行期间会对进程产生很大的影响，甚至卡顿（电商不适合）

  + 设定参数HeapDump,OOM的时候会自动产生堆转存文件
  + 很多服务器备份（高可用），停掉一台服务器对业务影响不大
  + 在线定位（小公司用不到）

+ java -Xms20M -Xmx20M -XX:+UseParallelGC -XX:_HeapDumpOnOutOfMemoryError FullGC_Problem01

+ 使用MAT、jhat、jvisualvm进行dump文件分析

  https://www.cnblogs.com/baihuitestsoftware/articles/6406271.html 
  jhat -J-mx512M xxx.dump
  http://192.168.17.11:7000
  拉到最后：找到对应链接
  可以使用OQL查找特定问题对象

+ 找到代码的问题

#### jconsole远程连接

> java -Djava.rmi.server.hostname=192.168.211.11 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=11111 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false XXX

## 重点垃圾收集器

### CMS

#### CMS 存在的两个问题

+ 内存碎片化——MarkSweep 标记清除算法

+ 当无法分配内存时，会进行FullGC，是使用serial old垃圾收集器进行单线程回收，效率低，STW

  解决办法：降低触发CMS的阈值  

  jdk1.8 -XX:CMSInitiatingOccupancyFraction 默认值是-1，通过如下公式可以计算：

  > (100-MinHeapFreeRatio)+(CMSTriggerRatio*MinHeapFreeRatio/100)

  jdk1.8（100-40）+ (80*40/100)=92

  <span style="color:red">如果频繁发生Serial Old卡顿，应该将这个值调小，使其频繁的进行CMS回收，减少或者避免FullGC</span>

#### CMS阶段

+ 初始化标记阶段

  initial mark，通过GC Roots找到根对象，该阶段会导致STW，但是时间很短；

+ 并发标记

  concurrent mark，最耗时的阶段

+ 重新标记

  remark，标记上个阶段并发标记过程中产生的垃圾对象，会导致STW

+ 并发清理

### G1

G1是一种服务端应用程序适用的垃圾收集器，目标是用在多核、大内存的机器上，它在大多数情况下可以实现指定GC暂停时间，同时还能保持较高的吞吐量

#### 特点

+ 内存区域不是固定的Eden或者old

+ 并发收集

+ 压缩空闲空间不会延长GC的暂停时间

+ 更容易预测GC的暂停时间

+ 适用不需要实现很高的吞吐量的场景

+ 新、老年代的比例是动态的，建议不要手工指定

  因为这是G1预测停顿时间的基准

#### 基本概念

+ CSet(Collection Set)

  一组可被回收的分区的集合；在CSet中存活的数据会在GC过程中被移动到另一个可用分区，CSet中的分区可以来自Eden区，Survivor区或者老年代，CSet会占用不到整个堆空间的1%。

+ <span style="color:red">RSet(Remembered Set)</span>

  每一个Region中，都有一个Hash表，记录了其他Region中的对象到本Region的引用，RSet的价值在于：使得垃圾收集器不需要扫描整个堆，找到谁引用了当前分区中的对象，只需要扫描RSet即可。

  G1能够高速回收的关键，详细到对象级别，占用的空间会多一点点

  <span style="color:red">由于RSet的存在，那么每次给对象赋引用的时候，就得做一些额外的操作，（在RSet中做一些额外的记录，在GC中被称为写屏障，这个写屏障不是内存屏障）</span>

#### G1何时触发GC

+ YGC

  Eden空间不足，多线程并行执行

+ FGC

  Old空间不足，System.gc();

#### MixedGC

相当于一个CMS，有一个参数(InitiatingHeapOccupancyPercent)默认值是45%，意思是当堆内存达到45%时，启动MixedGC，

MixedGC执行流程：

+ 初始标记 (initial-mark) STW
+ 并发标记
+ 最终标记（重新标记） STW
+ 并行筛选回收 

模拟MixedGC执行的场景：

> [GC pause (G1 Evacuation Pause) (young) (initial-mark), 0.1025634 secs]
>    [Parallel Time: 102.3 ms, GC Workers: 1]
>       [GC Worker Start (ms):  762704.1]
>       [Ext Root Scanning (ms):  0.4]
>       [Update RS (ms):  19.4]
>          [Processed Buffers:  56]
>       [Scan RS (ms):  6.0]
>       [Code Root Scanning (ms):  0.0]
>       [Object Copy (ms):  76.5]
>       [Termination (ms):  0.0]
>          [Termination Attempts:  1]
>       [GC Worker Other (ms):  0.0]
>       [GC Worker Total (ms):  102.2]
>       [GC Worker End (ms):  762806.3]
>    [Code Root Fixup: 0.0 ms]
>    [Code Root Purge: 0.0 ms]
>    [Clear CT: 0.0 ms]
>    [Other: 0.3 ms]
>       [Choose CSet: 0.0 ms]
>       [Ref Proc: 0.0 ms]
>       [Ref Enq: 0.0 ms]
>       [Redirty Cards: 0.1 ms]
>       [Humongous Register: 0.0 ms]
>       [Humongous Reclaim: 0.0 ms]
>       [Free CSet: 0.0 ms]
>    [Eden: 17.0M(17.0M)->0.0B(12.0M) Survivors: 4096.0K->3072.0K Heap: 168.7M(200.0M)->159.2M(200.0M)]
>  [Times: user=0.03 sys=0.07, real=0.10 secs] 
> [GC concurrent-root-region-scan-start]
> [GC concurrent-root-region-scan-end, 0.0130782 secs]
> [GC concurrent-mark-start]
> [GC concurrent-mark-end, 1.6027589 secs]
> [GC remark [Finalize Marking, 0.0004751 secs] [GC ref-proc, 0.0000756 secs] [Unloading, 0.0021195 secs], 0.0050491 secs]
>  [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [GC cleanup 159M->157M(200M), 0.0008976 secs]
>  [Times: user=0.01 sys=0.00, real=0.00 secs] 
> [GC concurrent-cleanup-start]
> [GC concurrent-cleanup-end, 0.0000092 secs]

<span style="color:red">从整个GC日志来看，当到达阈值过后触发MixedGC，流程如下：</span>

+ initial-mark(Root Scanning)
+ concurrent-root-region-scan
+ concurrent-mark
+ remark（Finalize Marking）
+ cleanup
+ concurrent-cleanup

网上有文章说jdk1.8使用G1不会触发MixedGC，因为InitiatingHeapOccupancyPercent参数有Bug，纯属扯淡

#### G1的FullGC

java 10以前是串行FullGC，之后是并行FullGC

<span style="color:red">G1的调优应尽量避免FullGC</span>

## 重点算法

### Card Table

由于GC时，需要扫描整个Old区，效率非常低，所以JVM设计了CardTable，

如果一个Old区的CardTable中有对象指向Y区（年轻代），就将它设为Dirty，下次扫描时，只需要扫描Dirty Card，

在结构上，Card Table用BitMap来实现

### 三色标记算法

#### 算法概念

+ 白色：未被标记的对象
+ 灰色：自身被标记，成员变量未被标记
+ 黑色：自身和成员变量均已标记完成

![](./res/三色标记算法.png)

漏标的情况：

![](./res/三色标记算法-漏标.png)

#### 漏标的解决办法

要解决漏标的问题，只要打破漏标必须同时满足的两个条件之一即可,解决方法有以下两种：

+ 增量更新（incremental update）

  关注引用的增加，把黑色重新标记为灰色，下次重新扫描属性（这样已经扫描过的Files也会扫描）。

  CMS使用的是这种方式

+ SATB-关注引用的删除（snapshot at the beginning）

  当B->D的引用消失时，会把这个引用推到GC的堆栈，保证D还能被GC扫描到

  G1使用的是这种方式，

  为什么G1使用这种方式？

  +  因为采用增量更新的方式的话，会导致已经扫描过的对象的Files，需要重新扫描，Files可能很多，或者链路很深。

  + <span style="color:red">SATB与RSet配合，浑然天成</span>

    G1使用的是Region，并且每个Region中(RSet)都保存了其他对象对本Region中对象的引用。这样就可以直接扫描D对象所在的RSet，看有没有对象引用D对象，如果没有就可以直接回收D对象

  