package com.edkai.thrid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lk
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ThirdPartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdPartApplication.class,args);
    }
}
