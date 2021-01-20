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
+ -Xcomp，使用存编译模式，执行很快，启动很慢

### JVM Runtime Data  Area( 运行时数据区)

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

JVM可以直接访问的内核空间的内存（OS管理的内存）

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

熟悉GC常用算法，熟悉常见垃圾收集器，具有实际JVM调优实战经验

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

+ 拷贝算法（coping）

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

+ 新生代=Eden+2个suvivor区

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

+ Serial 年轻代 串行回收

+ Parallel Scavenge 年轻代 并行回收

+ ParNew 年轻代  配合CMS的并行回收器

+ SerialOld

+ ParallelOld

+ Concurrent MarkSweep 老年代 并发的，垃圾回收和应用程序同时运行，降低STW的时间（200ms）

  要使用CMS只能手动指定，CMS既然是MarkSweep，就一定会有碎片化的问题，碎片达到一定程度，CMS的老年代分配不下对象时，使用SerialOld进行老年代回收

+ G1(10ms)

  算法：三色标记+SATB

+ ZGC(1ms) pk C++

  算法：ColoredPointers + LoadBarrier

+ Shenandoah

  算法：ColorPointers + WriteBarrier

+ Eplison 

+ PS 和 PN区别的延伸阅读：
  https://docs.oracle.com/en/java/javase/13/gctuning/ergonomics.html

#### 垃圾回收器跟内存大小的关系

+ Serial 几十兆
+ Parallel Scavenge 上百兆-几个G
+ CMS-20G
+ G1 上百G
+ ZGC 4T-16T（JDK13）

JDK1.8默认的垃圾回收器是Parallel Scavenge+ParallelOld



























