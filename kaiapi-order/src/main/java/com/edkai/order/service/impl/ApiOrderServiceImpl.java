package com.edkai.order.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edkai.common.BaseResponse;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.common.model.vo.InterfaceInfoVo;
import com.edkai.common.model.vo.UserVO;
import com.edkai.common.service.InnerInterfaceInfoService;
import com.edkai.common.utils.ResultUtils;
import com.edkai.order.common.OrderRabbitMq;
import com.edkai.order.constants.CookieConstant;
import com.edkai.order.constants.OrderConstant;
import com.edkai.order.feign.UserFeignService;
import com.edkai.order.model.dto.order.ApiOrderCancelDto;
import com.edkai.order.model.dto.order.GenerateOrderRequest;
import com.edkai.order.model.dto.order.OrderQueryRequest;
import com.edkai.order.model.entity.ApiOrder;
import com.edkai.order.model.enums.OrderStatusEnum;
import com.edkai.order.model.vo.OrderDetailVo;
import com.edkai.order.model.vo.OrderVo;
import com.edkai.order.service.ApiOrderService;
import com.edkai.order.mapper.ApiOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ASUS
 * @description 针对表【api_order】的数据库操作Service实现
 * @createDate 2023-06-02 13:50:03
 */
@Service
@Slf4j
public class ApiOrderServiceImpl extends ServiceImpl<ApiOrderMapper, ApiOrder>
        implements ApiOrderService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserFeignService userFeignService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @Resource
    private ApiOrderMapper apiOrderMapper;

    @Resource
    private OrderRabbitMq orderRabbitMq;

    @Override
    public BaseResponse generateToken(Long id, HttpServletRequest request,HttpServletResponse response) {
        if (null == id) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        //防重令牌
        String token = IdUtil.simpleUUID();
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + id, token, 30, TimeUnit.MINUTES);
        Cookie cookie = new Cookie(CookieConstant.ORDER_TOKEN, token);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setMaxAge(CookieConstant.ORDER_TOKEN_EXPIRE_TIME);
        response.addCookie(cookie);
        return ResultUtils.success(null);
    }

    @Override
    public OrderVo generateOrder(GenerateOrderRequest generateOrderRequest, HttpServletRequest request) {
        if (generateOrderRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceId = generateOrderRequest.getInterfaceId();
        Long userId = generateOrderRequest.getUserId();
        Long orderNum = generateOrderRequest.getOrderNum();
        if (interfaceId <= 0 || userId <= 0 || orderNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询订单token 保证接口的幂等性
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies) {
            if (CookieConstant.ORDER_TOKEN.equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }
        if (token == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 远程获取当前登录用户
        BaseResponse<UserVO> responseUserVo = userFeignService.getLoginUser();
        UserVO loginUser = responseUserVo.getData();
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 判断前端传来的用户信息有无串改
        Long loginUserId = loginUser.getId();
        if (!userId.equals(loginUserId)){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        // 保证删除和获取为原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + loginUserId),
                token);
        if (result == null || result == 0L) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交太快了，请重新提交");
        }
        InterfaceInfoVo interfaceInfoVo = innerInterfaceInfoService.innerGetInterfaceInfoById(interfaceId);
        if (interfaceInfoVo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (1==interfaceInfoVo.getIsFree()){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        BigDecimal charging = interfaceInfoVo.getCharging();
        BigDecimal totalAmount = charging.multiply(BigDecimal.valueOf(orderNum)).setScale(2, RoundingMode.HALF_UP);
        // 雪花算法生成订单
        String orderSn = generateOrderSn(loginUserId.toString());
        ApiOrder apiOrder = new ApiOrder();
        apiOrder.setInterfaceId(interfaceId);
        apiOrder.setUserId(loginUserId);
        apiOrder.setOrderSn(orderSn);
        apiOrder.setOrderNum(orderNum);
        apiOrder.setCharging(charging);
        apiOrder.setTotalAmount(totalAmount);
        apiOrder.setStatus(OrderStatusEnum.TO_BE_PAID.getValue());
        try {
            apiOrderMapper.insert(apiOrder);
        } catch (Exception e) {
            log.error("订单插入失败,{}",e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"订单保存失败");
        }
        // 发送mq消息
        orderRabbitMq.sendOrder(apiOrder);
        //返回页面信息
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(interfaceInfoVo,orderVo);
        orderVo.setInterfaceId(interfaceId);
        orderVo.setUserId(userId);
        orderVo.setOrderSn(orderSn);
        orderVo.setOrderNum(orderNum);
        orderVo.setTotalAmount(totalAmount);
        DateTime date = DateUtil.date();
        orderVo.setCreateTime(date);
        orderVo.setExpirationTime(DateUtil.offset(date, DateField.MINUTE,30));
        return orderVo;
    }

    @Override
    public void updateOrderSuccess(String aliOrderSn) {
        UpdateWrapper<ApiOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("orderSn",aliOrderSn)
                .set("status",OrderStatusEnum.FINISH.getValue());
        this.update(updateWrapper);
    }

    @Override
    public void updateOrderClose(ApiOrder apiOrder) {
        this.update(new UpdateWrapper<ApiOrder>().eq("orderSn",apiOrder.getOrderSn()).
                set("status",OrderStatusEnum.CLOSE.getValue()));
    }

    @Override
    public BaseResponse cancelOrderSn(ApiOrderCancelDto apiOrderCancelDto, HttpServletRequest request, HttpServletResponse response) {
        String orderSn = apiOrderCancelDto.getOrderSn();
        //订单已经被取消的情况
        ApiOrder orderSn1 = this.getOne(new QueryWrapper<ApiOrder>().eq("orderSn", orderSn));
        if (orderSn1.getStatus() == 2||orderSn1.getStatus()==3){
            return ResultUtils.success("取消订单成功");
        }
        //更新订单表状态
        boolean update = this.update(new UpdateWrapper<ApiOrder>().eq("orderSn", orderSn).set("status", OrderStatusEnum.INVALID.getValue()));
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"订单取消失败");
        }
        return ResultUtils.success("取消订单成功");
    }

    @Override
    public Page<OrderDetailVo> getOrderInfoByPage(OrderQueryRequest orderQueryRequest) {
        if (orderQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = orderQueryRequest.getUserId();
        if (userId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = orderQueryRequest.getCurrent();
        // 限制爬虫
        long size = orderQueryRequest.getPageSize();
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer status = orderQueryRequest.getStatus();
        Page<OrderDetailVo> orderDetailVoPage = apiOrderMapper.getCurrentOrderInfo(new Page<>(current, size),userId,status);
        List<OrderDetailVo> records = orderDetailVoPage.getRecords();
        List<OrderDetailVo> collect = records.stream().map(record -> {
            Integer status1 = record.getStatus();
            if (status1 == 0) {
                Date date = record.getCreateTime();
                record.setExpirationTime(DateUtil.offset(date, DateField.MINUTE, 30));
            }
            return record;
        }).collect(Collectors.toList());
        orderDetailVoPage.setRecords(collect);
        return orderDetailVoPage;
    }

    /**
     * 生成订单号
     * @return
     */
    private String generateOrderSn(String userId){
        String timeId = IdWorker.getTimeId();
        String substring = timeId.substring(0, timeId.length() - 15);
        return substring + RandomUtil.randomNumbers(5) + userId.substring(userId.length()-2,userId.length());
    }
}



