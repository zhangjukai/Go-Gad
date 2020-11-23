## Mybatis日志实现

### Mybatis日志初始化逻辑

SqlSessionFactoryBean中LOGGER属性，如下：

```java
private static final Logger LOGGER = 
    LoggerFactory.getLogger(SqlSessionFactoryBean.class);
```

LoggerFactory具体实现是org.apache.ibatis.logging.LoggerFactory，LoggerFactory初始化时会执行如下代码：

```java
static {
  tryImplementation(LogFactory::useSlf4jLogging);
  tryImplementation(LogFactory::useCommonsLogging);
  tryImplementation(LogFactory::useLog4J2Logging);
  tryImplementation(LogFactory::useLog4JLogging);
  tryImplementation(LogFactory::useJdkLogging);
  tryImplementation(LogFactory::useNoLogging);
}
```

```java
private static void tryImplementation(Runnable runnable) {
  if (logConstructor == null) {
    try {
      runnable.run();
    } catch (Throwable t) {
      // ignore
    }
  }
}
```

```java
public static synchronized void useSlf4jLogging() {
  setImplementation(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
}

private static void setImplementation(Class<? extends Log> implClass) {
    try {
        Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
        Log log = candidate.newInstance(LogFactory.class.getName());
        if (log.isDebugEnabled()) {
            log.debug("Logging initialized using '" + implClass + "' adapter.");
        }
        logConstructor = candidate;
    } catch (Throwable t) {
        throw new LogException("Error setting Log implementation.  Cause: " + t, t);
    }
}
```

由以上代码可以看出，在LoggerFactory中是通过去加载指定的日志类来创建Log对象的，其中一个重点是`if (logConstructor == null)`当创建了就不会再创建了。

### 指定日志类

#### 调用指定方法

```java
org.apache.ibatis.logging.LogFactory.useSlf4jLogging();
org.apache.ibatis.logging.LogFactory.useLog4J2Logging();
org.apache.ibatis.logging.LogFactory.useLog4JLogging();
org.apache.ibatis.logging.LogFactory.useJdkLogging();
org.apache.ibatis.logging.LogFactory.useCommonsLogging();
org.apache.ibatis.logging.LogFactory.useStdOutLogging();
```

以上方法在Spring容器初始化前调用会起作用

#### 指定实现类

```java
org.apache.ibatis.session.Configuration configuration = new 
    org.apache.ibatis.session.Configuration();
configuration.setLogImpl(Log4j2Impl.class);
factoryBean.setConfiguration(configuration);
```

通过SqlSessionFactory的`setLogImpl`方法指定一个日志实现类，在一个系统中存在多种日志框架可以通过这种方式指定。

### 各种日志框架使用

不考虑指定日志方式，仅看日志默认实现，具体的加载过程，如下代码：

```java
static {
  tryImplementation(LogFactory::useSlf4jLogging);
  tryImplementation(LogFactory::useCommonsLogging);
  tryImplementation(LogFactory::useLog4J2Logging);
  tryImplementation(LogFactory::useLog4JLogging);
  tryImplementation(LogFactory::useJdkLogging);
  tryImplementation(LogFactory::useNoLogging);
}
```

由此可见，Mybatis日志的默认顺序是：slf4j、jcl、log4j2、log4j、jul、nologging，下面依次来看各种日志的具体整合，这儿需要强调一点，**必须保证项目纯净，保证不会通过其他的jar引入slf4j、log4j等，需要集成时才引入**。

#### 使用Slf4j

对于SLF4J,其官网的介绍如下：

> The Simple Logging Facade for Java (SLF4J) serves as a simple facade or abstraction for various logging frameworks (e.g. java.util.logging, logback, log4j) allowing the end user to plug in the desired logging framework at *deployment* time.

大概意思就是SLF4J是各种日志框架的一个门面，外观，其本身并不提供写日志的具体实现，在使用中需要继承各种的日志框架，比如： java.util.logging, logback, log4j等。

上面我们只引入了slf4j-api，所有最后是没有日志信息输出的，在这种情况下，我们需要绑定其他的日志框架。

引入slf4j：

```java
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.26</version>
</dependency>
```

启动项目过后，SqlSessionFactoryBean中LOGGER属性的值如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/slf4j-noplogger.png)

通过LOGGER的值我们能够看出，日志是使用的slf4j，具体的却是一个NOPLogger，并且控制台有如下输出信息：

