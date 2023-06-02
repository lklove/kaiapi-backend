package com.edkai.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author kai
 */
@Data
public class BranchDeleteRequest implements Serializable {
    /**
     * id
     */
    private Long[] ids;

    private static final long serialVersionUID = 1L;
}