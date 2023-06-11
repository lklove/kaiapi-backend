package com.edkai.project.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edkai.common.BaseResponse;
import com.edkai.common.constant.RedisConstant;
import com.edkai.common.model.to.SmsTo;
import com.edkai.common.utils.AuthPhoneNumberUtil;
import com.edkai.common.ErrorCode;
import com.edkai.common.utils.ResultUtils;
import com.edkai.project.common.RabbitMqUtils;
import com.edkai.project.common.SmsLimiter;
import com.edkai.project.config.security.component.JwtTokenUtil;
import com.edkai.project.constant.UserConstant;
import com.edkai.common.exception.BusinessException;
import com.edkai.project.mapper.UserMapper;
import com.edkai.project.model.dto.user.UserBindPhoneRequest;
import com.edkai.project.model.dto.user.UserLoginBySmsRequest;
import com.edkai.project.model.dto.user.UserRegisterRequest;
import com.edkai.project.model.entity.User;
import com.edkai.project.model.vo.LoginUserVo;
import com.edkai.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lk
 * @createDate 2023-05-23 14:59:13
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT = "user-register";

    @Resource
    private SmsLimiter smsLimiter;

    @Resource
    private RabbitMqUtils rabbitMqUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    @Lazy
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserMapper userMapper;


    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public boolean senSms(String phoneNum) {
        if (StringUtils.isEmpty(phoneNum) || !AuthPhoneNumberUtil.isPhoneNum(phoneNum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号输入错误");
        }
        // 删除处于发送失败的消息，防止定时任务对手机号再次重新发送消息
        redisTemplate.opsForHash().delete(RedisConstant.SMS_MQ_PRODUCE_FAIL, phoneNum);
        //生产验证码
        int code = (int) ((Math.random() * 9 + 1) * 10000);

        if (!smsLimiter.sendSmsAuth(phoneNum, String.valueOf(code))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送频率过高，请稍后重试");
        }
        SmsTo smsTo = new SmsTo(phoneNum, String.valueOf(code));
        try {
            rabbitMqUtils.sendSms(smsTo);
        } catch (Exception e) {
            redisTemplate.delete("sms:" + phoneNum + "_tokens");
            redisTemplate.delete("sms:" + phoneNum + "_last_refill_time");
            log.error("短信发送过程抛出异常：==》{}",e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "短信发送失败，请稍后尝试");
        }
        log.info("手机号为：==》{}短信发送成功,code为==》{}", phoneNum, code);
        return true;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) principal;
        return user != null && UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String name = userPrincipal.getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser == null || currentUser.getId() == null || !currentUser.getUserAccount().equals(name)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
//        long userId = currentUser.getId();
//        currentUser = this.getById(userId);
//        if (currentUser == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
//        }
        return currentUser;
    }

    @Override
    public LoginUserVo userLogin(String userAccount, String userPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userAccount);
        if (!passwordEncoder.matches(userPassword, userDetails.getPassword())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户名或密码错误");
        }
        return initUser(userDetails);
    }

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String mobile = userRegisterRequest.getMobile();
        String code = userRegisterRequest.getCode();
        String captcha = userRegisterRequest.getCaptcha();
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, mobile, code, captcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 图形验证码是否正确
        String signature = request.getHeader("signature");
        if (null == signature) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String picCaptcha = (String) redisTemplate.opsForValue().get(RedisConstant.CAPTCHA_PREFIX + signature);
        if (AuthPhoneNumberUtil.isCaptcha(captcha) || !captcha.equals(picCaptcha)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图形验证码错误或已经过期，请重新刷新验证码");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        if (!AuthPhoneNumberUtil.isPhoneNum(mobile)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号非法");
        }
        // 手机号和验证码是否匹配
        boolean verify = smsLimiter.verifyCode(mobile, code);
        if (!verify) {
            throw new BusinessException(ErrorCode.SMS_CODE_ERROR);
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            //手机号不能重复
            String getMapperPhone = userMapper.selectPhone(mobile);
            if (null != getMapperPhone) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号已经被注册");
            }
            // 2. 加密
            String encryptPassword = passwordEncoder.encode(userPassword);

            // 3. 分配 accessKey, secretKey
            String accessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            // 4. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setMobile(mobile);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }


    @Override
    public boolean userLogout(HttpServletRequest request) {
        return false;
    }

    @Override
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        // 随机生成 4 位验证码
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        // 定义图片的显示大小
        LineCaptcha lineCaptcha = null;
        lineCaptcha = CaptchaUtil.createLineCaptcha(100, 30);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        // 在前端发送请求时携带captchaId，用于标识不同的用户。
        String signature = request.getHeader("signature");
        if (null == signature) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        try {
            // 调用父类的 setGenerator() 方法，设置验证码的类型
            lineCaptcha.setGenerator(randomGenerator);
            // 输出到页面
            lineCaptcha.write(response.getOutputStream());
            // 打印日志
            log.info("captchaId：{} ----生成的验证码:{}", signature, lineCaptcha.getCode());
            // 关闭流
            response.getOutputStream().close();
            //将对应的验证码存入redis中去，2分钟后过期
            redisTemplate.opsForValue().set(RedisConstant.CAPTCHA_PREFIX + signature, lineCaptcha.getCode(), 4, TimeUnit.MINUTES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LoginUserVo loginBySms(UserLoginBySmsRequest userLoginBySmsRequest) {
        if (userLoginBySmsRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String code = userLoginBySmsRequest.getCode();
        String mobile = userLoginBySmsRequest.getMobile();
        if (StringUtils.isAnyBlank(code,mobile)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"手机号或验证码为空");
        }
        if (!AuthPhoneNumberUtil.isPhoneNum(mobile)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"手机号格式错误");
        }
        // 查询用户存不存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("mobile",mobile));
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"手机号不存在");
        }
        if (!smsLimiter.verifyCode(mobile,code)) {
            throw new BusinessException(ErrorCode.SMS_CODE_ERROR);
        }
        return initUser(user);
    }

    @Override
    public String getGithubStart() {
        String listContent = null;
        try {
            listContent= HttpUtil.get("https://img.shields.io/github/stars/lklove?style=social");
        }catch (Exception e){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"获取GitHub Starts 超时");
        }
        //该操作查询时间较长
        List<String> titles = ReUtil.findAll("<title>(.*?)</title>", listContent, 1);
        String stars = null;
        for (String title : titles) {
            //打印标题
            String[] split = title.split(":");
            stars = split[1];
        }
        return stars;
    }

    @Override
    public BaseResponse bindPhone(UserBindPhoneRequest userBindPhoneRequest, HttpServletRequest request) {
        String captcha = userBindPhoneRequest.getCaptcha();
        Long id = userBindPhoneRequest.getId();
        String userAccount = userBindPhoneRequest.getUserAccount();
        String mobile = userBindPhoneRequest.getMobile();
        String code = userBindPhoneRequest.getCode();
        String signature = request.getHeader("signature");
        if (StringUtils.isAnyBlank(userAccount, String.valueOf(id),mobile,code,captcha,signature)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String picCaptcha = (String) redisTemplate.opsForValue().get(RedisConstant.CAPTCHA_PREFIX + signature);
        //验证图形验证码是否正确
        if (null == picCaptcha || AuthPhoneNumberUtil.isCaptcha(captcha) || !captcha.equals(picCaptcha)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"图形验证码错误或已经过期，请重新刷新验证码");
        }
        //验证手机号是否正确
        if(!AuthPhoneNumberUtil.isPhoneNum(mobile)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"手机号非法");
        }
        // 手机号和验证码是否匹配
        boolean verify = smsLimiter.verifyCode(mobile, code);
        if (!verify){
            throw new BusinessException(ErrorCode.SMS_CODE_ERROR);
        }
        synchronized (userAccount.intern()){
            //手机号不能重复
            String getMapperPhone = userMapper.selectPhone(mobile);
            if (null != getMapperPhone){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"手机号已经被注册");
            }
            boolean update = this.update(new UpdateWrapper<User>().eq("id", id).eq("userAccount", userAccount).set("mobile", mobile));
            if (!update){
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        }
        String phone = DesensitizedUtil.mobilePhone(mobile);
        //更新全局对象中的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser =(User) authentication.getPrincipal();
        currentUser.setMobile(phone);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return ResultUtils.success(phone);
    }

    private LoginUserVo initUser(UserDetails userDetails){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(userDetails, loginUserVo);
        String token = jwtTokenUtil.generateToken(userDetails);
        loginUserVo.setToken(token);
        loginUserVo.setTokenHead(tokenHead);
        return loginUserVo;
    }
}




