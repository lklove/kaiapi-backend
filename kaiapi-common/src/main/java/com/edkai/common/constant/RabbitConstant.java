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

    /**
     * 订单队列
     */
    String ORDER_QUEUE_NAME="api.order.queue";

    /**
     * 订单交换机
     */
    String ORDER_EXCHANGE_NAME="api.order.exchange";

    /**
     * 订单超时的死信队列
     */
    String ORDER_DLX_NAME="api.order.fail.queue";

    /**
     * 发送订单信息的routingKey
     */
    String SEND_ORDER_ROUTING_KEY="order.send";

    /**
     * 订单发送成功队列
     */
    String ORDER_SUCCESS_QUEUE_NAME="api.order.success.queue";

    /**
     * 死信队列与交换机的routingKey
     */
    String ORDER_DLX_ROUTING_KEY="order.dlx";

    /**
     * 发送订单信息的routingKey
     */
    String SEND_ORDER_SUCCESS_ROUTING_KEY="order.success.send";
}
