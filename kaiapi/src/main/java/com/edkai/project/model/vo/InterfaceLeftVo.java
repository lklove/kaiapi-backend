package com.edkai.project.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lk
 */
@Data
public class InterfaceLeftVo implements Serializable {
    private static final long serialVersionUID = -8937751640163047556L;
    /**
     * 接口id
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
     * 请求类型
     */
    private String method;

    /**
     * 描述
     */
    private String description;

    /**
     * 剩余调用次数）
     */
    private Integer leftNum;
}
