package com.edkai.kaiapiinterface.controller;

import com.edkai.common.BaseResponse;
import com.edkai.common.utils.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @PostMapping("/send")
    public BaseResponse<String> sendSms(String phoneNum){
        return ResultUtils.success(phoneNum+"短信发送成功");
    }
}
