package com.edkai.thrid.modle.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lk
 */
@Data
public class AliPayDto {
    private String traceNo;
    private BigDecimal totalAmount;
    private String subject;
}
