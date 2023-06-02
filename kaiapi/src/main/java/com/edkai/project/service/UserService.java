package com.edkai.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
     * @param phoneNum
     * @return
     */
    boolean senSms(String phoneNum);

    boolean isAdmin(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    LoginUserVo userLogin(String userAccount, String userPassword, HttpServletResponse response);

    long userRegister(UserRegisterRequest userRegisterRequest,HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    void getCaptcha(HttpServletRequest request, HttpServletResponse response);

    LoginUserVo loginBySms(UserLoginBySmsRequest userLoginBySmsRequest, HttpServletResponse response);
}
