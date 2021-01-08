# Mysql调优

## 整体介绍

### MySQL架构图

![](./res/Mysql架构.png)

### 存储引擎：InnoDB与MyISAM的区别

|              | InnoDB                               | MyISAM                                         |
| :----------- | ------------------------------------ | ---------------------------------------------- |
| 存储方式     | .frm(存储表定义) 和 .ibd(数据和索引) | .frm(存储表定义) .MYD(存储数据) .MYI(存储索引) |
| 索引类型     | 聚簇索引                             | 非聚簇索引                                     |
| 事务支持     | 是                                   | 否                                             |
| 支持表锁     | 是                                   | 是                                             |
| 支持行所     | 是                                   | 否                                             |
| 支持外键     | 是                                   | 否                                             |
| 支持全文索引 | 是(5.6以后)                          | 是                                             |
| 适用场景     | 大量insert、update、delete           | 大量select                                     |

### Mysql日志

#### 二进制日志—Binlog

Binlog是记录所有**数据库表结构变更以及表数据修改**的二进制日志。Binlog不会记录select和show这类操作的日志，因为这类操作并没有修改数据。

Binlog日志包含两类文件：

+ 二进制索引文件
+ 二进制日志文件

从上面的Mysql架构图来看，Binlog位于Mysql Server阶段，**线上环境一定要开启binlog**。

#### 事务日志—Redo Log、undo Log

Redo Log和undo Log，从Mysql架构图来看，属于存储引擎，只有InnoDB 存储引擎才有。

##### Redo Log

Redo Log指的是在事务中对任何数据的操作，都将最新的数据备份到Redo log日志文件中，是为了**实现事务的持久性**而出现的产物。

当系统崩溃重启后，可以根据Redo Log的内容，将所有数据恢复到最新状态。

数据写入流程如下：

<img src="./res/mysql数据写入磁盘流程.png" style="float:left;" />

上图实际展示了三种写入策略，可以通过设置**innodb_flush_log_at_trx_commit**参数来调整，InnoDB中默认innodb_flush_log_at_trx_commit=1

##### undo Log

undo Log指的是事务开始前，在操作任何数据之前，首先将要操作的数据备份到undo Log中，是为了**实现事务的原子性**，在InnoDB中undo Log还用来实现多版本并发控制（简称：MVCC）。

**undo Log是逻辑日志，可以理解为：**

+ 当delete一条数据时，undo Log中会记录一条对应的insert语句记录
+ 当insert一条数据时，undo Log中会记录一条对应的delete语句
+ 当update一条数据时，undo Log中会记录一条相反的update记录

### ACID

+ 原子性（Atomicity）

  在一个事务中的所有操作，作为一个整体不可分割，要么全部执行成功，要么全部失败，<span style="color:red">通过undo Log实现</span>。

+ 一致性(Consistency)

  事务的执行结果必须是数据库从一个一致性状态到另一个一致性状态，比如A账户给B账户转账，两个账户的总金额应该保持不变。

+ 隔离性(Isolation)

  并发执行的事务不会相互影响，对数据库的影响和他们串行执行时一样

+ 持久性(Durability)

  事务一旦提交，其对数据库的更新就是持久的，<span style="color:red">通过Redo Log实现</span>。

### mysql事务隔离级别

事务具有隔离性，理论上来说事务之间的执行不应该相互影响，其对数据库的影响应该和串行执行时一样。

完全的隔离性会导致系统并发性能很低，降低对资源的利用率，因而实际上对隔离性的要求会有所放宽，这也会一定程度上造成数据库一致性的降低、

具体的隔离级别，从低到高依次是：

+ 读未提交（READ UNCOMMITTED） 对事物的读取没有任何限制，不推荐
+ 读已提交（READ COMMITTED）
+ 可重复读（REPEATABLE READ）
+ 串行化（SERIALIZABLE） 性能最低

mysql默认使用的是可重复读，orcoal默认使用的是读已提交

对于不同的事务级别，会导致不同的问题

+ 脏读：读取到其他事务还没有commit的数据
+ 不可重复读：在一个事务中，对一组数据多次读取，读取的结果不相同（读取到了其他事务commit的数据）
+ 幻读：和不可重复读类上，只是角度不同，在插入数据时，读到其他事务commit的数据

对应情况如下表：

| 事务隔离级别                 | 脏读 | 不可重复读 | 幻读 |
| ---------------------------- | ---- | ---------- | ---- |
| 读未提交（READ UNCOMMITTED） | √    | √          | √    |
| 读已提交（REDA COMMITTED）   |      | √          | √    |
| 可重复读（REPEATABLE READ）  |      |            | √    |
| 串行化（SERIALIZABLE）       |      |            |      |

**如何设置事务级别：**

```mysql
set session transaction isolation level read uncommitted;
```

session：指定设置的作用域为当前会话

global：全局设置

### 数据库范式

+ 1NF：字段不可分
+ 2NF：有主键，非主键字段依赖主键
+ 3NF：非主键字段不能相互依赖

### 一些命令

+ SHOW PROFILES;

  可以查看每条命令执行的时间

+ SHOW PROFILE for QUERY queryId;

  查询某一条sql执行的各个阶段消耗的时间，queryId为SHOW PROFILES;查询结果中的Query_id；

