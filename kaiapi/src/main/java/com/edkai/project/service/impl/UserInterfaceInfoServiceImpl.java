package com.edkai.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.common.model.entity.UserInterfaceInfo;
import com.edkai.common.ErrorCode;
import com.edkai.common.model.to.AddUserInterfaceTo;
import com.edkai.common.constant.CommonConstant;
import com.edkai.common.exception.BusinessException;
import com.edkai.project.mapper.UserInterfaceInfoMapper;
import com.edkai.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.edkai.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.edkai.project.model.entity.User;
import com.edkai.project.model.vo.InterfaceLeftVo;
import com.edkai.project.model.vo.UserInterfaceInfoVO;
import com.edkai.project.service.InterfaceInfoService;
import com.edkai.project.service.UserInterfaceInfoService;
import com.edkai.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ASUS
 * @description 针对表【user_interface_info(用户接口信息关系表)】的数据库操作Service实现
 * @createDate 2023-02-23 19:30:59
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {
    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (add) {
            if (userInterfaceInfo.getUserId() <= 0 || userInterfaceInfo.getInterfaceInfoId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }

    @Override
    public Boolean invokeCount(long userId, long interfaceInfoId) {
        if (userId <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断有不有用户与该接口记录，没有则添加
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = this.getOne(queryWrapper);
        if (userInterfaceInfo == null){
            userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setTotalNum(1);
            return this.save(userInterfaceInfo);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId", userId);
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.setSql("leftNum = leftNum -1 ,totalNum = totalNum +1");
        return this.update(updateWrapper);
    }

    @Override
    public int leftNumOfInterface(long interfaceInfoId, long userId) {
        if (userId <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("userId", userId);
        UserInterfaceInfo userInterfaceInfo = this.getOne(queryWrapper);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return userInterfaceInfo.getLeftNum();
    }

    /**
     * 更新用户接口，如果没有则创建
     *
     * @param userInterfaceInfo
     * @param request
     * @return
     */
    @Override
    public boolean updateUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request) {
        // 参数校验
        this.validUserInterfaceInfo(userInterfaceInfo, false);
        long id = userInterfaceInfo.getId();
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = this.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldUserInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return this.updateById(userInterfaceInfo);
    }

    @Override
    public Page<UserInterfaceInfoVO> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        String userAccount = userInterfaceInfoQueryRequest.getUserAccount();
        String interfaceName = userInterfaceInfoQueryRequest.getInterfaceName();
        // 根据用户账户和接口名称查询相关的userId和InterfaceId
        if (StringUtils.isNotBlank(userAccount)){
            User userTmp = userService.getOne(new QueryWrapper<User>().eq("userAccount", userAccount));
            if (userTmp!=null){
                userInterfaceInfo.setUserId(userTmp.getId());
            }
        }
        if (StringUtils.isNotBlank(interfaceName)){
            InterfaceInfo interfaceInfo = interfaceInfoService.getOne(new QueryWrapper<InterfaceInfo>().eq("name", interfaceName));
            if (interfaceInfo!=null){
                userInterfaceInfo.setInterfaceInfoId(interfaceInfo.getId());
            }
        }

        // 根据分页条件查出所有
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = this.page(new Page<UserInterfaceInfo>(current, size), queryWrapper);
        if (userInterfaceInfoPage == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "数据不存在");
        }
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage = new Page<>();
        BeanUtils.copyProperties(userInterfaceInfoPage, userInterfaceInfoVOPage, "records");
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoPage.getRecords();
        // ======================将entity转换为VO=========================
        // 获得所有userId 和 interfaceId
        List<Long> userIdList = userInterfaceInfoList.stream().map(UserInterfaceInfo::getUserId).collect(Collectors.toList());
        List<Long> interfaceIdList = userInterfaceInfoList.stream().map(UserInterfaceInfo::getInterfaceInfoId).collect(Collectors.toList());
        List<User> userList = userService.listByIds(userIdList);
        if (CollectionUtils.isEmpty(userList)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //将list转换为map方便 id获取到对应的interfaceInfo
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, item -> item));
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.listByIds(interfaceIdList);
        //将list转换为map方便 id获取到对应的interfaceInfo
        Map<Long, InterfaceInfo> interfaceInfoMap = interfaceInfoList.stream().collect(Collectors.toMap(InterfaceInfo::getId, item -> item));
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //遍历 recorder 转换为 userInterfaceVo对象
        List<UserInterfaceInfoVO> userInterfaceInfoVOList = userInterfaceInfoList.stream().map(item -> {
            UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
            BeanUtils.copyProperties(item, userInterfaceInfoVO);
            User user = userMap.get(item.getUserId());
            userInterfaceInfoVO.setUserName(user.getNickName());
            userInterfaceInfoVO.setUserAccount(user.getUserAccount());
            InterfaceInfo interfaceInfo = interfaceInfoMap.get(item.getInterfaceInfoId());
            userInterfaceInfoVO.setInterfaceName(interfaceInfo.getName());
            userInterfaceInfoVO.setInterfaceDescription(interfaceInfo.getDescription());
            return userInterfaceInfoVO;
        }).collect(Collectors.toList());
        return userInterfaceInfoVOPage.setRecords(userInterfaceInfoVOList);
    }

    @Override
    public Long addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        String interfaceName;
        String userAccount;
        int leftNum=0;
        if (userInterfaceInfoAddRequest == null ||
                StringUtils.isBlank(userAccount=userInterfaceInfoAddRequest.getUserAccount())||
                StringUtils.isBlank(interfaceName=userInterfaceInfoAddRequest.getInterfaceName())||
                (leftNum= userInterfaceInfoAddRequest.getLeftNum())<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        // 根据账户查询id
        User user = userService.getOne(new QueryWrapper<User>().eq("userAccount", userAccount));
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 根据接口名称查询接口id
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(new QueryWrapper<InterfaceInfo>().eq("name", interfaceName));
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        Long userId = user.getId();
        Long interfaceInfoId = interfaceInfo.getId();
        long count = this.count(new QueryWrapper<UserInterfaceInfo>().eq("userId", userId).eq("interfaceInfoId", interfaceInfoId));
        if (count >0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"请勿重复添加");
        }
        userInterfaceInfo.setUserId(userId);
        userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
        userInterfaceInfo.setLeftNum(leftNum);
        boolean result = this.save(userInterfaceInfo);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return userInterfaceInfo.getId();
    }

    @Override
    public Page<InterfaceLeftVo> listUserInterfaceLeftNumByPage(@RequestParam("current") int current,
                                                                @RequestParam("pageSize") int pageSie,
                                                                HttpServletRequest request) {
        if (pageSie >50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Page<InterfaceLeftVo> interfaceLeftVoPage=userInterfaceInfoMapper.selectLeftNum(new Page<InterfaceLeftVo>(current,pageSie),loginUser.getId());
        List<InterfaceLeftVo> records = interfaceLeftVoPage.getRecords();
        List<InterfaceLeftVo> newRecords = records.stream().map(item -> {
            String realUrl = item.getUrl();
            item.setUrl(interfaceInfoService.secretUrl(realUrl));
            return item;
        }).collect(Collectors.toList());
        interfaceLeftVoPage.setRecords(newRecords);
        return interfaceLeftVoPage;
    }

    @Override
    public boolean addUserInterface(AddUserInterfaceTo addUserInterfaceTo) {
        if (addUserInterfaceTo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceId = addUserInterfaceTo.getInterfaceId();
        if (interfaceId <=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = addUserInterfaceTo.getUserId();
        if (userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long orderNum = addUserInterfaceTo.getOrderNum();
        if (orderNum <=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo oldUserInterfaceInfo = this.getOne(new QueryWrapper<UserInterfaceInfo>().eq("userId", userId).eq("interfaceInfoId", interfaceId));
        if (oldUserInterfaceInfo== null){
            oldUserInterfaceInfo = new UserInterfaceInfo();
            oldUserInterfaceInfo.setInterfaceInfoId(interfaceId);
            oldUserInterfaceInfo.setUserId(userId);
            oldUserInterfaceInfo.setLeftNum(orderNum.intValue());
            return this.save(oldUserInterfaceInfo);
        }
        Integer oldLeftNum = oldUserInterfaceInfo.getLeftNum();
        oldUserInterfaceInfo.setLeftNum(oldLeftNum+orderNum.intValue());
        return this.updateById(oldUserInterfaceInfo);
    }

}