```java
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

对于以上输出官网给出的解决方法是：

> This warning message is reported when the `org.slf4j.impl.StaticLoggerBinder` class could not be loaded into memory. This happens when no appropriate SLF4J binding could be found on the class path. Placing one (and only one) of *slf4j-nop.jar* *slf4j-simple.jar*, *slf4j-log4j12.jar*, *slf4j-jdk14.jar* or *logback-classic.jar* on the class path should solve the problem.
>
> Note that slf4j-api versions 2.0.x and later use the [ServiceLoader](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) mechanism. Backends such as logback 1.3 and later which target slf4j-api 2.x, do not ship with `org.slf4j.impl.StaticLoggerBinder`. If you place a logging backend which targets slf4j-api 2.0.x, you need *slf4j-api-2.x.jar* on the classpath. See also [relevant faq](http://www.slf4j.org/faq.html#changesInVersion18) entry.

##### SLF4J绑定LOG4J2

**导入Log4j2的jar：**

```java
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.12.1</version>
</dependency>
<!--绑定log4j与slf4j -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
    <version>2.12.1</version>
</dependency>
```

**添加log4j2.xml配置文件：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <Appenders>
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="%d %-5p [%t] %C{2} (%F:%L) - %m%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="org.apache.log4j.xml" level="info"/>
        <Root level="debug">
            <AppenderRef ref="STDOUT"/>
        </Root>
    </Loggers>
</Configuration>
```

这样就能够输出日志文件，同时Mybatis也能打印sql语句，如果不加log4j2.xml这个配置文件，也不会输出日志文件。

这种情况下，SqlSessionFactoryBean中LOGGER属性的值如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/slf4j-log4j2.png)

##### SLF4J绑定Log4j

去掉slf4j绑定Log4j2中导入的jar，只留下slf4j-api，然后导入log4j相关的jar：

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.30</version>
</dependency>
```

添加log4j.properties配置文件：

```java
# 全局日志配置
log4j.rootLogger=info, stdout
# MyBatis 日志配置
log4j.logger.com.zjk.hy.mybatis.dao=TRACE
# 控制台输出
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

##### 使用Logback

Logback是由log4j创始人设计的另一个开源日志组件,官方网站： http://logback.qos.ch。它当前分为下面下个模块：

- logback-core：其它两个模块的基础模块
- logback-classic：它是log4j的一个改良版本，同时**它完整实现了slf4j API**，使你可以很方便地更换成其它日志系统如log4j或JDK14 Logging
- logback-access：访问模块与Servlet容器集成提供通过Http来访问日志的功能

引入logback-classic：

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
```

引入这一个jar就可以了，简单的输出也不需要其他的配置，

SqlSessionFactoryBean中得到的LOGGER，如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/slf4j-logback.png)

##### 使用JUL

**导入slf4j-jdk14：**

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-jdk14</artifactId>
    <version>1.7.25</version>
</dependency>
```

SqlSessionFactoryBean中得到的LOGGER，如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/slf4j-jdk.png)

但是这种情况下并没有Mybatis打印sql的日志输出，这是为什么呢？

在前面的学习中，了解到JUL只会输出INFO UP以上的日志，而sm中的日志是INFO down的，如下：

```java
private static void setImplementation(Class<? extends Log> implClass) {
  try {
    Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
    Log log = candidate.newInstance(LogFactory.class.getName());
    if (log.isDebugEnabled()) {
      log.debug("Logging initialized using '" + implClass + "' adapter.");
    }
    logConstructor = candidate;
  } catch (Throwable t) {
    throw new LogException("Error setting Log implementation.  Cause: " + t, t);
  }
}
```

以上代码位于：org.apache.ibatis.logging.LogFactory，最终会调用一下代码，

![](D:/workspace/Go-Gad/notes/MyBatis/res/JDK14Logger-isDebugEnabled.png)

这儿跳过的细节比较多，直白的说就是，JUL中只会输出800以及以上级别的日志，这儿传入的是500，所以不会输出相关日志。

##### 总结

在Spring或者Springboot加Mybatis的项目中，一般都会引入slf4j，不直接引入也会通过其他jar隐式的引入，对于具体的日志框架，现在也多用Logback或者log4j(2)，总之，我们通过SqlSessionFactoryBean中的LOGGER能够看出具体使用的是哪个日志框架。

#### 使用JCl（Spring5）

spring5使用的spring-jcl(spring改了jcl的代码)来记录日志的，spring-context中默认引入了spring-jcl，如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/Spring-jcl-jar.png)

要使用jcl，需要将使用slf4j中引入的相关jar全部去掉，启动项目，SqlSessionFactoryBean中的LOGGER的值如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/Spring-jcl-jul.png)

由上图可以看到，具体的日志框架使用的是：JavaUtilLog（Spring-jcl默认使用jul），所以不会有sql语句的输出，上面已经分析过了。

##### jcl中使用log4j

看了半天，发现Spring5中的jcl并不提供log4j的集成，源码如下：

