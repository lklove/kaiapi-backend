package com.edkai.project.service.impl.inner;

import com.edkai.common.model.to.AddUserInterfaceTo;
import com.edkai.common.service.InnerUserInterfaceInfoService;
import com.edkai.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(userId,interfaceInfoId);
    }

    @Override
    public int leftNumOfInterface(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.leftNumOfInterface(interfaceInfoId,userId);
    }

    @Override
    public boolean addUserInterface(AddUserInterfaceTo addUserInterfaceTo) {
        return userInterfaceInfoService.addUserInterface(addUserInterfaceTo);
    }
}
