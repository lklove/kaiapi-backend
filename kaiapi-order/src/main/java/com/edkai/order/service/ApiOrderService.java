package com.edkai.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.common.BaseResponse;
import com.edkai.order.model.dto.order.ApiOrderCancelDto;
import com.edkai.order.model.dto.order.GenerateOrderRequest;
import com.edkai.order.model.dto.order.OrderQueryRequest;
import com.edkai.order.model.entity.ApiOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edkai.order.model.vo.OrderDetailVo;
import com.edkai.order.model.vo.OrderVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author ASUS
* @description 针对表【api_order】的数据库操作Service
* @createDate 2023-06-02 13:50:03
*/
public interface ApiOrderService extends IService<ApiOrder> {
    /**
     * 生成唯一id 保证接口的幂等性消费
     * @param id 用户id
     * @param response 响应体
     * @return
     */
    BaseResponse generateToken(Long id, HttpServletRequest request,HttpServletResponse response);

    /**
     * 生成订单
     *
     * @param generateOrderRequest 生成订单dto
     * @param request
     * @return
     */
    OrderVo generateOrder(GenerateOrderRequest generateOrderRequest, HttpServletRequest request);

    /**
     * 更新订单状态为 支付完成。
     * @param aliOrderSn
     */
    void updateOrderSuccess(String aliOrderSn);

    /**
     * 更新订单状态为 关闭（超时未付款）
     * @param apiOrder
     */
    void updateOrderClose(ApiOrder apiOrder);

    /**
     * 取消订单
     * @param apiOrderCancelDto
     * @param request
     * @param response
     * @return
     */
    BaseResponse cancelOrderSn(ApiOrderCancelDto apiOrderCancelDto, HttpServletRequest request, HttpServletResponse response);

    Page<OrderDetailVo> getOrderInfoByPage(OrderQueryRequest orderQueryRequest);
}
