package com.edkai.thrid.common;

import com.edkai.common.constant.RabbitConstant;
import com.edkai.common.constant.RedisConstant;
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
public class RabbitOrderPaySuccessUtils {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendPaySuccess(String aliOrderSn){
        String key = RedisConstant.ALIPAY_ORDER_SUCCESS + aliOrderSn;
        // 保存订单信息，保证消息的可靠传递
        redisTemplate.opsForValue().set(key,aliOrderSn);
        CorrelationData correlationData = new CorrelationData(aliOrderSn);
        correlationData.getFuture().addCallback(result -> {
            if (result.isAck()){
                redisTemplate.delete(key);
                log.info("支付成功订单{}成功到达交换机",aliOrderSn);
            }else {
                redisTemplate.delete(key);
                redisTemplate.opsForList().leftPush(RedisConstant.ORDER_SUCCESS_SEND_FAILURE,aliOrderSn);
                log.error("支付成功订单--消息投递到服务端失败：{}---->{}", aliOrderSn, result.getReason());
            }
        },ex -> {
            redisTemplate.delete(key);
            redisTemplate.opsForList().leftPush(RedisConstant.ORDER_SUCCESS_SEND_FAILURE,aliOrderSn);
            log.error("支付成功订单--消息投递到服务端发生异常：订单号：{}---->异常消息：{}",aliOrderSn,ex.getMessage());
        });
        rabbitTemplate.convertAndSend(RabbitConstant.ORDER_EXCHANGE_NAME,RabbitConstant.SEND_ORDER_SUCCESS_ROUTING_KEY,aliOrderSn,correlationData);
    }
}
