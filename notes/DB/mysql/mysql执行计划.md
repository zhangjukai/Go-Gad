# mysql执行计划

## 简介

mysql通过explain+sql语句提供有关MySQL如何执行语句的信息，官网地址：https://dev.mysql.com/doc/refman/8.0/en/explain-output.html

explain为select语句中使用的<span style="color:red;">每一个表返回一行信息</span>，按照mysql在处理语句时读取这些表的顺序排序输出。

## 输出列的含义

| 标识          | 列名          | 含义                   |
| ------------- | ------------- | ---------------------- |
| id            | select_id     | 该select标识符         |
| select_type   | 没有          | 该select类型           |
| table         | table_name    | 输出行表名             |
| partitions    | partitions    | 匹配的分区             |
| type          | access_type   | 联接类型               |
| possible_keys | possible_keys | 可能的索引选择         |
| key           | key           | 实际选择的索引         |
| key_len       | key_length    | 所选键的长度           |
| ref           | ref           | 与索引比较的列         |
| rows          | rows          | 预估查询结果数量       |
| filtered      | filtered      | 按表条件过滤的行百分比 |
| Extra         | 没有          | 附加信息               |

### id

select查询的序列号，包含一组数字，表示查询中执行select子句或操作表的顺序

id分为三种情况：

+ id相同，执行顺序从上到下

+ id不同，如果是子查询，id的序号会递增，id值越大优先级越高，会越先被执行

+ id相同和id不同，同时存在，相同的可以认为是一组，从上往下顺序执行，在所有组中，id值越大优先级越高，越先执行

  ```mysql
  EXPLAIN select * from score as a 
  left join 
   ( SELECT *  from course where teacherId in (SELECT id from teacher where name = '苏老师')) as b 
  on (a.courseId = b.id)
  ```

### select_type

主要用来分辨查询的类型，是普通查询还是联合查询、子查询等

| select_type值        | 列名                       | 含义                                                         |
| -------------------- | -------------------------- | ------------------------------------------------------------ |
| SIMPLE               | 没有                       | 简单的select（不使用子查询和UNION）                          |
| PRIMARY              | 没有                       | 查询中如果包含任何复杂的子查询，则最外层select被标记为primary |
| UNION                | 没有                       | 如果第二个select出现在union之后，则被标记为union             |
| DEPENDENT UNION      | dependent(true)            | 跟UNION类似，此处表示union或者union all联合而成的结果会受外部表影响 |
| UNION RESULT         | union_result               | 从union表获取结果的select                                    |
| SUBQUERY             | 没有                       | 在select或者where中包含子查询                                |
| DEPENDENT SUBQUERY   | dependent(true)            | subquery的子查询收到外部表查询的影响                         |
| DERIVED              | 没有                       | from子句中出现的子查询，派生表                               |
| DEPENDENT DERIVED    | dependent(true)            | 派生表依赖于另一个表                                         |
| MATERIALIZED         | materialized_from_subquery | 物化子查询                                                   |
| UNCACHEABLE SUBQUERY | cacheable（false）         | 子查询，其结果无法缓存，必须针对外部查询的每一行重新进行评估 |
| UNCACHEABLE UNION    | cacheable(false)           | UNION属于不可缓存子查询的中的第二个或更高版本的选择（请参阅参考资料 `UNCACHEABLE SUBQUERY`） |

### table

输出行所引用的表的名称。这也可以是以下值之一：

- <union*M*,*N*>：该行是指具有和id值的行 的 *`M`*并集 *`N`*。
- <derived*`N`*>：该行是指用于与该行的派生表结果`id`的值 *`N`*。派生表可能来自（例如）`FROM`子句中的子查询 。
- <subquery*`N`*>：该行是指该行的物化子查询的结果，其`id` 值为*`N`*。请参见 [第8.2.2.2节“通过实现来优化子查询”](https://dev.mysql.com/doc/refman/8.0/en/subquery-materialization.html)。

### partitions

查询将从中匹配记录的分区。该值适用`NULL`于未分区的表。

### type

联接类型，去数据库查询数据的方式，最容易想到的就是全表扫描，直接遍历一张表所有的数据，效率低下。联接类型很多，按效率从好带坏一次为：

system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL 

一般情况下，<span style="color:red">需要保证查询至少达到range级别，最好能达到ref</span>

+ system

  该表只有一行（=系统表），这是const联接类型的特列

+ const

  这个表至多有一个匹配行，意思就是根据条件查询出来的结果只有一条数据

  ```mysql
  EXPLAIN select * from mjj_activity where id = '1' and code='ACTIVITY_FISSION';
  ```

+ eq_ref

  使用唯一性索引进行数据查找

  ```mysql
  alter table dep add unique key dep(`depno`);
  
  EXPLAIN select a.* from dep as a 
  LEFT JOIN dept2 as b on (a.depno = b.no)
  ```

+ ref

  使用了非唯一性索引进行数据的查找

  ```mysql
  create index idx_score_1 on score(courseId);
  EXPLAIN SELECT * from score where courseId = 1;
  ```

+ fulltext

  使用FULLTEXT索引执行联接

+ ref_or_null

  对于某个字段即需要关联条件，也需要null值的情况下，查询优化器会选择这种访问方式

+ index_merge

  在查询过程中需要多个索引组合使用

+ index_subquery

  利用索引来关联子查询，不再扫描全表

+ range

  利用索引查询的时候限制了范围，在指定范围内进行查询，这样避免了index的全索引扫描，适用的操作符： =, <>, >, >=, <, <=, IS NULL, BETWEEN, LIKE, or IN() 

  ```mysql
  EXPLAIN select * from student where `name` LIKE '王%';
  ```

+ index

  全索引扫描这个比all的效率要好，主要有两种情况

  - 如果索引是查询的覆盖索引，并且可用于满足表中所需的所有数据，则仅扫描索引树。在这种情况下，`Extra`列显示为 `Using index`。仅索引扫描通常比索引扫描更快， [`ALL`](https://dev.mysql.com/doc/refman/8.0/en/explain-output.html#jointype_all)因为索引的大小通常小于表数据。
  - 使用对索引的读取执行全表扫描，以按索引顺序查找数据行。 `Uses index`没有出现在 `Extra`列中。

+ ALL

  全表扫描

### Extra

包含额外的信息

+ using filesort

  说明mysql无法利用索引进行排序，只能利用排序算法进行排序，会消耗额外的性能

  ```mysql
  EXPLAIN select * from student where `name` LIKE '王%' ORDER BY id;
  ```

+ using temporary

  建立临时表来保存中间结果，查询完成之后把临时表删除

  ```mysql
  EXPLAIN select stuId,SUM(grade) from score GROUP BY stuId;
  ```

+ using index

  使用覆盖索引的时候就会出现

+ Using index condition

  查询使用了索引，但是需要回表来查询数据

+ using where

  使用where进行条件过滤

+ using join buffer

  使用连接缓存，情况没有模拟出来

+ impossible where

  where语句的结果总是false,Impossible WHERE noticed after reading const tables

