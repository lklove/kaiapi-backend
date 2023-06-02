package com.edkai.project.common;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author lk
 */
@Component
public class RedisTokenBucket {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private final long EXPIRE_TIME = 400; //400秒后过期

    /**
     * 对每个手机号设计的令牌桶算法
     *
     * @param phoneNum 手机号
     * @return
     */
    public boolean tryAcquire(String phoneNum) {
        // 1.每分钟允许最多发送的短信数
        int permitsPerMinute = 1;
        // 桶中最大令牌数
        int maxToken = 1;
        // 获取当前时间戳
        long now = System.currentTimeMillis();
        // 计算令牌桶内令牌数
        int tokens = Integer.parseInt(redisTemplate.opsForValue().get(phoneNum + "_tokens") == null ? "0" : redisTemplate.opsForValue().get(phoneNum + "_tokens"));
        // 计算令牌桶上次填充的时间戳
        long lastRefillTime = Long.parseLong(redisTemplate.opsForValue().get(phoneNum + "_last_refill_time") == null ? "0" : redisTemplate.opsForValue().get(phoneNum + "_last_refill_time"));
        // 计算当前时间与上次填充时间的时间差
        long timeSinceLast = now - lastRefillTime;
        // 计算需要填充的令牌数
        int refill = (int) (timeSinceLast / 1000 * permitsPerMinute / 60);
        // 更新令牌桶内令牌数
        tokens = Math.min(refill + tokens, maxToken);
        // 更新上次填充时间戳
        redisTemplate.opsForValue().set(phoneNum + "_last_refill_time", String.valueOf(now),EXPIRE_TIME, TimeUnit.SECONDS);
        // 如果令牌数大于等于1，则获取令牌
        if (tokens >= 1) {
            tokens--;
            redisTemplate.opsForValue().set(phoneNum + "_tokens", String.valueOf(tokens),EXPIRE_TIME, TimeUnit.SECONDS);
            // 如果获取到令牌，则返回true
            return true;
        }
        // 如果没有获取到令牌，则返回false
        return false;

    }

}
