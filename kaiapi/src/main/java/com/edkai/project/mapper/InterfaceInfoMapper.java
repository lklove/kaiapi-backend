package com.edkai.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.edkai.project.model.vo.InterfaceDetailVo;
import com.edkai.project.model.vo.ManageInterfaceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author MyASUS
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
* @createDate 2023-02-01 12:06:32
* @Entity entity.model.com.edkai.project.InterfaceInfo
*/
@Mapper
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    Page<ManageInterfaceVo> selectAllPage(Page<ManageInterfaceVo> objectPage, @Param("queryRequest") InterfaceInfoQueryRequest interfaceInfoQueryRequest);


}




