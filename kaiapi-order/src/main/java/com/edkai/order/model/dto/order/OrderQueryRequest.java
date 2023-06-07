package com.edkai.order.model.dto.order;

import com.edkai.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lk
 */
@Data
public class OrderQueryRequest extends PageRequest implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 交易状态【0->待付款；1->已完成；2->超时订单 ，3->取消订单】
     */
    private Integer status;
}
