package com.edkai.order.listen;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edkai.common.BaseResponse;
import com.edkai.common.constant.RabbitConstant;
import com.edkai.common.constant.RedisConstant;
import com.edkai.common.model.to.AddUserInterfaceTo;
import com.edkai.order.feign.UserInterfaceFeignClient;
import com.edkai.order.model.entity.ApiOrder;
import com.edkai.order.model.enums.OrderStatusEnum;
import com.edkai.order.service.ApiOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author lk
 */
@Component
@Slf4j
public class OrderListener {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ApiOrderService apiOrderService;

    @Resource
    private UserInterfaceFeignClient userInterfaceFeignClient;

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = RabbitConstant.ORDER_SUCCESS_QUEUE_NAME)
    public void orderSuccess(String aliOrderSn, Message message, Channel channel) throws IOException {
        try {
            redisTemplate.delete(RedisConstant.ALIPAY_ORDER_SUCCESS + aliOrderSn);
            // 解决重复消费
            String key = RedisConstant.ORDER_PAY_SUCCESS + aliOrderSn;
            Object o = redisTemplate.opsForValue().get(key);
            if (null != o) {
                //已经成功处理过了，直接放行
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
            log.info("订单支付成功，订单号为===>：{}", aliOrderSn);
            //修改订单状态
            apiOrderService.updateOrderSuccess(aliOrderSn);
            //添加用户接口调用次数
            ApiOrder apiOrder = apiOrderService.getOne(new QueryWrapper<ApiOrder>().eq("orderSn", aliOrderSn));
            Long userId = apiOrder.getUserId();
            Long orderNum = apiOrder.getOrderNum();
            Long interfaceId = apiOrder.getInterfaceId();
            // Feign远程调用 添加用户调用次数
            AddUserInterfaceTo addUserInterfaceTo = new AddUserInterfaceTo();
            addUserInterfaceTo.setUserId(userId);
            addUserInterfaceTo.setOrderNum(orderNum);
            addUserInterfaceTo.setInterfaceId(interfaceId);
            BaseResponse<Boolean> response = userInterfaceFeignClient.addUserInterfaceByFeign(addUserInterfaceTo);
            if (!response.getData()){
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            }
            redisTemplate.opsForValue().set(key,aliOrderSn,30, TimeUnit.MINUTES);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("OderSuccessListener has exception,订单号：{}，异常信息:{}", aliOrderSn, e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }

    /**
     * 订单延时队列，处理
     * @param apiOrder
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = RabbitConstant.ORDER_DLX_NAME)
    public void orderTimeOut(ApiOrder apiOrder,Message message,Channel channel) throws IOException {
        try {
            log.info("监听到订单超时，订单信息为：{}",apiOrder);
            // 订单超时未支付处理，已支付的直接ack
            Long id = apiOrder.getId();
            // 查询最新订单信息
            ApiOrder newApiOrder = apiOrderService.getById(id);
            if (OrderStatusEnum.TO_BE_PAID.getValue()== newApiOrder.getStatus()){
                apiOrderService.updateOrderClose(apiOrder);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("订单延时队列发送异常，异常信息为：{}",e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            throw new RuntimeException(e);
        }
    }
}
