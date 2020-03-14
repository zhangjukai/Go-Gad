## 分布式中Header和IP的处理方式

### Nginx反向代理传递自定义header

​	Nginx默认不支持用户自定义的非Nginx标准的Header，如果需要使用，需要在http段或者server段添加underscores_in_headers on; 

```nginx
http {
  underscores_in_headers on;
  server {
    location / {
      proxy_pass http://bank/;
      proxy_set_header app_key ${http_app_key};
    }
  }
}
```

### Nginx传递客户端真实IP

​	后端在获取客户端的IP地址时，如果通过Nginx反向代理或者转发，获取到的IP地址会是Nginx服务器的地址，如果需要获取到真实的客户端IP地址，Nginx需要添加如下配置：

```nginx
proxy_set_header Host $host;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header REMOTE-HOST $remote_addr;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
```

eg:

```nginx
location / {
     proxy_pass http://127.0.0.1:10678;
     proxy_set_header Host $host;
     proxy_set_header X-Real-IP $remote_addr;
     proxy_set_header REMOTE-HOST $remote_addr;
     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
```

### 后端获取IP

```java
 /**
   * 获取客户端ip地址
   * @param request
   * @return
   */
public static String getIpAddr(HttpServletRequest request) {
    String ipAddress = null;
    try {
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || 					                "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
    } catch (Exception e) {
        ipAddress="";
    }
    return ipAddress;
}
```

### zuul网关传递header

通过继承ZuulFilter来实现

转发IP地址：

```java
@Override
public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    String remoteAddr = request.getRemoteAddr();
    ctx.getZuulRequestHeaders().put(HTTP_X_FORWARDED_FOR, remoteAddr);
    return null;
}
```

转发header中的数据：

```java
 @Override
public Object run() throws ZuulException {
    //得到request上下文
    RequestContext currentContext = RequestContext.getCurrentContext();
    //得到request域
    HttpServletRequest request = currentContext.getRequest();
    //得到头信息
    String header = request.getHeader("header-key");
    //判断是否有头信息
    if(header != null && !"".equals(header)){
        //把头信息继续向下传
        currentContext.addZuulRequestHeader("header-key", header);
    }
    return null;
}
```

### zuul网关过滤header

zuul.ignoredHeaders=Cookie,Set-Cookie

zuul.sensitiveHeaders=Cookie,Set-Cookie

### RestTemplate传递Header

```java
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class RestTemplateUtil {

    public static String post(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.POST, params);
        return rss.getBody();
    }

    public static String get(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.GET, params);
        return rss.getBody();
    }

    public static String delete(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.DELETE, params);
        return rss.getBody();
    }

    public static String put(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.PUT, params);
        return rss.getBody();
    }

    /**
     * @param req
     * @param url
     * @param method
     * @param params maybe null
     * @return
     */
    private static ResponseEntity<String> request(ServletRequest req, String url, HttpMethod method, Map<String, ?> params) {
        HttpServletRequest request = (HttpServletRequest) req;
        //获取header信息
        HttpHeaders requestHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            requestHeaders.add(key, value);
        }
        //获取parameter信息
        if(params == null) {
            params = request.getParameterMap();
        }

        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        ResponseEntity<String> rss = HttpServletUtils.getRestTemplate().exchange(url, method, requestEntity, String.class, params);
        return rss;
    }
}
```

### 设置Feign的Header信息

#### 通过直接在请求上,或者在类上添加Headers的注解

```java
@Headers({"Content-Type: application/json","Accept: application/json",Accept {contentType}})
@PostMapping(value = "/card-blank/batch-create")
Response batchCreateCard(@RequestBody CreateCardBlankDTO condition,@Param("contentType") String type);
```

#### 通过实现RequestInterceptor接口,完成对所有的Feign请求,设置Header

```java
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor{

    private final Logger logger = LoggerFactory.getLogger(getClass());
 
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                template.header(name, values);

            }
            logger.info("feign interceptor header:{}",template);
        }
}
```



