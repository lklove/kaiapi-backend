package com.edkai.order.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edkai.order.model.entity.ApiOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edkai.order.model.vo.OrderDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author ASUS
* @description 针对表【api_order】的数据库操作Mapper
* @createDate 2023-06-02 13:50:03
* @Entity com.edkai.order.model.entity.ApiOrder
*/
@Mapper
public interface ApiOrderMapper extends BaseMapper<ApiOrder> {

    Page<OrderDetailVo> getCurrentOrderInfo(Page<Object> objectPage, @Param("userId") Long userId, @Param("status") Integer status);
}




