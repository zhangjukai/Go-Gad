# SpringCloudGateway

## 简介

Gateway是在Spring生态系统之上构建的API网关服务，基于Spring5、Spring Boot2.x和Spring Reactor、Spring Webflux等技术。Gateway的宗旨是提供一种简单而有效的方式来对API进行路由，以及提供一些强大的过滤器功能，例如：熔断、限流、重试等等。

### 底层原理

使用的是Webflux中的reactor-netty响应式编程组件，底层使用了Netty通讯框架

### 功能

+ 反向代理
+ 鉴权
+ 流量控制
+ 熔断
+ 日志监控
+ 。。。。。

### 为什么选择Gateway？

+ Zuul2.0一直跳票

+ Gateway的特性：

  <span style="color:red">基于Spring5、Spring Boot2.x和Spring Reactor、Spring Webflux等技术</span>

  动态路由：能够匹配任何请求属性，可以对路由指定Predicate(断言)、Filter（过滤器），集成Hystrix断路器功能，请求限流、支持路径重写，，支持长连接易于使用等

+ 底层原理区别

  + Zuul 1.x

    <span style="color:red">基于Servlet 2.5，使用的是阻塞架构，不支持长连接</span>；采用的是Tomcat容器，使用的是传统的Servlet IO处理模型，当请求进入servlet container时，servlet container就会为其绑定一个线程，结构如下：

    ![](./res/Tomcat模型.png)

  + Spring Cloud GateWay

    <span style="color:red">非阻塞架构</span>，支持WebSocket长连接，与Spring紧密集成拥有更好的开发体验

### 三大核心概念

+ Route-路由

  路由是构建网关的基本模块，由ID，目标URI，和一些列的断言、过滤器组成，如果断言为true，则匹配该路由。

+ Predicate-断言

  参考的是java8的java.util.function.Predicate，开发人员可以匹配HTTP请求中的所有内容，如果请求与断言匹配，则进行路由。

+ Filtere-过滤

  指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由之前或者之后对请求进行修改

### Gateway工作流程

核心逻辑：路由转发+执行过滤器链

![](./res/spring_cloud_gateway_diagram.png)

客户端想Spring Cloud Gateway发送请求，然后在Gateway Handler Mapping中找到与请求相匹配的路由，将其发送到Gateway Web Handler。

Web Handler再通过指定的过滤器来将请求发送到我们实际的服务，执行业务逻辑，然后返回。

Filter在“pre”类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等。

在“post”类型的过滤器中可以做响应内容，响应头的修改，日志的输出，流量监控等。

## 入门教程

>  cloud-Gateway