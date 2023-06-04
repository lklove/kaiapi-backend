package com.edkai.thrid.modle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName alipay_info
 */
@TableName(value ="alipay_info")
@Data
public class AlipayInfo implements Serializable {
    /**
     * 订单id
     */
    @TableId
    private String ordersn;

    /**
     * 交易名称
     */
    private String subject;

    /**
     * 交易金额
     */
    private BigDecimal totalamount;

    /**
     * 买家付款金额
     */
    private BigDecimal buyerpayamount;

    /**
     * 买家在支付宝的唯一id
     */
    private String buyerid;

    /**
     * 支付宝交易凭证号
     */
    private String tradeno;

    /**
     * 交易状态
     */
    private String tradestatus;

    /**
     * 买家付款时间
     */
    private Date gmtpayment;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AlipayInfo other = (AlipayInfo) that;
        return (this.getOrdersn() == null ? other.getOrdersn() == null : this.getOrdersn().equals(other.getOrdersn()))
            && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
            && (this.getTotalamount() == null ? other.getTotalamount() == null : this.getTotalamount().equals(other.getTotalamount()))
            && (this.getBuyerpayamount() == null ? other.getBuyerpayamount() == null : this.getBuyerpayamount().equals(other.getBuyerpayamount()))
            && (this.getBuyerid() == null ? other.getBuyerid() == null : this.getBuyerid().equals(other.getBuyerid()))
            && (this.getTradeno() == null ? other.getTradeno() == null : this.getTradeno().equals(other.getTradeno()))
            && (this.getTradestatus() == null ? other.getTradestatus() == null : this.getTradestatus().equals(other.getTradestatus()))
            && (this.getGmtpayment() == null ? other.getGmtpayment() == null : this.getGmtpayment().equals(other.getGmtpayment()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrdersn() == null) ? 0 : getOrdersn().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getTotalamount() == null) ? 0 : getTotalamount().hashCode());
        result = prime * result + ((getBuyerpayamount() == null) ? 0 : getBuyerpayamount().hashCode());
        result = prime * result + ((getBuyerid() == null) ? 0 : getBuyerid().hashCode());
        result = prime * result + ((getTradeno() == null) ? 0 : getTradeno().hashCode());
        result = prime * result + ((getTradestatus() == null) ? 0 : getTradestatus().hashCode());
        result = prime * result + ((getGmtpayment() == null) ? 0 : getGmtpayment().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ordersn=").append(ordersn);
        sb.append(", subject=").append(subject);
        sb.append(", totalamount=").append(totalamount);
        sb.append(", buyerpayamount=").append(buyerpayamount);
        sb.append(", buyerid=").append(buyerid);
        sb.append(", tradeno=").append(tradeno);
        sb.append(", tradestatus=").append(tradestatus);
        sb.append(", gmtpayment=").append(gmtpayment);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}