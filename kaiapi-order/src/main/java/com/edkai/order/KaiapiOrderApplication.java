package com.edkai.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableDubbo
@MapperScan("com.edkai.order.mapper")
public class KaiapiOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaiapiOrderApplication.class, args);
    }

}
