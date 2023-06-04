package com.edkai.thrid.controller;

import cn.hutool.core.date.DateUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.edkai.common.ErrorCode;
import com.edkai.common.constant.RedisConstant;
import com.edkai.common.exception.BusinessException;
import com.edkai.thrid.common.RabbitOrderPaySuccessUtils;
import com.edkai.thrid.config.AliPayConfig;
import com.edkai.thrid.modle.dto.AliPayDto;
import com.edkai.thrid.modle.entity.AlipayInfo;
import com.edkai.thrid.service.AlipayInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lk
 */
@RestController
@RequestMapping("/alipay")
public class PayController {
    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private AlipayInfoService alipayInfoService;

    @Resource
    private RabbitOrderPaySuccessUtils rabbitOrderPaySuccessUtils;

    @PostMapping("/pay")
    public void aliPay(AliPayDto aliPayDto, HttpServletResponse response) throws IOException {
        AlipayClient alipayClient = aliPayConfig.alipayClient();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //设置支付成功的异步回调页面
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        String subject =aliPayDto.getSubject()+"-"+aliPayDto.getTraceNo();
        request.setBizContent("{\"out_trade_no\":\"" + aliPayDto.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPayDto.getTotalAmount() + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"请求支付失败请重试");
        }
        response.setContentType("text/html;charset=" +aliPayConfig.getCHARSET());
        // 直接将完整的表单html输出到页面
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 支付成功请求
     */
    @PostMapping("/notify")
    public void paySuccessNotify(HttpServletRequest request) throws AlipayApiException {
        if(request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            if (AlipaySignature.rsaCheckV1(params,aliPayConfig.getALIPAY_PUBLIC_KEY(),
                    aliPayConfig.getCHARSET(),aliPayConfig.getSIGN_TYPE())){
                // 保证接口的幂等性
                String out_trade_no = params.get("out_trade_no");
                String key = RedisConstant.ALIPAY_TRADE_INFO + out_trade_no;
                Object outTradeNo = redisTemplate.opsForValue().get(key);
                if (outTradeNo == null){
                    // 订单信息入库
                    AlipayInfo alipayInfo = new AlipayInfo();
                    alipayInfo.setOrdersn(out_trade_no);
                    alipayInfo.setSubject(params.get("subject"));
                    alipayInfo.setTotalamount(new BigDecimal(params.get("total_amount")));
                    alipayInfo.setBuyerpayamount(new BigDecimal(params.get("buyer_pay_amount")));
                    alipayInfo.setBuyerid(params.get("buyer_id"));
                    alipayInfo.setTradeno(params.get("trade_no"));
                    alipayInfo.setTradestatus(params.get("trade_status"));
                    alipayInfo.setGmtpayment(DateUtil.parse(params.get("gmt_payment")));
                    alipayInfoService.save(alipayInfo);
                    redisTemplate.opsForValue().set(key,alipayInfo,30, TimeUnit.MINUTES);
                    rabbitOrderPaySuccessUtils.sendPaySuccess(out_trade_no);
                }
            }
        }
    }

    @GetMapping("/test")
    public String tetsHh(){
        return "hahaha";
    }
}
