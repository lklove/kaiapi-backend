package com.edkai.thrid.config;

import com.edkai.common.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lk
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue orderSuccessQueue() {

        return new Queue(RabbitConstant.ORDER_SUCCESS_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding orderSuccessBinding(){
        return new Binding(RabbitConstant.ORDER_SUCCESS_QUEUE_NAME, Binding.DestinationType.QUEUE,
                RabbitConstant.ORDER_EXCHANGE_NAME,RabbitConstant.SEND_ORDER_SUCCESS_ROUTING_KEY,null);
    }
}
