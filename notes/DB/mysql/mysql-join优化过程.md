# mysql-join优化过程

先记录一条跑不动的SQL，如下：

```mysql
select a.channel,a.id as agentId,a.createdOn,COUNT(DISTINCT b.id) as inAppCount,
COUNT(DISTINCT c.id)  as inActivityCount,COUNT(DISTINCT d.id)  as shareVisitCount
from (select id,channel,createdOn from mjj_agent where status = 'VLD' ) as a
LEFT JOIN (select id,agentId from mjj_agentactivelog where createdOn like '2020-12-28%') as b on (b.agentId = a.id)
LEFT JOIN (select createdBy,id from mjj_activity_fission_in_log where createdOn like '2020-12-28%')  as c on (c.createdBy = a.id)
LEFT JOIN (select inviterId,id from mjj_in_regist_log where createdOn like '2020-12-28%') as d on (d.inviterId = a.id)
GROUP BY a.id,a.createdOn,a.channel
```

我也不知道自己写的是个什么鬼，下面先熟悉一下join的一些情况，最后再来优化这个SQL语句。

collate utf8_bin是 以二进制值比较，也就是区分大小写，collate是核对的意思

uft-8_general_ci  一般比较，不区分大小写

SHOW FULL COLUMNS FROM mjj_agent;

SHOW FULL COLUMNS FROM mjj_agentactivelog;

## join简介

join，连接两张表，大致分为内连接，外连接，右连接，左连接，自然连接等，下面盗用一张图看下各种join的情况，如下：

<img src="./res/sql_join.jpg" style="zoom:80%;float:left;" />

## 联接查询中使用索引

### 数据准备

数据直接从线上导一份出来（所以就不对外提供了），在各种测试、尝试的过程中主要使用到两张表

+ mjj_agent（4000多条数据）
+ mjj_agentactivelog(60000多条数据)

需求是：查询mjj_agent的id，account字段，统计mjj_agentactivelog中每个agent id出现的次数，关联字段是agentId

### 原始情况

```mysql
SELECT a.id,a.account,b.id from mjj_agent as a
LEFT JOIN mjj_agentactivelog as b on (a.id=b.agentId);
```

发现要执行很久，结果如下：

![](./res/join_1.png)

可以看到68000多条数据，执行了30多秒，这个不用想，实际情况肯定是接收不了的，

那么看下执行计划情况，如下：

```mysql
EXPLAIN SELECT a.id,a.account,b.id from mjj_agent as a
LEFT JOIN mjj_agentactivelog as b on (a.id=b.agentId);
```

结果如下：

![](./res/join_2.png)

可以看到都是走的全表扫描，接下来就是如何优化的问题，建索引。

### agentId上建立索引

```mysql
ALTER TABLE `mjj_agentactivelog` ADD INDEX idx_agent(`agentId`); 
```

查看索引情况：

```mysql
SHOW INDEX from mjj_agentactivelog;
```

![](./res/join_3.png)

可以看到索引已经建好了，结果中的Cardinality是基数的意思，涉及Hyperloglog算法，另外说明。现在再看下执行计划，结果如下：

![](./res/join_4.png)

依然没有使用索引，为什么呢？这是遇到索引失效的问题了，这个sql很简单，没有其他的操作，