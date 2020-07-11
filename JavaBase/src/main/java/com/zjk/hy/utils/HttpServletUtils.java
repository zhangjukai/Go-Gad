package com.zjk.hy.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 */
public class HttpServletUtils {
    private static final RestTemplate REST_TEMPLATE;

    static {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
        httpClientBuilder.setSSLContext(sslContext);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        // 注册http和https请求
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", socketFactory).build();
        // 开始设置连接池
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 最大连接数2700
        connectionManager.setMaxTotal(2700);
        // 同路由并发数100
        connectionManager.setDefaultMaxPerRoute(100);
        httpClientBuilder.setConnectionManager(connectionManager);
        // 重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        HttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setConnectTimeout(6000);
        // 连接不够用时的等待时间
        httpRequestFactory.setConnectionRequestTimeout(10000);
        // 读取数据时的超时时间
        httpRequestFactory.setReadTimeout(10000);
        REST_TEMPLATE = new RestTemplate(httpRequestFactory);
    }

    public static RestTemplate getRestTemplate() {
        return REST_TEMPLATE;
    }

    private HttpServletUtils() {
        throw new AssertionError("本类是一个工具类，不期望被实例化");
    }

    public static void postForHeader(String url, Map<String,Object> params) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        // 此处可将需要传递的header从request中取出来，或通过参数传进来
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Map<String,Object>> formEntity = new HttpEntity<Map<String,Object>>(params, headers);
        String result = REST_TEMPLATE.postForObject(url, formEntity, String.class);
    }

}
