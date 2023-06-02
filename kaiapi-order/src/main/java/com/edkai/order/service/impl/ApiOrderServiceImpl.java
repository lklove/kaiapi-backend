package com.edkai.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edkai.common.BaseResponse;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.common.utils.ResultUtils;
import com.edkai.order.constants.CookieConstant;
import com.edkai.order.constants.OrderConstant;
import com.edkai.order.model.entity.ApiOrder;
import com.edkai.order.service.ApiOrderService;
import com.edkai.order.mapper.ApiOrderMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
* @author ASUS
* @description 针对表【api_order】的数据库操作Service实现
* @createDate 2023-06-02 13:50:03
*/
@Service
public class ApiOrderServiceImpl extends ServiceImpl<ApiOrderMapper, ApiOrder>
    implements ApiOrderService{
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public BaseResponse generateToken(Long id, HttpServletResponse response) {
        if (null == id){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        //防重令牌
        String token = IdUtil.simpleUUID();
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + id,token,30, TimeUnit.MINUTES);
        Cookie cookie = new Cookie(CookieConstant.ORDER_TOKEN,token);
        cookie.setPath("/");
        cookie.setMaxAge(CookieConstant.ORDER_TOKEN_EXPIRE_TIME);
        response.addCookie(cookie);
        return ResultUtils.success(null);
    }
}




