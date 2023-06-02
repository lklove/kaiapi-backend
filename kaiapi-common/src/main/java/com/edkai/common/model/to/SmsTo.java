package com.edkai.common.model.to;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsTo implements Serializable {
    private static final long serialVersionUID = -4522577855211592253L;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

}
