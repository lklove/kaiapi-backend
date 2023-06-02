package com.edkai.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liukai
 */
@Data
public class InterfaceInvokeRequest implements Serializable {
    /**
     * 接口id
     */
    private Long id;

    /**
     *  用户请求参数
     *  get请求时 参数名=参数值 post请求时 为json字符串
     */
    private String userRequestParams;
}
