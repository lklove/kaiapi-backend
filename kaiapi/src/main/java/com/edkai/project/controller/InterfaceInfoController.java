package com.edkai.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.common.BaseResponse;
import com.edkai.common.ErrorCode;
import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.common.utils.ResultUtils;
import com.edkai.project.annotation.AuthCheck;
import com.edkai.project.common.*;
import com.edkai.common.exception.BusinessException;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.edkai.project.model.enums.InterfaceInfoStatusEnum;
import com.edkai.project.model.vo.InterfaceDetailVo;
import com.edkai.common.model.vo.InterfaceInfoVo;
import com.edkai.project.model.vo.ManageInterfaceVo;
import com.edkai.project.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口信息
 *
 * @author kai
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;


    /**
     * 创建
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long newInterfaceInfoId = interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest, request);

        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param branchDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody BranchDeleteRequest branchDeleteRequest, HttpServletRequest request) {
        if (branchDeleteRequest == null || branchDeleteRequest.getIds().length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = interfaceInfoService.deleteInterfaceInfo(branchDeleteRequest, request);

        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest, request);
        return ResultUtils.success(result);
    }

    @GetMapping("/feign/get")
    public BaseResponse<InterfaceInfoVo> feignGetInterfaceInfoById(long id,HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo=interfaceInfoService.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
        BeanUtils.copyProperties(interfaceInfo,interfaceInfoVo);
        return ResultUtils.success(interfaceInfoVo);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceDetailVo> getInterfaceInfoById(long id,HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceDetailVo interfaceDetailVo=interfaceInfoService.getInterfaceInfoById(id,request);
        return ResultUtils.success(interfaceDetailVo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取接口可用列表

     * @param
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfoVo>> listInterfaceInfoByPage(@RequestParam(defaultValue = "1", name = "current") int current,
                                                                       @RequestParam(defaultValue = "5", name = "size") int pageSie,
                                                                       HttpServletRequest request) {
        Page<InterfaceInfoVo> interfaceInfoPage = interfaceInfoService.listInterfaceInfoByPage(current, pageSie, request);
        return ResultUtils.success(interfaceInfoPage);
    }


    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> publish(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 发布接口
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offline(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 发布接口
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @PostMapping("/invoke")
    public BaseResponse invoke(@RequestBody InterfaceInvokeRequest interfaceInvokeRequest, HttpServletRequest request) {
        if (interfaceInvokeRequest == null || interfaceInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.invoke(interfaceInvokeRequest, request);
    }

    /**
     * 管理员获取所有的分页信息
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/AllPage")
    public BaseResponse<Page<ManageInterfaceVo>> listInterfaceInfoAllByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<ManageInterfaceVo> manageInterfaceVoPage = interfaceInfoService.listInterfaceInfoAllByPage(interfaceInfoQueryRequest);
        return ResultUtils.success(manageInterfaceVoPage);
    }

}
