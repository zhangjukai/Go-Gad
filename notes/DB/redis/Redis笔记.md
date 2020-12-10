## redis笔记

### 基础操作

**连接redis：**

进入`/usr/local/redis/bin`目录，执行./redis-cli

**退出**

`exit`

**查看帮助文档**

./redis-cli -h

**DB操作**

redis中默认16个db，可以通过-n来指定连接那个db

./redis-cli -n 8      连接下标为8的DB

在redis命令行中执行：select 0，连接下标为0的db



### String 字符串操作

通过`help @string`查看相关的帮助文档

**插入数据:**

```
set key value
```

 `SET key value [EX seconds|PX milliseconds|KEEPTTL] [NX|XX]`

+ NX：只能新建，即key不存在时才能设置成功，可用于分布式锁等场景
+ XX：只能更新，即key存在时才能设置成功

**获取数据：**

```
get key
```

