package com.edkai.common.model.to;

import lombok.Data;

import java.io.Serializable;

/**
 * 远程调用的传输类
 * @author lk
 */
@Data
public class AddUserInterfaceTo implements Serializable {

    private static final long serialVersionUID = -1008928736147305967L;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 购买数量
     */
    private Long orderNum;
    /**
     * 接口id
     */
    private Long interfaceId;
}
