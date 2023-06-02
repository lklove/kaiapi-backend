package com.edkai.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.common.model.entity.UserInterfaceInfo;
import com.edkai.project.model.vo.InterfaceLeftVo;
import org.apache.ibatis.annotations.Param;

/**
* @author ASUS
* @description 针对表【user_interface_info(用户接口信息关系表)】的数据库操作Mapper
* @createDate 2023-02-23 19:30:59
* @Entity com.edkai.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    Page<InterfaceLeftVo> selectLeftNum(Page<InterfaceLeftVo> objectPage, @Param("userId") Long userId);
}




