package com.edkai.common.service;

import com.edkai.common.model.to.AddUserInterfaceTo;

/**
 * @author lk
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 接口剩余调用次数
     * @param interfaceInfoId
     * @param userId
     * @return 剩余次数
     */
    int leftNumOfInterface(long interfaceInfoId, long userId);

    /**
     * 添加用户调用信息
     * @param addUserInterfaceTo
     * @return
     */
    boolean addUserInterface(AddUserInterfaceTo addUserInterfaceTo);
}
