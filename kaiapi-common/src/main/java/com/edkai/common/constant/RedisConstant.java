package com.edkai.common.constant;

/**
 * @author lk
 */
public interface RedisConstant {
    /**
     * mq发送短信保存消息投递的状态以及消息内容
     */
    String SMS_HASH_PREFIX = "api:sms_hash_";
    /**
     * mq生产者发送短信到队列失败的信息。
     */
    String SMS_MQ_PRODUCE_FAIL = "api:sms:mq:produce:fail";

    String SMS_FAIL_SCHEDULE_LOCK = "api:sms:fail:lock";

    String SMS_NOT_CONSUMER_LOCK="api:sms:consumer:lock";

    String CAPTCHA_PREFIX = "api:captchaId:";

    String ORDER_INFO_PREFIX = "api:order:sendOrder:";

    String ALIPAY_TRADE_INFO= "api:order:alipayInfo:";

    String ALIPAY_ORDER_SUCCESS="api:order:alipay:success:";

    String ORDER_PAY_SUCCESS="api:order:pay:";

    String ORDER_SEND_FAILURE="api:order:send:failure";

    String ORDER_SUCCESS_SEND_FAILURE="api:order:success:send:failure";

    String PAY_SUCCESS_FAILURE_JOB_LOCK="api:order:success:lock";
    String ORDER_FAILURE_JOB_LOCK="api:order:lock";

}
