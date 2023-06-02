package com.edkai.project.job.schedule;

import cn.hutool.json.JSONUtil;
import com.edkai.common.constant.RedisConstant;
import com.edkai.common.model.to.SmsTo;
import com.edkai.project.common.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lk
 */
@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class SendSmsFailSchedule {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RabbitMqUtils rabbitMqUtils;

    /**
     * 定时任务添加分布锁是为了保证每次任务只有一个服务器能执行，防止多台服务器共同执行
     * 3
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void sendFailedSMS(){
        RLock lock = redissonClient.getLock(RedisConstant.SMS_FAIL_SCHEDULE_LOCK);
        try{
            if (lock.tryLock(0, -1, TimeUnit.SECONDS)) {
                Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.SMS_MQ_PRODUCE_FAIL);
               for (Map.Entry<Object,Object> entry: map.entrySet()){
                   Object value = entry.getValue();
                   SmsTo smsTo = JSONUtil.toBean(JSONUtil.toJsonStr(value), SmsTo.class);
                   rabbitMqUtils.sendSms(smsTo);
                   log.info("短信发送失败的定时任务 发送手机号为--》{},短信为--》{}",smsTo.getMobile(),smsTo.getCode());
                   redisTemplate.opsForHash().delete(RedisConstant.SMS_MQ_PRODUCE_FAIL,entry.getKey());
               }
            }
        } catch (InterruptedException e) {
            log.error("===定时任务:获取失败生产者发送消息redis出现bug===");
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }


    /**
     * 定时任务：每分钟从Redis中查询状态未：已投递，但长时间未消费的消息.
     * 将未消费的消息，取出然后重新发送短信
     */
    @Scheduled(cron = "*/60 * * * * ?")
    public void delSuccessSms(){
        RLock lock = redissonClient.getLock(RedisConstant.SMS_NOT_CONSUMER_LOCK);
        try {
            boolean tryLock = lock.tryLock(20, 10, TimeUnit.SECONDS);
            if (tryLock){
                Set<String> keys = redisTemplate.keys(RedisConstant.SMS_HASH_PREFIX + "*");
                for (String key : keys) {
                    int status = (int) redisTemplate.opsForHash().get(key, "status");
                    Long expire = redisTemplate.opsForHash().getOperations().getExpire(key);
                    if (status == 1 && expire <480){
                        SmsTo smsTo = (SmsTo) redisTemplate.opsForHash().get(key, "smsTo");
                        redisTemplate.delete(key);
                        log.info("消息 {} 长时间未消费",smsTo);
                        rabbitMqUtils.sendSms(smsTo);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("===定时任务：获取长时间未消费出现bug===");
            throw new RuntimeException(e);
        }finally {
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
