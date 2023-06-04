package com.edkai.order.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单状态枚举类
 * @author lk
 */
public enum OrderStatusEnum {

    /**
     * 待付款
     */
    TO_BE_PAID("待付款",0),
    /**
     * 完成
     */
    FINISH("完成",1),
    /**
     * 关闭
     */
    CLOSE("关闭",2),
    /**
     * 无效订单
     */
    INVALID("无效订单",3);

    private final String text;

    private final int value;

    OrderStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

}
