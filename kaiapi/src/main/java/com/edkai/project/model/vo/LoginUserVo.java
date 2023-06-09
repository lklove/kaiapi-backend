package com.edkai.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lk
 */
@Data
public class LoginUserVo implements Serializable{
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * token
     */
    private String token;
    /**
     * token 头部标识
     */
    private String tokenHead;

}
