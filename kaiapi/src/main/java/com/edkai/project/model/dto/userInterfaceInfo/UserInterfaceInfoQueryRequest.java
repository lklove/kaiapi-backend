package com.edkai.project.model.dto.userInterfaceInfo;

import com.edkai.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author kai
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 总的调用次数）
     */
    private Integer totalNum;

    /**
     * 剩余调用次数）
     */
    private Integer leftNum;


}