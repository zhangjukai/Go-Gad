## openfeign服务调用

Feign是一个声明式的web服务客户端，让编写web服务客户端变得非常容易，只需创建一个接口并在接口上添加注解即可。

### 如何使用openfeign

**1.导入相关jar：**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

**2.主启动类上添加@EnableFeignClients注解**

**3.定义接口并添加相关注解**

```java
@FeignClient(value = "CLOUD-PROVIDER-USER")
public interface UserFeignService {
    @GetMapping(value = "/user/list")
    String findList();
}
```

**4.通过定义好的接口调用服务**

```java
@RestController
public class UserController {
    @Autowired
    UserFeignService userFeignService;

    @GetMapping(value = "/feign/user/list")
    public String findListByFeign() {
        return userFeignService.findList();
    }
}
```

**5.定义超时时间**

OpenFeign默认等待一秒钟，超过后报错。可通过如下配置进行设置：

```yaml
ribbon:
  ReadTimeout:  5000
  ConnectTimeout: 5000
```

**6.打印feign调用日志**

添加配置类，指定日志级别：

```java
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLoggerLevelConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
```

配置文件中添加配置：

```yaml
logging:
  level:
    com.zjk.hy.service.UserFeignService: debug
```

日志结果输出如下：

![](./res/openFeign_log.png)

