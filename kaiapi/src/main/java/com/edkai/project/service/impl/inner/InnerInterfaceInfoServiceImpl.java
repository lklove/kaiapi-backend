package com.edkai.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.common.model.vo.InterfaceInfoVo;
import com.edkai.common.service.InnerInterfaceInfoService;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.project.mapper.InterfaceInfoMapper;
import com.edkai.project.service.InterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Override
    public InterfaceInfo getInterfaceInfo(Long interfaceId) {
        if (interfaceId == null || interfaceId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",interfaceId);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public InterfaceInfoVo innerGetInterfaceInfoById(long id) {
        return interfaceInfoService.innerGetInterfaceInfoById(id);
    }
}
