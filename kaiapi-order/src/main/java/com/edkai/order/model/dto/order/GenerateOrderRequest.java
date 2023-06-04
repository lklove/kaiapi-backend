package com.edkai.order.model.dto.order;

import lombok.Data;


/**
 *
 * @author lk
 */
@Data
public class GenerateOrderRequest {

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 购买数量
     */
    private Long orderNum;

}
