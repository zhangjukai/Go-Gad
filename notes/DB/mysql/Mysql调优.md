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

上图实际展示了三种写入策略，可以通过设置**innodb_flush_log_at_trx_commit**参数来调整，InnoDB中默认innodb_flush_log_at_trx_commit=0

##### undo Log

undo Log指的是事务开始前，在操作任何数据之前，首先将要操作的数据备份到undo Log中，是为了**实现事务的原子性**，在InnoDB中undo Log还用来实现多版本并发控制（简称：MVCC）。

**undo Log是逻辑日志，可以理解为：**

+ 当delete一条数据时，undo Log中会记录一条对应的insert语句记录
+ 当insert一条数据时，undo Log中会记录一条对应的delete语句
+ 当update一条数据时，undo Log中会记录一条相反的update记录

### ACID

+ 原子性（Atomicity）

  在一个事务中的所有操作，作为一个整体不可分割，要么全部执行成功，要么全部失败，通过undo Log实现。

+ 一致性(Consistency)

  事务的执行结果必须是数据库从一个一致性状态到另一个一致性状态，比如A账户给B账户转账，两个账户的总金额应该保持不变。

+ 隔离性(Isolation)

  并发执行的事务不会相互影响，对数据库的影响和他们串行执行时一样

+ 持久性(Durability)

  事务一旦提交，其对数据库的更新就是持久的，通过Redo Log实现。

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

+ 更小的通常更好

  应该尽量使用可以正常存储数据的**最小数据类型**，更小的数据类型通常更快，因为他们占用更少的磁盘，内存和CPU缓存，并且处理时需要的CPU周期更少。

+ 简单就好

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

  + 用整型存储IP地址

    mysql提供了ip与整型相互转换的函数，如下：

    ```mysql
    SELECT INET_ATON('192.168.11.125');
    SELECT INET_NTOA(3232238461);
    ```

+ 尽量避免null

  如果查询中包含为NULL的列，对于mysql来说很难优化，因为可为null的列，使得索引、索引统计和值的比较都更加复杂。

  坦白来说，通常情况下null的列改为not null到来的性能提升比较小，所以没有必要将所有的schema进行修改，但是应该尽量避免设计成null的列。

