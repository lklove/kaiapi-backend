package com.edkai.project.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lk
 */
@Data
public class InterfaceDetailVo implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;
    /**
     * 接口状态 （0 - 关闭，1 - 开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求参数
     * [{"name":"username","type":"string"}]
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 剩余次数
     */
    private Integer isFree;

    /**
     * 剩余次数
     */
    private Integer leftNum;

}
