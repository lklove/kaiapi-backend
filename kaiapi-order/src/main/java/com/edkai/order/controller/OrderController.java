package com.edkai.order.controller;

import com.edkai.common.BaseResponse;
import com.edkai.order.service.ApiOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
}
