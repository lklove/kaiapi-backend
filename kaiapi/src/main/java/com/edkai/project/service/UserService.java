package com.edkai.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edkai.common.BaseResponse;
import com.edkai.project.model.dto.user.UserBindPhoneRequest;
import com.edkai.project.model.dto.user.UserLoginBySmsRequest;
import com.edkai.project.model.dto.user.UserRegisterRequest;
import com.edkai.project.model.entity.User;
import com.edkai.project.model.vo.LoginUserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author lk
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-05-23 14:59:13
*/
public interface UserService extends IService<User> {
    /**
     * 手机号发送验证码
     * @param phoneNum 手机号
     * @return 是否发送成功
     */
    boolean senSms(String phoneNum);

    /**
     * 是否为管理源
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 获取登录用户
     * @return 登录用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户登录
     * @param userAccount 账户
     * @param userPassword 密码
     * @return
     */
    LoginUserVo userLogin(String userAccount, String userPassword);

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求体
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest,HttpServletRequest request);

    /**
     * 用户注销
     * @param request 请求体
     * @return 是否注销成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取验证码
     * @param request request请求体
     * @param response 响应体，用户输出验证码
     */
    void getCaptcha(HttpServletRequest request, HttpServletResponse response);

    /**
     * 短信登录
     * @param userLoginBySmsRequest 短信的请求参数
     * @return 用户登录vo
     */
    LoginUserVo loginBySms(UserLoginBySmsRequest userLoginBySmsRequest);

    /**
     * 获取github start数
     * @return start数
     */
    String getGithubStart();

    BaseResponse bindPhone(UserBindPhoneRequest userBindPhoneRequest, HttpServletRequest request);
}
