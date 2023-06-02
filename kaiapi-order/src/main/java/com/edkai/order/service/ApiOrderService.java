package com.edkai.order.service;

import com.edkai.common.BaseResponse;
import com.edkai.order.model.entity.ApiOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
* @author ASUS
* @description 针对表【api_order】的数据库操作Service
* @createDate 2023-06-02 13:50:03
*/
public interface ApiOrderService extends IService<ApiOrder> {

    BaseResponse generateToken(Long id, HttpServletResponse response);
}
