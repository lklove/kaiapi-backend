package com.edkai.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.edkai.common.BaseResponse;
import com.edkai.common.ErrorCode;
import com.edkai.common.model.entity.UserInterfaceInfo;
import com.edkai.common.model.to.AddUserInterfaceTo;
import com.edkai.common.utils.ResultUtils;
import com.edkai.project.annotation.AuthCheck;
import com.edkai.project.common.*;
import com.edkai.common.exception.BusinessException;
import com.edkai.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.edkai.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.edkai.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.edkai.project.model.entity.User;
import com.edkai.project.model.vo.InterfaceLeftVo;
import com.edkai.project.model.vo.UserInterfaceInfoVO;
import com.edkai.project.service.UserInterfaceInfoService;
import com.edkai.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 用户接口关系
 *
 * @author kai
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long newUserInterfaceInfoId=userInterfaceInfoService.addUserInterfaceInfo(userInterfaceInfoAddRequest,request);
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param branchDeleteRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody BranchDeleteRequest branchDeleteRequest, HttpServletRequest request) {
        if (branchDeleteRequest == null || branchDeleteRequest.getIds().length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        Long[] ids = branchDeleteRequest.getIds();
        // 仅本人或管理员可删除
        if ( !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //批量删除
        if (ids.length>1){
            boolean b = userInterfaceInfoService.removeBatchByIds(Arrays.asList(ids));
            return ResultUtils.success(b);
        }
        Long id =ids[0];
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        boolean result = userInterfaceInfoService.updateUserInterfaceInfo(userInterfaceInfo,request);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        if (userInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<UserInterfaceInfoVO>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<UserInterfaceInfoVO> userInterfaceInfoPage = userInterfaceInfoService.listUserInterfaceInfoByPage(userInterfaceInfoQueryRequest,request);
        return ResultUtils.success(userInterfaceInfoPage);
    }

    @GetMapping("/getUserInterfaceLeftNum")
    public BaseResponse<Page<InterfaceLeftVo>> listUserInterfaceLeftNumByPage(@RequestParam(defaultValue = "1", name = "current") int current,
                                                                              @RequestParam(defaultValue = "5", name = "size") int pageSie,
                                                                              HttpServletRequest request) {
        Page<InterfaceLeftVo> interfaceLeftVoPage = userInterfaceInfoService.listUserInterfaceLeftNumByPage(current, pageSie, request);
        return ResultUtils.success(interfaceLeftVoPage);
    }



}
