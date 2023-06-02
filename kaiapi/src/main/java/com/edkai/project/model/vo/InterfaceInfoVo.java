package com.edkai.project.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.edkai.common.model.entity.InterfaceInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lk
 */
@Data
public class InterfaceInfoVo  {
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
     * 是否免费(0-收费, 1-免费)
     */
    private Integer isFree;

    /**
     * 计费规则（元/每条）
     */
    private BigDecimal charging;
}
