package com.edkai.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.edkai.common.model.entity.User;
import com.edkai.common.service.InnerUserService;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.project.mapper.UserMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<com.edkai.project.model.entity.User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        com.edkai.project.model.entity.User user2 = userMapper.selectOne(queryWrapper);
        User user = new User();
        BeanUtils.copyProperties(user2,user);
        return user;
    }
}
