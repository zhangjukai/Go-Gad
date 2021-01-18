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

## 类加载器

### 加载过程

+ Loading
+ Linking
  + Verification-校验
  + Preparation-静态变量赋默认值
  + Resolution
+ Initialzing-初始化

### 类加载器

![](./res/ClassLoader.png)

为什么会有双亲委派机制？

主要为了安全，

### 编译器

+ -Xmixed 混合模式（默认）
+ -Xint 使用解释模式，启动很快，执行稍慢
+ -Xcomp，使用存编译模式，执行很快，启动很慢

