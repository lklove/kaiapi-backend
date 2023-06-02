package com.edkai.project.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 短信限流器
 * @author lk
 */
@Component
@Slf4j
public class SmsLimiter {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private RedisTokenBucket redisTokenBucket;
    private final String SMS_PREFIX="sms:";
    private final String CODE_PREFIX="code:";
    /**
     * 短信过期时间5分钟
     */
    private final long CODE_EXPIRE_TIME= 5;

    /**
     * 对发送短信进行限流，利用令牌桶算法
     * @param phoneNumber
     * @param code
     * @return
     */
    public boolean sendSmsAuth(String phoneNumber, String code) {
        if (redisTokenBucket.tryAcquire(SMS_PREFIX + phoneNumber)) {
            // 通过验证验证后，向redis中写入数据
            String key = CODE_PREFIX + phoneNumber;
            redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
            return true;
        } else {
            log.info("send sms to " + phoneNumber + " rejected due to rate limiting");
            return false;
        }
    }

    /**
     * 验证手机号对应的验证码是否正确
     * @param phoneNumber
     * @param code
     * @return
     */
    public boolean verifyCode(String phoneNumber, String code) {
        String key = CODE_PREFIX + phoneNumber;
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && value.equals(code)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

}
