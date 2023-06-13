package com.edkai.kaiapiinterface.controller;

import com.edkai.common.BaseResponse;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.common.utils.ResultUtils;
import com.edkai.kaiapiinterface.modle.entity.WeatherVo;
import com.edkai.kaiapiinterface.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lk
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Resource
    private WeatherService weatherService;

    @GetMapping("/get")
    public BaseResponse<WeatherVo> getWeatherByCity(String city){
        WeatherVo weatherVo = weatherService.getWeatherByCity(city);
        return ResultUtils.success(weatherVo);
    }
}
