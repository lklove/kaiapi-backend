package com.edkai.project.config;

import com.edkai.common.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lk
 */
@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue smsQueue() {
        Map<String, Object> arguments = new HashMap<>();
        //声明死信队列和交换机
        arguments.put("x-dead-letter-exchange", RabbitConstant.SMS_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key",RabbitConstant.SMS_DLX_ROUTING_KEY);
        // 消息过期时间：1分钟
        arguments.put("x-message-ttl", 60000);
        return new Queue(RabbitConstant.SMS_QUEUE_NAME, true, false, false, arguments);
    }

    @Bean
    public Queue smsDeadLetter() {
        return new Queue(RabbitConstant.SMS_DLX_NAME, true, false, false);
    }


    @Bean
    public Exchange smsExchange() {
        return ExchangeBuilder.directExchange(RabbitConstant.SMS_EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(smsQueue()).to(smsExchange()).with(RabbitConstant.SEND_SMS_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding smsDelayBinding(){
        return BindingBuilder.bind(smsDeadLetter()).to(smsExchange()).with(RabbitConstant.SMS_DLX_ROUTING_KEY).noargs();
    }
}
