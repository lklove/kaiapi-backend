package com.edkai.thrid.schedule;

import cn.hutool.json.JSONUtil;
import com.edkai.common.constant.RedisConstant;
import com.edkai.common.model.to.SmsTo;
import com.edkai.thrid.common.RabbitOrderPaySuccessUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lk
 */
@EnableAsync
@EnableScheduling
@Slf4j
@Component
public class SendOrderSuccessFailure {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RabbitOrderPaySuccessUtils rabbitUtils;

    /**
     * 定时任务添加分布锁是为了保证每次任务只有一个服务器能执行，防止多台服务器共同执行
     * 每1分钟进行一次查询补偿发送 支付成功 失败的消息
     */
    @Scheduled(cron = "*/60 * * * * ?")
    public void sendPaySuccessFailed(){
        RLock lock = redissonClient.getLock(RedisConstant.PAY_SUCCESS_FAILURE_JOB_LOCK);
        try{
            if (lock.tryLock(0, -1, TimeUnit.SECONDS)) {
                Set<Object> members = redisTemplate.opsForSet().members(RedisConstant.ORDER_SUCCESS_SEND_FAILURE);
                if (!CollectionUtils.isEmpty(members)){
                    // 对发送成功消息失败的订单进行补偿机制
                    for (Object member : members) {
                        rabbitUtils.sendPaySuccess(member.toString());
                        redisTemplate.opsForSet().remove(RedisConstant.ORDER_SUCCESS_SEND_FAILURE,member);
                        log.info("对订单{}完成补偿重发",member);
                    }
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


}
