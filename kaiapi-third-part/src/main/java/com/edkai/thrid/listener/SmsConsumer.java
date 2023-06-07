package com.edkai.thrid.listener;

import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.edkai.common.ErrorCode;
import com.edkai.common.constant.RabbitConstant;
import com.edkai.common.constant.RedisConstant;
import com.edkai.common.exception.BusinessException;
import com.edkai.common.model.to.SmsTo;
import com.edkai.thrid.common.AliSmsUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * 发送短信的消费者
 *
 * @author lk
 */
@Component
@Slf4j
public class SmsConsumer {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    AliSmsUtils aliSmsUtils;

    @RabbitListener(queues = RabbitConstant.SMS_QUEUE_NAME)
    public void smsListener(SmsTo smsTo, Message message, Channel channel) throws IOException{
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String messageId = (String) headers.get("spring_returned_message_correlation");
        String key = RedisConstant.SMS_HASH_PREFIX + messageId;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        int retry = Integer.parseInt(redisTemplate.opsForHash().get(key, "retry").toString());
        if (retry > 3) {
            log.error("重试次数大于三次");
            channel.basicReject(deliveryTag, false);
            redisTemplate.delete(key);
            return;
        }
        try {
            String mobile = smsTo.getMobile();
            String code = smsTo.getCode();
            if (null == mobile || null == code){
                throw new RuntimeException("请求参数错误");
            }
            // 模拟发送短信
            SendSmsResponse sendSmsResponse = aliSmsUtils.sendSms(smsTo);
            String resCode = sendSmsResponse.getBody().getCode();
            if (!"OK".equals(resCode)){
                log.error("短信发送失败原因：{}",sendSmsResponse.getBody().getMessage());
                throw new RemoteException(sendSmsResponse.getBody().getMessage());
            }
            log.info("发送短信成功，手机号：{},验证码:{}",smsTo.getMobile(),smsTo.getCode());
            channel.basicAck(deliveryTag,false);
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("手机号{}第{}次重试",smsTo.getMobile(),retry);
            redisTemplate.opsForHash().put(key,"retry",retry+1);
            channel.basicReject(deliveryTag, true);
        }
    }

    /**
     * 监听死信队列 - 记录发送短信失败后的日志
     * （可以记录日志，入库，人工干预处理）
     * @param sms
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = RabbitConstant.SMS_DLX_NAME)
    public void delayListener(SmsTo sms, Message message, Channel channel) throws IOException {
        try{
            log.error("监听到死信队列消息==>{}",sms);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
