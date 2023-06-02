package com.edkai.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.common.model.entity.UserInterfaceInfo;
import com.edkai.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.edkai.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edkai.project.model.vo.InterfaceLeftVo;
import com.edkai.project.model.vo.UserInterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ASUS
 * @description 针对表【user_interface_info(用户接口信息关系表)】的数据库操作Service
 * @createDate 2023-02-23 19:30:59
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 接口校验
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 统计调用次数
     *
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    Boolean invokeCount(long userId, long interfaceInfoId);

    int leftNumOfInterface(long interfaceInfoId, long userId);


    boolean updateUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request);

    /**
     * 获取分页用户接口视图对象
     * @param userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    Page<UserInterfaceInfoVO> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request);

    /**
     * 添加用户与接口关系
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    Long addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request);

    /**
     * 获取用户接口调用剩余次数
     * @param current
     * @param pageSie
     * @param request
     * @return
     */
    Page<InterfaceLeftVo> listUserInterfaceLeftNumByPage(int current, int pageSie, HttpServletRequest request);
}
