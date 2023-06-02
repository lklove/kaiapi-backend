package com.edkai.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edkai.project.model.entity.User;
import org.apache.ibatis.annotations.Param;


/**
* @author ASUS
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-05-23 14:59:13
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询库中是否保存了手机号
     * @param mobile 手机号
     * @return 返回手机号
     */
    String selectPhone(@Param("mobile") String mobile);
}




