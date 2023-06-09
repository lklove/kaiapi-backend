package com.edkai.project.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lk
 */
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(){
        Config config =new Config();
        config.useSingleServer().setAddress("redis://192.168.4.134:6379");
        return Redisson.create(config);
    }
}
