package com.edkai.project.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求方式枚举
 *
 * @author lk
 */
public enum RequestMethodEnum {

    POST("POST"),
    GET("GET");

    private final String text;

    RequestMethodEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
