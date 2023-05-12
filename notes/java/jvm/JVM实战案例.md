## 栈溢出

### 具体异常

```
com.alibaba.otter.canal.parse.exception.CanalParseException: java.lang.StackOverflowError
Caused by: java.lang.StackOverflowError: null
	at java.util.regex.Pattern$GroupHead.match(Pattern.java:4660) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Loop.match(Pattern.java:4787) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupTail.match(Pattern.java:4719) ~[na:1.8.0_202]
	at java.util.regex.Pattern$BranchConn.match(Pattern.java:4570) ~[na:1.8.0_202]
	at java.util.regex.Pattern$CharProperty.match(Pattern.java:3779) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Branch.match(Pattern.java:4606) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupHead.match(Pattern.java:4660) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Loop.match(Pattern.java:4787) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupTail.match(Pattern.java:4719) ~[na:1.8.0_202]
	at java.util.regex.Pattern$BranchConn.match(Pattern.java:4570) ~[na:1.8.0_202]
	at java.util.regex.Pattern$CharProperty.match(Pattern.java:3779) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Branch.match(Pattern.java:4606) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupHead.match(Pattern.java:4660) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Loop.match(Pattern.java:4787) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupTail.match(Pattern.java:4719) ~[na:1.8.0_202]
	at java.util.regex.Pattern$BranchConn.match(Pattern.java:4570) ~[na:1.8.0_202]
	at java.util.regex.Pattern$CharProperty.match(Pattern.java:3779) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Branch.match(Pattern.java:4606) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupHead.match(Pattern.java:4660) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Loop.match(Pattern.java:4787) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupTail.match(Pattern.java:4719) ~[na:1.8.0_202]
	at java.util.regex.Pattern$BranchConn.match(Pattern.java:4570) ~[na:1.8.0_202]
	at java.util.regex.Pattern$CharProperty.match(Pattern.java:3779) ~[na:1.8.0_202]
	at java.util.regex.Pattern$Branch.match(Pattern.java:4606) ~[na:1.8.0_202]
	at java.util.regex.Pattern$GroupHead.match(Pattern.java:4660) ~[na:1.8.0_202]
```

canal运行过程中出现的栈溢出异常，栈溢出一般出现在以下情况：

+ 局部数组过大
+ 递归层次太多
+ 死循环

### 解决办法：

合理设置-Xss参数

-Xss默认值是1024，即1m，在出现StackOverflowError异常的时候可以适当调大，比如：8m，16m,32m等；-Xss设置的每个线程可以使用的内存大小即栈的大小，也不能设置过大，如果设置过大，就会影响到创建线程栈的数量。

### JVM常见的参数

- **-Xms**:初始堆大小
- **-Xmx**:最大堆大小
- **-Xmn**:新生代大小



## 内存溢出

**jvm gc日志配置：**

```
-XX:+UseG1GC 
-XX:+HeapDumpOnOutOfMemoryError 
-XX:+PrintGCDetails 
-XX:+PrintGCDateStamps 
-Xloggc:../logs/gc.log
```

> HeapDumpOnOutOfMemoryError：会在内存溢出时将内存内容dump出来，会生成一个类似java_pid12052.hprof的文件

**dump文件分析：**

借助Eclipse MAT工具(MemoryAnalyzerTool)，分析dump文件

**jmap命令：**

查看活跃的对象：

```
jmap -histo:live 29527 | more
```

通过以上的方式获得一下信息：

![](.\res\canal_mat_1.png)

![](.\res\canal_mat_2.png)

![](.\res\canal_mat_3.png)

从上面的三张图我们可以看出，导致内存溢出的原因是DatabaseTableMeta下的MemoryTableMate下的SchemaRepository引起的，

对于SchemaRepository官网介绍如下：

> Druid SQL Parser内置了一个SchemaRepository，在内存中缓存SQL Schema信息，用于SQL语义解析中的ColumnResolve等操作。

到这儿就已经明白是怎么回事了，针对100w张表的情况下，全部放入进去，对内存的占用肯定特别大。

### Canal对ddl的缓存

<img src=".\res\MemoryTableMetaCode.png" style="float:left;" />

从源码上了解到，canal使用SchemaRepository来缓存所有的ddl，然后通过tableMetas(一个map)，来缓存每一个表的所有字段以及对应的数据类型等，如果要对内存进行优化，这两个是可以优化的点。

存在的问题：

**什么能够代替内存做缓存，并且性能不能有明显下降？**

+ redis：可以做缓存，但是同样是使用内存，成本没有变化
+ rocksdb：存磁盘，性能减少不是很大，灵感来源于flink

具体实现思路：

+ 将ddl存rocksdb，当需要的时候那单个表的ddl，临时创建SchemaRepository对象去解析ddl
+ tableMetas则直接存rocksdb，把rocksdb当做缓存来使用



