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


}
