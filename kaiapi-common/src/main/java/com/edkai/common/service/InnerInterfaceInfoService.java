package com.edkai.common.service;

import com.edkai.common.model.entity.InterfaceInfo;

/**
 * @author lk
 */
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（根据接口id）
     * @param interfaceId 接口id
     * @return 接口信息。
     */
    InterfaceInfo getInterfaceInfo(Long interfaceId);
}