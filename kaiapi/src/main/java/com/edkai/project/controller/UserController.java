package com.edkai.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;

import com.edkai.common.utils.AuthPhoneNumberUtil;
import com.edkai.common.exception.BusinessException;
import com.edkai.common.BaseResponse;
import com.edkai.project.common.DeleteRequest;
import com.edkai.common.ErrorCode;
import com.edkai.common.utils.ResultUtils;
import com.edkai.project.model.dto.user.*;

import com.edkai.project.model.entity.User;
import com.edkai.project.model.vo.LoginUserVo;
import com.edkai.project.model.vo.UserVO;
import com.edkai.project.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 *
 * @author kai
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;



    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest,HttpServletRequest request) {
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        long result = userService.userRegister(userRegisterRequest,request);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVo> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVo loginUserVo = userService.userLogin(userAccount, userPassword);
        return ResultUtils.success(loginUserVo);
    }

    /**
     * 发送短信
     * @param phoneNum
     * @return
     */
    @ApiOperation("发送短信验证码")
    @GetMapping("/sendSms")
    public BaseResponse<Boolean> sendSms(String phoneNum){
        if (StringUtils.isEmpty(phoneNum)|| !AuthPhoneNumberUtil.isPhoneNum(phoneNum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"手机号输入错误");
        }
        boolean result=userService.senSms(phoneNum);
        return ResultUtils.success(result);
    }

    /**
     * 生成图形验证码
     * @param request
     * @param response
     */
    @ApiOperation("生成图形验证码")
    @GetMapping("/getCaptcha")
    public void getCaptcha(HttpServletRequest request,HttpServletResponse response){
        userService.getCaptcha(request,response);
    }

    /**
     * 短信登录
     * @param userLoginBySmsRequest
     * @return
     */
    @ApiOperation("用户通过手机号进行登录")
    @PostMapping("/loginBySms")
    public BaseResponse<LoginUserVo> loginBySms(@RequestBody UserLoginBySmsRequest userLoginBySmsRequest){
        if (userLoginBySmsRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVo loginUserVo=userService.loginBySms(userLoginBySmsRequest);
        return ResultUtils.success(loginUserVo);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }


    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserVO> getUserById(int id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取全部用户数用户
     */
    @GetMapping("/getAllUserCount")
    public BaseResponse<Long> getAllUserCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("isDelete",0);
        return ResultUtils.success(userService.count(queryWrapper));
    }

    @GetMapping("/getGithubStart")
    public BaseResponse<String> getGithubStart(){
        return ResultUtils.success(userService.getGithubStart());
    }

    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        List<User> userList = userService.list(queryWrapper);
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }

    /**
     * 分页获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        long current = 1;
        long size = 10;
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    @ApiOperation("绑定用户手机号")
    @PostMapping("/bindPhone")
    public BaseResponse bindPhone(UserBindPhoneRequest userBindPhoneRequest,HttpServletRequest request){
        return userService.bindPhone(userBindPhoneRequest,request);
    }

}
