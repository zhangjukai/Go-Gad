### logback.xml配置

``` xml
<appender name="AMQP" class="org.springframework.amqp.rabbit.logback.AmqpAppender">
        <!--Layout（纯文本）而不是格式化的JSON -->
        <layout>
            <pattern>
                <![CDATA[%d{yyyy-MM-dd HH:mm:ss} [%thread] [%class:%line] %-5level %logger - %msg%n ]]>
            </pattern>
        </layout>
        <addresses>10.1.2.243:5672</addresses>
        <username>mjj</username>
        <password>edai@168</password>
        <declareExchange>true</declareExchange>
        <exchangeType>direct</exchangeType>
        <exchangeName>mjjLogDev</exchangeName>
        <routingKeyPattern>mjj-err-log</routingKeyPattern>
        <generateId>true</generateId>
        <charset>UTF-8</charset>
        <durable>false</durable>
        <deliveryMode>NON_PERSISTENT</deliveryMode>
        <autoDelete>false</autoDelete>
    </appender>

    <logger name="mel.mjj" level="ERROR" additivity="false">
        <appender-ref ref="AMQP"/>
    </logger>
```

如果需要传输json格式的数据，需要把layout节点换为encoder，

```XML
<encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
    <providers class="net.logstash.logback.composite.loggingevent.LoggingEventJsonProviders">
        <pattern>
            <pattern>
                {
                "app_name":"mjj_error_log",
                "ip":"%ip",
                "date":"%d{yyyy-MM-dd HH:mm:ss}",
                "level":"%level",
                "threadName":"%thread",
                "fileName":"%logger",
                "msg":"%msg"
                }
            </pattern>
        </pattern>
    </providers>
</encoder>
```

### MQ监听消费

``` java
@Slf4j
@Component
public class ErrLogTopicMQReceiver {
    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "mjj-err-log", durable = "true"),
            exchange = @Exchange(value = "mjjLogDev", durable = "false", 
                                 type = ExchangeTypes.DIRECT, autoDelete = "false"),
            key = "mjj-err-log"
    ))
    public void baseSettlementReceiver(Channel channel,Message jsonData,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long tag) 
        throws IOException {
        System.out.println("================Err 日志：" + new String(jsonData.getBody()));
        channel.basicAck(tag, false);
    }
}
```

### 自定义变量取值

``` xml
<conversionRule conversionWord="ip" converterClass="mel.mjj.extension.config.LogIpConfig" />
```

```java
@Slf4j
public class LogIpConfig extends ClassicConverter {

    private static final Logger logger = LoggerFactory.getLogger(LogIpConfig .class);
    private static String webIP;
    static {
        try {
            webIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("获取日志Ip异常", e);
            webIP = null;
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return webIP;
    }
}
```



### 注意点

1、日志级别

2、exchange的各自参数配置和java代码里面需要对应，不然会导致不通