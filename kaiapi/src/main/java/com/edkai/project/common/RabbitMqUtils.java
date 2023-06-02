package com.edkai.project.common;

import com.edkai.common.constant.RabbitConstant;
import com.edkai.common.constant.RedisConstant;
import com.edkai.common.model.to.SmsTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author lk
 */
@Component
@Slf4j
public class RabbitMqUtils implements RabbitTemplate.ReturnsCallback {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private SmsTo smsToTemp;

    public void sendSms(SmsTo smsTo){
        String messageId = UUID.randomUUID().toString();
        log.info("短信消息的消息id为：{}",messageId);
        CorrelationData correlationData = new CorrelationData(messageId);
        smsToTemp=smsTo;
        // 保存投递消息的状态
        String smsStatusKey=RedisConstant.SMS_HASH_PREFIX+messageId;
        // 0-消息未投递，1-消息投递成功
        redisTemplate.opsForHash().put(smsStatusKey,"status",0);
        redisTemplate.opsForHash().put(smsStatusKey,"retry",1);
        redisTemplate.opsForHash().put(smsStatusKey,"smsTo",smsTo);
        redisTemplate.expire(smsStatusKey,10, TimeUnit.MINUTES);
        correlationData.getFuture().addCallback(result -> {
            if (result.isAck()){
                redisTemplate.opsForHash().put(smsStatusKey,"status",1);
                log.info("短信消息成功投递至===》{}交换机,投递消息为===》{}", RabbitConstant.SMS_EXCHANGE_NAME,smsTo);
            }else {
                log.error("短信发送消息投递至===》{}交换机失败，投递消息为==》{}",RabbitConstant.SMS_EXCHANGE_NAME,smsTo);
                redisTemplate.delete(smsStatusKey);
                redisTemplate.opsForHash().put(RedisConstant.SMS_MQ_PRODUCE_FAIL,smsTo.getMobile(),smsTo);
            }
        },ex -> {
            log.error("短信发送消息投递===》{}交换机发生异常,异常原因===》{}",RabbitConstant.SMS_EXCHANGE_NAME,ex.getMessage());
            redisTemplate.delete(smsStatusKey);
            redisTemplate.opsForHash().put(RedisConstant.SMS_MQ_PRODUCE_FAIL,smsTo.getMobile(),smsTo);
            throw new RuntimeException(ex);
        });
        rabbitTemplate.convertAndSend(RabbitConstant.SMS_EXCHANGE_NAME,RabbitConstant.SEND_SMS_ROUTING_KEY,smsTo,correlationData);
    }



    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息投递到队列失败===》{}",returnedMessage);
        String messageId = (String) returnedMessage.getMessage().getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        redisTemplate.delete(RedisConstant.SMS_HASH_PREFIX+messageId);
        redisTemplate.opsForHash().put(RedisConstant.SMS_MQ_PRODUCE_FAIL,smsToTemp.getMobile(),smsToTemp);
    }

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(this);
    }
}
