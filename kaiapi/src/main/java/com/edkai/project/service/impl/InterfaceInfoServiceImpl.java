package com.edkai.project.service.impl;

import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edkai.common.model.entity.InterfaceInfo;

import com.edkai.common.model.entity.UserInterfaceInfo;
import com.edkai.kaiapiclientsdk.client.KaiApiClient;
import com.edkai.kaiapiclientsdk.modle.entity.Api;
import com.edkai.project.common.BranchDeleteRequest;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.project.mapper.UserInterfaceInfoMapper;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.edkai.project.model.entity.User;
import com.edkai.project.model.enums.InterfaceInfoStatusEnum;
import com.edkai.project.model.enums.RequestMethodEnum;
import com.edkai.project.model.vo.InterfaceDetailVo;
import com.edkai.project.model.vo.InterfaceInfoVo;
import com.edkai.project.model.vo.ManageInterfaceVo;
import com.edkai.project.service.InterfaceInfoService;
import com.edkai.project.mapper.InterfaceInfoMapper;
import com.edkai.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MyASUS
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-02-01 12:06:32
 */
@Service
@Slf4j
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {
    private static final String GATEWAY_HOST="http://localhost:8090";
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;


    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();
        String descrption = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();
        Long userId = interfaceInfo.getUserId();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, descrption, url, requestHeader, responseHeader, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50L) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public Object invoke(InterfaceInvokeRequest interfaceInvokeRequest, HttpServletRequest request) {
        if (interfaceInvokeRequest == null || interfaceInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInvokeRequest.getId();
        String userRequestParams = interfaceInvokeRequest.getUserRequestParams();
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口不存在");
        }
        if (interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        KaiApiClient kaiApiClient = new KaiApiClient(accessKey, secretKey);
        //  接口调用
        String method = interfaceInfo.getMethod();
        HttpResponse result = null;
        Api api = new Api();
        api.setInterfaceId(interfaceInfo.getId());
        api.setUrl(interfaceInfo.getUrl());
        if (RequestMethodEnum.POST.getText().equals(method)) {
            result = kaiApiClient.invokeByPost(userRequestParams, api);
        } else {
            result = kaiApiClient.invokeByGet(userRequestParams, api);
        }
        if (result.getStatus() != HttpStatus.OK.value()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作失败");
        }
        return result.body();
    }

    @Override
    public Page<InterfaceInfoVo> listInterfaceInfoByPage(int current, int pageSize, HttpServletRequest request) {
        // 限制爬虫
        if (pageSize > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //分页查询接口信息
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(true,true,"isFree");
        Page<InterfaceInfo> interfaceInfoPage = this.page(new Page<>(current, pageSize), queryWrapper);
        Page<InterfaceInfoVo> interfaceInfoVoPage = new Page<>();
        BeanUtils.copyProperties(interfaceInfoPage, interfaceInfoVoPage, "records");
        List<InterfaceInfo> records = interfaceInfoPage.getRecords();
        List<InterfaceInfoVo> collect = records.stream().map(interfaceInfo -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            String realUrl = interfaceInfo.getUrl();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVo);
            interfaceInfoVo.setUrl(secretUrl(realUrl));
            return interfaceInfoVo;
        }).collect(Collectors.toList());
        interfaceInfoVoPage.setRecords(collect);
        return interfaceInfoVoPage;
    }

    @Override
    public Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        this.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = this.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return interfaceInfo.getId();
    }

    @Override
    public Boolean deleteInterfaceInfo(BranchDeleteRequest branchDeleteRequest, HttpServletRequest request) {
        Long[] ids;
        if (branchDeleteRequest == null || (ids = branchDeleteRequest.getIds()) == null || ids.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (ids == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id不能为空");
        }
        // 仅管理员能删
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //批量删除
        if (ids.length > 1) {
            boolean b = this.removeBatchByIds(Arrays.asList(ids));
            return b;
        }
        long id = ids[0];
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅管理员或本人能删
        User user = userService.getLoginUser(request);
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = this.removeById(id);
        return b;
    }

    @Override
    public boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        this.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = this.updateById(interfaceInfo);
        return result;
    }

    @Override
    public Page<ManageInterfaceVo> listInterfaceInfoAllByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<ManageInterfaceVo> manageInterfaceVoPage=interfaceInfoMapper.selectAllPage(new Page<ManageInterfaceVo>(current,size),interfaceInfoQueryRequest);
        return manageInterfaceVoPage;
    }

    @Override
    public InterfaceDetailVo getInterfaceInfoById(long id,HttpServletRequest request) {
        if (id<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        InterfaceDetailVo interfaceDetailVo = new InterfaceDetailVo();
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String realUrl = interfaceInfo.getUrl();
        interfaceInfo.setUrl(secretUrl(realUrl));
        BeanUtils.copyProperties(interfaceInfo,interfaceDetailVo);
        // 判断接口是否免费，免费设置剩余次数为-1，接口不免费则查询表
        if (interfaceInfo.getIsFree() == 1){
            interfaceDetailVo.setLeftNum(-1);
            return interfaceDetailVo;
        }
        // 接口收费
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<UserInterfaceInfo>().eq("userId", loginUser.getId())
                .eq("interfaceInfoId", interfaceInfo.getId());
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        // 没有接口与用户信息则设置剩余次数为0
        if (userInterfaceInfo == null){
            interfaceDetailVo.setLeftNum(0);
        }else {
            // 有用户接口调用信息则设置调用信息。
            interfaceDetailVo.setLeftNum(userInterfaceInfo.getLeftNum());
        }
        return interfaceDetailVo;
    }


    public String secretUrl(String url){
        String[] split = url.split("/interface");
        return GATEWAY_HOST+"/interface/"+split[1];
    }
}




