package com.edkai.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.common.BaseResponse;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.common.utils.ResultUtils;
import com.edkai.order.model.dto.order.ApiOrderCancelDto;
import com.edkai.order.model.dto.order.GenerateOrderRequest;
import com.edkai.order.model.dto.order.OrderQueryRequest;
import com.edkai.order.model.entity.ApiOrder;
import com.edkai.order.model.enums.OrderStatusEnum;
import com.edkai.order.model.vo.OrderDetailVo;
import com.edkai.order.model.vo.OrderVo;
import com.edkai.order.service.ApiOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class OrderController {
    @Resource
    private ApiOrderService apiOrderService;
    /**
     * 生成防重令牌：保证创建订单的接口幂等性
     * @param id
     * @param response
     * @return
     */
    @GetMapping("/generateToken")
    public BaseResponse generateToken(Long id, HttpServletResponse response){
        return apiOrderService.generateToken(id,response);
    }

    @PostMapping("/generateOrder")
    public BaseResponse<OrderVo> generateOrder(@RequestBody GenerateOrderRequest generateOrderRequest,HttpServletRequest request){
        if (generateOrderRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderVo orderVo = apiOrderService.generateOrder(generateOrderRequest,request);
        return ResultUtils.success(orderVo);
    }


    /**
     * 取消订单
     * @param apiOrderCancelDto
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/cancelOrderSn")
    public BaseResponse cancelOrderSn(ApiOrderCancelDto apiOrderCancelDto, HttpServletRequest request, HttpServletResponse response) {
        if (apiOrderCancelDto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return apiOrderService.cancelOrderSn(apiOrderCancelDto,request,response);
    }

    @GetMapping("/getOrderInfo")
    public BaseResponse<Page<OrderDetailVo>> getOrderInfoByPage(OrderQueryRequest orderQueryRequest){
        if (orderQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<OrderDetailVo> orderDetailVoPage=apiOrderService.getOrderInfoByPage(orderQueryRequest);
        return ResultUtils.success(orderDetailVoPage);
    }

    /**
     * 获取成功交易的订单数
     * @return
     */
    @GetMapping("/getOrderSuccess")
    public BaseResponse<Long> getOrderSuccessCount(){
        QueryWrapper<ApiOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", OrderStatusEnum.FINISH.getValue());
        return ResultUtils.success(apiOrderService.count(queryWrapper));
    }

}
