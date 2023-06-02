package com.edkai.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的id请求
 * @author kai
 */
@Data
public class IdRequest implements Serializable {
    private static final long serialVersionUID = 8293589841428320535L;
    private Long id;

}
