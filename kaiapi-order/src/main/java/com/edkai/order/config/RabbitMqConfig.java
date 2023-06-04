package com.edkai.order.config;

import com.edkai.common.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单服务mq 配置
 * @author lk
 */
@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue orderQueue() {
        Map<String, Object> arguments = new HashMap<>();
        //声明死信队列和交换机
        arguments.put("x-dead-letter-exchange", RabbitConstant.ORDER_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key",RabbitConstant.ORDER_DLX_ROUTING_KEY);
        // 消息过期时间：30分钟
        arguments.put("x-message-ttl", 1000*60*30);
        return new Queue(RabbitConstant.ORDER_QUEUE_NAME, true, false, false, arguments);
    }


    @Bean
    public Queue orderDeadLetter() {
        return new Queue(RabbitConstant.ORDER_DLX_NAME, true, false, false);
    }


    @Bean
    public Exchange orderExchange() {
        return ExchangeBuilder.directExchange(RabbitConstant.ORDER_EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(RabbitConstant.SEND_ORDER_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding orderDelayBinding(){
        return BindingBuilder.bind(orderDeadLetter()).to(orderExchange()).with(RabbitConstant.ORDER_DLX_ROUTING_KEY).noargs();
    }


}
