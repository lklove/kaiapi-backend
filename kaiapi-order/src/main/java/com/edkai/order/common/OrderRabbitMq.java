package com.edkai.order.common;

import com.edkai.common.constant.RabbitConstant;
import com.edkai.common.constant.RedisConstant;
import com.edkai.order.model.entity.ApiOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lk
 */
@Component
@Slf4j
public class OrderRabbitMq {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendOrder(ApiOrder apiOrder) {
        String orderSn = apiOrder.getOrderSn();
        String key = RedisConstant.ORDER_INFO_PREFIX + orderSn;
        // 保存订单信息，保证消息的可靠传递
        redisTemplate.opsForValue().set(key,apiOrder);
        CorrelationData correlationData = new CorrelationData(orderSn);
        correlationData.getFuture().addCallback(result -> {
            if (result.isAck()){
                redisTemplate.delete(key);
                log.info("订单{}成功到达交换机",orderSn);
            }else {
                // 记录日志，将失败消息存放到失败的数据库中进行补偿
                redisTemplate.delete(key);
                redisTemplate.opsForHash().put(RedisConstant.ORDER_SEND_FAILURE,orderSn,apiOrder);
                log.error("订单--消息投递到服务端失败：{}---->{}",apiOrder.getOrderSn(),result.getReason());
            }
        },ex -> {
            //记录日志，将失败消息存放到失败的数据库中进行补偿
            redisTemplate.delete(key);
            redisTemplate.opsForHash().put(RedisConstant.ORDER_SEND_FAILURE,orderSn,apiOrder);
            log.error("订单--消息投递到服务端发生异常：订单号：{}---->异常消息：{}",apiOrder.getOrderSn(),ex.getMessage());
        });
        rabbitTemplate.convertAndSend(RabbitConstant.ORDER_EXCHANGE_NAME,RabbitConstant.SEND_ORDER_ROUTING_KEY,apiOrder,correlationData);
    }

}
