package com.liang.bbs.common.config;


import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * spring鐢≧estTemplate锛屼唬鏇夸箣鍓嶇殑HttpClient
 *
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        return httpClientBuilder().build();
    }

    @Bean
    public HttpClientBuilder httpClientBuilder() {
        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig());
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setResponseTimeout(Timeout.ofMilliseconds(10000)) // 璁剧疆鍝嶅簲瓒呮椂鏃堕棿
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(1000)) // 璁剧疆浠庤繛鎺ユ睜鑾峰彇杩炴帴鐨勮秴鏃舵椂闂?
                .build();
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(10000)) // 璁剧疆璇诲彇瓒呮椂鏃堕棿
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setMaxTotal(200); // 璁剧疆鏈€澶ц繛鎺ユ暟
        connectionManager.setDefaultMaxPerRoute(200); // 璁剧疆姣忎釜璺敱鐨勬渶澶ц繛鎺ユ暟
        return connectionManager;
    }

}
