## redis笔记

### 安装

一、解压

　　tar -zxvf redis-6.0.8.tar.gz

　　安装

　　cd redis-6.0.8

　　make && make install PREFIX=/usr/local/redis

二、配置

　　mkdir /usr/local/redis/conf.d

　　复制示例配置

　　cp redis.conf /usr/local/redis/conf.d

　　建立数据、日志存储目录

　　mkdir -p /var/redis/data

　　mkdir /var/redis/log

　　修改

　　vim /usr/local/redis/conf.d/redis.conf

　　daemonize yes 　　　　　　　　行225
　　supervised systemd 　　　　　　行236
　　修改存储规则 　　　　　　　　　行~307
　　dir /var/redis/data/ 　　　　　　　　　　 数据文件存储目录 　　 行~365
　　logfile "/var/redis/log/redis.log" 　　　　　日志存储文件 行 　　　　~260

　　建立service文件，以便systemctl控制

　　cd /etc/systemd/system

　　touch redis.service

　　vim redis.service

```
[Unit]
Description=RedisService
After=network.target

[Service]
Type=forking
PIDFile=/var/run/redis_6379.pid
ExecStart=/usr/local/redis/bin/redis-server /usr/local/redis/conf.d/redis.conf
ExecReload=/bin/kill -s HUP $MAINPID
ExecStop=/bin/kill -s QUIT $MAINPID
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

三、启动

　　systemctl enable redis

　　systemctl start redis

　　查看启动情况

　　systemctl status redis

### redis特点

+ 存储在内存中，**速度快**
+ Key-Value
+ 单线程Worker，6.x过后支持iothread并发
+ 连接多，通过epoll(IO多路复用)实现
+ Value支持5种数据类型
+ 本地方法：计算向数据移动，IO优化
+ 串行化/原子操作：并行 VS 串行

### redis与memcached的区别

如下图所示：

![](./res/redis_memcached.png)

memcached的value值只能是String类型（很对类型或者对象可以通过json转换成字符串），当需要取一个list中的某个值时，只能将整个内容取出到客户端进行操作。

redis支持list等数据类型，可以通过redis的本地方法进行计算，如图中所示，可以直接通过下标进行取值。

### redis部分理论

**redis的串行化结构，如下：**

![](./res/redis-kernel.png)

**6.x之前串行执行流程图:**

![](./res/redis_worker.png)

**6.x之后引入IOThread，结构如下：**

![](./res/redis_worker_iothread.png)

IO Thread的引入是为了提高服务器只跑redis服务时对CPU的利用率的，IO Thread默认是关闭的，需要手动开启，开启方式：

```
io-threads 4
io-threads-do-reads yes
```

io-threads的值：如果你的server有4个核心，尝试把这个值设置为3,如果有8个核心,尝试把这个值设置为6,但这个值不建议超过8。

### redis基础操作

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

**set/get:**

```
set key value
```

```
get key
```

 `SET key value [EX seconds|PX milliseconds|KEEPTTL] [NX|XX]`

+ NX：只能新建，即key不存在时才能设置成功，可用于分布式锁等场景
+ EX：只能更新，即key存在时才能设置成功

批量操作：`mset`、`mget` 

+ MSETNX ：key不存在才设置，这是个原子操作
+ 

字符串操作：` GETRANGE key start end`、`SETRANGE key offset value`

字符串追加：`APPEND key value`

查看key对应value的类型：`type key`

查看value值得类型：` OBJECT subcommand [arguments [arguments ...]]`,eg:`OBJECT encoding k1`

加一：`INCR key`                            减一：`DECR key`

加N：`INCRBY key n`                     减N：`DECRBY key n`

二进制安全，只取字节流

