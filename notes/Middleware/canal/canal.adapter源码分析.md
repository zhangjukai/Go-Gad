# Adapter

## 前期准备

### 启动canal服务

启动canal和kafka，确保canal能将binlog同步到kafka中

### 源码中修改adapter配置

**adapter主配置: **

adapter的启动程序位于client-adapter下的launcher项目，修改该项目的application.yml

```yml
canal.conf:
  mode: kafka #tcp kafka rocketMQ rabbitMQ
  consumerProperties:
  	kafka.bootstrap.servers: 192.168.40.131:9092
  srcDataSources:
    defaultDS:
      url: jdbc:mysql://192.168.40.131:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false
      username: root
      password: 123456
  canalAdapters:
  - instance: example # canal instance Name or mq topic name
    groups:
    - groupId: g1
      outerAdapters:
      - name: rdb
        key: mysql-test
        properties:
          jdbc.driverClassName: com.mysql.jdbc.Driver
          jdbc.url: jdbc:mysql://192.168.40.131:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false
          jdbc.username: root
          jdbc.password: 123456
```

配置说明:

canal.conf.mode: 设置消息模式

kafka.bootstrap.servers: 配置kafka地址

srcDataSources：配置数据源

canalAdapters：配置一个canaladapter的instance，下面的jdbc对应的是目标表的数据库配置

**adapter-rdb配置：**

配置位于client-adapter中的rdb项目，resources下的rdb目录下的mytest_user.yml文件

```yml
dataSourceKey: defaultDS
destination: example
groupId: g1
outerAdapterKey: mysql-test
concurrent: true
dbMapping:
  database: test
  table: student
  targetTable: target_student
  targetPk:
    id: id
#  mapAll: true
  targetColumns:
    id: id
    name: name
    age: age
    remark: remark
  etlCondition: "where c_time>={}"
  commitBatch: 3000 # 批量提交的大小
```

### 重新打包编译

应为是源码所以改了都需要打包编译，maven命令如下：

```java
mvn install -Dmaven.test.skip=true
```

出现：[INFO] BUILD SUCCESS，表示编译成功

### 启动adapter

运行client-adapter下的launcher项目下的CanalAdapterApplication

### 测试adapter是否正常

正常情况下在student中插入一条数据会插入到target_student表中

## adapter启动源码分析







## adapter数据同步源码分析