```java
// 默认使用JUL
private static LogApi logApi = LogApi.JUL;
static {
   ClassLoader cl = LogFactory.class.getClassLoader();
   try {
      // Try Log4j 2.x API
      cl.loadClass("org.apache.logging.log4j.spi.ExtendedLogger");
      logApi = LogApi.LOG4J;
   }
   catch (ClassNotFoundException ex1) {
      try {
         // Try SLF4J 1.7 SPI
         cl.loadClass("org.slf4j.spi.LocationAwareLogger");
         logApi = LogApi.SLF4J_LAL;
      }
      catch (ClassNotFoundException ex2) {
         try {
            // Try SLF4J 1.7 API
            cl.loadClass("org.slf4j.Logger");
            logApi = LogApi.SLF4J;
         }
         catch (ClassNotFoundException ex3) {
            // Keep java.util.logging as default
         }
      }
   }
}
```

##### jcl中使用log4j2

引入相关jar：

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.12.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.12.1</version>
</dependency>
```

添加log4j2.xml配置文件（前面已经介绍过了）

启动项目，SqlSessionFactoryBean中的LOGGER如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/Spring-jcl-log4j2.png)

这样就能够正常打印相应的日志了，能够输出sql语句

##### 集成SLF4J

对于集成SLF4J，如果项目中引入SLF4J，默认情况下根本到不了这一步，因为默认的加载属性是先加载SLF4J。除非是手动调用`org.apache.ibatis.logging.LogFactory.useCommonsLogging();`

而SLF4J同样需要绑定其他的日志框架，前面已经介绍过了这儿就不再介绍。

#### 直接使用Log4j2

由于我使用的demo里面是Spring5+Mybatis的，因为用了Spring，就没法把spring-jcl排除，就算通过以下方式排除，

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.0.12.RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jcl</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

那么为了能够正常启动项目，也必须引入`commons-logging`，那么也变成了jcl，所以走不到tryImplementation(LogFactory::useLog4J2Logging);这一步，要使用Log4j2就需要在Spring容器初始化前手动去调用useLog4J2Logging方法，

```java
org.apache.ibatis.logging.LogFactory.useLog4J2Logging();
```

启动项目，SqlSessionFactoryBean中的LOGGER如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/log4j2-LOGGER.png)

由上图可以看出，这就是一个Log4j2的对象，同时也能够正常的输出日志

#### 直接使用Log4j

在Spring容器初始化前手动调用：

```java
org.apache.ibatis.logging.LogFactory.useLog4JLogging();
```

启动项目，SqlSessionFactoryBean中的LOGGER如下：

![](D:/workspace/Go-Gad/notes/MyBatis/res/log4j-LOGGER.png)

由上图可以看出，这就是一个Log4j的对象，同时也能够正常的输出日志，sql日志输出如下:

```java
DEBUG [main] -==> Preparing: select * from s_user
DEBUG [main] -==> Parameters: 
DEBUG [main] -<== Total: 0
```

#### 直接使用JdkLogging

org.apache.ibatis.logging.LogFactory.useJdkLogging();

![](D:/workspace/Go-Gad/notes/MyBatis/res/JDK-LOGGER.png)

整个流程和上面的差不多，就不再累赘了。

#### 对于ssetLogImpl的理解

```java
 @Bean
public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource());
    org.apache.ibatis.session.Configuration configuration = new 
        org.apache.ibatis.session.Configuration();
    configuration.setLogImpl(Log4jImpl.class);
    factoryBean.setConfiguration(configuration);
    return factoryBean.getObject();
}
```

前面有些地方对于这个方法理解错了，这个方法的作用是在存在多个日志框架下，能够指定SqlSessionFactoryBean中使用哪个日志框架，

**configuration.setLogImpl(Log4j2Impl.class)输出结果如下：**

```java
2020-11-23 16:36:06,373 DEBUG [main] jdbc.BaseJdbcLogger (BaseJdbcLogger.java:137) - ==>  Preparing: select * from s_user
2020-11-23 16:36:06,469 DEBUG [main] jdbc.BaseJdbcLogger (BaseJdbcLogger.java:137) - ==> Parameters: 
2020-11-23 16:36:06,518 DEBUG [main] jdbc.BaseJdbcLogger (BaseJdbcLogger.java:137) - <==      Total: 0
```

**configuration.setLogImpl(Log4jImpl.class)输出结果如下：**

```java
DEBUG [main] -==> Preparing: select * from s_user
DEBUG [main] -==> Parameters: 
DEBUG [main] -<== Total: 0
```

### 总结

整体而言，Mybatis的日志实现到此基本就梳理完成了，需要注意的是slf4j和jcl，都是日志框架的一个门面，需要绑定其他具体的日志框架才能完成日志的记录，目前常见的日志框架：log4g、log4j2、logback、JUL等，在Spring+Mybatis或SpringBoot+Mybatis这样的体系中，还是log4g2和logback使用得多一点。