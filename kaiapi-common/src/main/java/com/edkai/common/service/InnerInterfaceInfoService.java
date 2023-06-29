package com.edkai.common.service;

import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.common.model.vo.InterfaceInfoVo;

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
    /**
     * 根据id获取接口信息
     * @param id
     * @return
     */
    InterfaceInfoVo innerGetInterfaceInfoById(long id);
}