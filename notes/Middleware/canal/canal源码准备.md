1、源码下载：

```
git clone https://github.com/alibaba/canal.git
```

2、源码导入idea

​	如果jdk版本高于1.8，需要将druid从1.2.6升级

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.8</version>
</dependency>
```

3、编译源码，maven打包跳过单元测试

```java
mvn clean install -Dmaven.test.skip=true
```





整体流程还是比较简单