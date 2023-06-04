package com.edkai.order.schedule;

import com.edkai.common.constant.RedisConstant;
import com.edkai.order.common.OrderRabbitMq;
import com.edkai.order.model.entity.ApiOrder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
public class SendOrderFailSchedule {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private OrderRabbitMq orderRabbitMq;

    /**
     * 定时任务添加分布锁是为了保证每次任务只有一个服务器能执行，防止多台服务器共同执行
     * 3
     */
    @Scheduled(cron = "*/60 * * * * ?")
    public void sendOrderFailed() {
        RLock lock = redissonClient.getLock(RedisConstant.ORDER_FAILURE_JOB_LOCK);
        try {
            if (lock.tryLock(0, -1, TimeUnit.SECONDS)) {
                Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.ORDER_SEND_FAILURE);
                Set<Object> keySet = map.keySet();
                for (Object key : keySet) {
                    ApiOrder apiOrder = (ApiOrder) map.get(key);
                    orderRabbitMq.sendOrder(apiOrder);
                    redisTemplate.opsForHash().delete(RedisConstant.ORDER_SEND_FAILURE, key);
                    log.info("完成订单发送失败任务的补偿，订单为：{}", apiOrder.getOrderSn());
                }
            }
        } catch (InterruptedException e) {
            log.error("===定时任务:获取失败生产者发送消息redis出现bug===");
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
