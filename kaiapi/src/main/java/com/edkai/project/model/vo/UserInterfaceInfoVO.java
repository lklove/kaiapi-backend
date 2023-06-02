package com.edkai.project.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口视图
 * @author lk
 */
@Data
public class UserInterfaceInfoVO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口描述
     */
    private String interfaceDescription;

    /**
     * 总的调用次数）
     */
    private Integer totalNum;

    /**
     * 剩余调用次数）
     */
    private Integer leftNum;

    private static final long serialVersionUID = 1L;
}
