## Hystrix断路器简介

Hystrix是一个用于处理分布式系统的**延迟**和**容错**的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如：超时、异常等，Hystrix能够保证在一个依赖出问题的情况下，**不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性**。

### Hystrix重要概念

+ 服务降级

  在分布式程序调用过程中出现**程序运行异常**、**超时**、**服务熔断触发服务降级**、**线程池/信号量打满导致服务降级**等情况下，为了不让客户等待，通过fallback的方式及时的返回一个友好的提示，如：服务器忙，请稍后再试。

+ 服务熔断

  熔断机制是应对雪崩效应的一种微服务链路保护机制，当链路的某个微服务出错或者相应时间过长时，会进行服务降级，进而熔断该结点微服务的调用，快速返回错误响应的信息。**当检测到该结点微服务调用响应正常后，恢复调用链路**。

  调用量达到最大服务访问量后，直接拒绝访问，然后调用服务降级的方法，并返回友好提示，类似于保险丝

+ 服务限流

  通过一定的限流算法处理短时间内出现大量的高并发的访问/请求，从而起到保护系统的作用。一般开发高并发系统常见的限流有：限制总并发数（比如数据库连接池、线程池）、限制瞬时并发数（如nginx的limit_conn模块，用来限制瞬时并发连接数）、限制时间窗口内的平均速率（如Guava的RateLimiter、nginx的limit_req模块，限制每秒的平均速率）；其他还有如限制远程接口调用速率、限制MQ的消费速率。另外还可以根据网络连接数、网络流量、CPU或内存负载等来限流。

### 服务降级

#### 服务端初步使用

1. 引入Hystrix

   ```java
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
   </dependency>
   ```

2. @EnableHystrix

   启动类上添加@EnableHystrix注解

3. @HystrixCommand

   某个服务的某个方法上添加@HystrixCommand，并设置参数：fallbackMethod = "findListFallBack"

4. 编写fallback方法

   ```java
   public List<UserDo> findListFallBack() {
       System.out.println("进入服务降级回调方法");
       return null;
   }
   ```

   回调方法的方法签名需要与原方法相同

5. 服务调用测试

总结：上面的列子是在原有测试项目的基础上修改的，并不是特别合适。其中重点就是@EnableHystrix和@HystrixCommand两个注解，以及fallbackMethod 配置的回调方法。

#### 服务端限制请求时间

配置如下：

```java
@HystrixCommand(fallbackMethod = "findListFallBack",
        commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value 
                         = "1500")
})
```

以上配置的意思是，超过1.5秒后就执行回调方法，但是业务方法也会继续执行。

#### 客户端使用@HystrixCommand

客户端使用Hystrix与上面的使用方式一样，并且多数都是用于客户端

#### 客户端全局fallback

```java
@DefaultProperties(defaultFallback = "defaultFallback")
```

在具体的controller类上添加以上注解，然后在要使用的方法上添加@HystrixCommand注解

#### Feign结合Hystrix

##### fallback方式

1.@FeignClient中添加fallback

```java
@FeignClient(value = "CLOUD-PROVIDER-USER",fallback = UserFeignFallbackService.class)
```

2.定义UserFeignFallbackService实现类

```java
import com.zjk.hy.service.UserFeignService;
import org.springframework.stereotype.Service;

@Service
public class UserFeignFallbackService implements UserFeignService {
    @Override
    public String findList() {
        return "feign调用，服务降级。。。。。。。o(╥﹏╥)o";
    }
}
```

3.配置文件修改

```yaml
feign:
  hystrix:
    enabled: true
```

##### FallbackFactory方式

```java
@FeignClient(value = "CLOUD-PROVIDER-USER",fallbackFactory = UserFeignFallbackFactoryService.class)
```

```java
import com.zjk.hy.service.UserFeignService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallbackFactoryService implements FallbackFactory<UserFeignService> {
    @Override
    public UserFeignService create(Throwable throwable) {
        return new UserFeignService() {
            @Override
            public String findList() {
                return "FallbackFactory-feign调用，服务降级。。。。。。。o(╥﹏╥)o";
            }
        };
    }
}
```

其他的与使用fallback相同

### 服务熔断

```java
@HystrixCommand(fallbackMethod = "payCircuitBreakerFallback",commandProperties = {
    	// 是否开启断路器 默认为false
        @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
    	// 请求次数
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
	    // 时间窗口期
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), 
   		// 失败率达到多少后跳闸 百分比
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60") 
})
@GetMapping("/user/{id}")
public String payCircuitBreaker(@PathVariable("id") Integer id) {
    if(id<0){
        throw new RuntimeException("id不能小于0 ");
    }
    return Thread.currentThread().getName()+"调用成功，流水号为："+IdUtil.simpleUUID();
}

private String payCircuitBreakerFallback(Integer id) {
    return "id不能为负数，请稍后再试，o(╥﹏╥)o，ID："+id;
}
```

其它的与前面服务降级相同，具体的表现是：当出现大量失败的情况下，正常访问刚开始也会被降级

官网地址：https://github.com/Netflix/Hystrix/wiki

大神文章：https://martinfowler.com/bliki/CircuitBreaker.html

![](./res/circuitbreaker.png)



说明：以为以后主要会用Alibaba那个限流框架，所以Hystrix了解到这儿就可以了