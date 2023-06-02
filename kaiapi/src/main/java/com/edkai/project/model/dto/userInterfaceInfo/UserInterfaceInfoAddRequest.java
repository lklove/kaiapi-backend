package com.edkai.project.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 接口名
     */
    private String interfaceName;


    /**
     * 剩余调用次数）
     */
    private Integer leftNum;


}