> The [`SHOW PROFILE`](https://dev.mysql.com/doc/refman/8.0/en/show-profile.html) and [`SHOW PROFILES`](https://dev.mysql.com/doc/refman/8.0/en/show-profiles.html) statements are deprecated; expect them to be removed in a future MySQL release. Use the [Performance Schema](https://dev.mysql.com/doc/refman/8.0/en/performance-schema.html) instead; see [Section 27.19.1, “Query Profiling Using Performance Schema”](https://dev.mysql.com/doc/refman/8.0/en/performance-schema-query-profiling.html).

命令文档：https://dev.mysql.com/doc/refman/8.0/en/show-profile.html

+ Performance Schema

  另外单开记录
  
+ show processlist;

  查看有多少客户端链接了mysql

### 数据库连接池

https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98

## schema与数据类型优化

### 数据类型优化

#### 更小的通常更好

应该尽量使用可以正常存储数据的<span style="color:red">最小数据类型</span>，更小的数据类型通常更快，因为他们占用更少的磁盘，内存和CPU缓存，并且处理时需要的CPU周期更少。

#### 简单就好

简单数据类型的操作通常需要更少的CPU周期，eg：

+ 整型比字符型操作代价更低，因为字符串的校对规则比整型更复杂

+ 使用Mysql自建类型而不是字符串来储存日期和时间

  测试情况：

  ```mysql
  CREATE TABLE `typetest1` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `querytime` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
  CREATE TABLE `typetest2` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `querytime` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
  SET PROFILE = 1;
  SELECT * from typetest1;
  SHOW PROFILES;
  SELECT * from typetest2;
  SHOW PROFILES;
  ```

  最后得到的结果是：`0.000328`、`0.00036225 `，如果加上时间条件得到的结果是：`0.0004685`，`0.00035375  `,由此看来查询所耗时间差距还是特别大的。

#### 用整型存储IP地址

mysql提供了ip与整型相互转换的函数，如下：

```mysql
SELECT INET_ATON('192.168.11.125');
SELECT INET_NTOA(3232238461);
```

#### 尽量避免null

如果查询中包含为NULL的列，对于mysql来说很难优化，因为可为null的列，使得索引、索引统计和值的比较都更加复杂。

坦白来说，通常情况下null的列改为not null到来的性能提升比较小，所以没有必要将所有的schema进行修改，但是应该尽量避免设计成null的列。

#### 优化细则

##### 整数类型

可以使用的几种数据类型：TINYINT(8)、SMALLINT(16)、MEDIUMINT(24)、INT(32)、BIGINT(64)；尽量使用满足需求的最小数据类型。

##### 字符和字符串类型

+ varchar

1. 可变长度，根据数据长度决定磁盘占用空间
2. varchar(n)，n小于等于255，额外使用一个字节保存长度，如果大于255，额外使用两个字节保存长度。
3. varchar(5)和varchar(255)，保存相同的内容，硬盘存储空间相同，但内存占用空间是指定的大小(不相同)。
4. varchar在5.6之前变更长度，或者从255变更到255以上的时候，都会导致锁表。
5. 应用场景
   + 存储数据波动较大的数据
   + 字符串很少更新的场景，每次更新后都会重新算，并使用额外空间来保存字符串长度
   + 适合保存多字节字符，如：汉字、特殊字符等

+ char

1. 固定长度的字符串，最大长度：255
2. 会自动删除末尾的空格
3. 检索效率、写效率会比varchar高，以空间换时间
4. 应用场景
   + 存储长度波动不大的数据，如：MD5摘要
   + 存储短字符串，经常更新的字符串

##### BLOB和TEXT类型

mysql把每个BLOB和TEXT值当作一个独立的对象处理，两者都是为了存储很大数据而设计的，分别采用二进制和字符串方式存储。

实际中不建议使用，当遇到数据很大的时候可以存文件，数据库里面记录文件地址。

##### 日期时间类型

mysql中对于时间类型有：date、time、datetime、timestamp等类型。在实际使用中，不要使用字符串类型来存储日期时间数据，另外使用int存储日期时间不如使用timestamp类型。<span style="color:red">因为日期时间类型占用的存储空间更小，mysql本身还提供了丰富处理函数，能够方便的对日期进行比较和计算</span>。

| 类型      | 占用空间 | 存储范围                                                     | 时区支持             |
| --------- | -------- | ------------------------------------------------------------ | -------------------- |
| datetime  | 8个字节  | 可精确到毫秒(范围大)                                         | 与时区无关           |
| timestamp | 4个字节  | 时间范围：1970-01-01到2038-01-19，可精确到秒，采用整型存储   | 依赖数据库设置的时区 |
| date      | 3个字节  | 保存1000-01-01到9999-12-31之间的日期，精确到日，mysql提供大量日期函数 |                      |

##### 使用枚举代替字符串类型

有时候可以使用枚举代替常用的字符串类型，mysql存储枚举类型非常紧凑，会根据列表值得数据压缩到一两个字节中，mysql内部会将每个值在列表中的位置保存为整数，且在表的.frm文件中保存“数字-字符串”的映射关系。

### 字符集的选择

对于mysql字符集的选择，这儿主要针对UTF-8做下补充，标准的UTF-8字符集编码是可以用1-4个字节去编码21位字符，只支持Unicode中 [基本多文本平面](https://zh.wikipedia.org/wiki/Unicode字符平面映射)（U 0000至U FFFF），有部分内容不支持。

mysql 5.5.3之后，增加了utf8mb4,是utf8的超集，能够使用4个字节存储更多的字符。参考，https://blog.csdn.net/qq_17555933/article/details/101445526

