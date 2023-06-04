package com.edkai.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.project.common.BranchDeleteRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edkai.project.model.vo.InterfaceDetailVo;
import com.edkai.common.model.vo.InterfaceInfoVo;
import com.edkai.project.model.vo.ManageInterfaceVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author MyASUS
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-02-01 12:06:32
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 对接口进行验证
     * @param interfaceInfo 验证的 interfaceInfo
     * @param add 是否为添加操作
     */
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 调用接口，得到返回值
     * @param interfaceInvokeRequest
     * @param request
     * @return
     */
    Object invoke(InterfaceInvokeRequest interfaceInvokeRequest, HttpServletRequest request);

    /**
     * 分页查询接口信息
     *
     * @param current
     * @param pageSize
     * @param request
     * @return
     */
    Page<InterfaceInfoVo> listInterfaceInfoByPage(int current,int pageSize,HttpServletRequest request);

    /**
     * 添加接口
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request);

    /**
     * 单个删除或者批量删除接口
     * @param branchDeleteRequest
     * @param request
     * @return
     */
    Boolean deleteInterfaceInfo(BranchDeleteRequest branchDeleteRequest, HttpServletRequest request);

    /**
     * 更新接口
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request);

    /**
     * 管理页面的接口信息
     * @param interfaceInfoQueryRequest
     * @return
     */
    Page<ManageInterfaceVo> listInterfaceInfoAllByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 根据id获取接口的详情
     * @param id
     * @return
     */
    InterfaceDetailVo getInterfaceInfoById(long id,HttpServletRequest request);

    /**
     * 将真实的请求地址转换为请求网关的地址
     * @param url 真实请求地址
     * @return 请求网关的请求地址
     */
    public String secretUrl(String url);
}
