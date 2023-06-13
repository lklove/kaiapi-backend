package com.edkai.kaiapiinterface.modle.entity;

import lombok.Data;

/**
 * @author lk
 */
@Data
public class WeatherVo {
    /**
     * 城市
     */
    private String city;

    /**
     * 当前气温
     */
    private String nowTemp;

    /**
     * 天气。如阴天
     */
    private String weather;

    /**
     * 最高最低气温
     */
    private String minMaxTemp;

    /**
     * 湿度
     */
    private String humidity;

    /**
     * 风向
     */
    private String windDire;
    /**
     * 紫外线
     */
    private String ultravioletLight;
    /**
     * 空气质量
     */
    private String airQuality;
    /**
     * pm2.5
     */
    private String pM;
}
