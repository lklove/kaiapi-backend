package com.edkai.common.constant;

/**
 * @author lk
 */
public interface RabbitConstant {
    /**
     * 发送短信队列
     */
    String SMS_QUEUE_NAME="api.sms.queue";
    /**
     * 发送短信交换机
     */
    String SMS_EXCHANGE_NAME="sms.exchange";
    /**
     * 发送短信失败的死信队列
     */
    String SMS_DLX_NAME="api.sms.dlx.queue";
    /**
     * 发送短信的routingKey
     */
    String SEND_SMS_ROUTING_KEY="sms.send";
    /**
     * 死信队列与交换机的routingKey
     */
    String SMS_DLX_ROUTING_KEY="sms.dlx";


}
