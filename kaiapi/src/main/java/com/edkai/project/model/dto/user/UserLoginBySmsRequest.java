package com.edkai.project.model.dto.user;

import lombok.Data;

/**
 * 短信登录的请求参数
 * @author lk
 *
 */
@Data
public class UserLoginBySmsRequest {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String code;
}
