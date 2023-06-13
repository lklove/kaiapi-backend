package com.edkai.kaiapiinterface.service;

import com.edkai.kaiapiinterface.modle.entity.WeatherVo;

/**
 *
 *获取天气接口
 * @author lk
 */
public interface WeatherService {
    /**
     * 根据城市获取天气，城市为小写拼音
     * @param city 城市为小写拼音
     * @return 天气信息
     */
    WeatherVo getWeatherByCity(String city);
}
