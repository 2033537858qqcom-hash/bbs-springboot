package com.liang.local.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LocalAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalAuthApplication.class, args);
        System.out.println("✅ 本地认证服务启动成功！端口：7014");
    }
}
