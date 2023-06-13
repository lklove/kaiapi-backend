package com.edkai.kaiapiinterface.controller;

import com.edkai.common.BaseResponse;
import com.edkai.common.utils.ResultUtils;
import com.edkai.kaiapiinterface.modle.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author liukai
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping
    public BaseResponse<String> getNameByGet(String name){
        String result="GET我的名字是"+name;
        return ResultUtils.success(result) ;
    }

    @PostMapping("/user")
    public BaseResponse<String> getNameByPost(@RequestBody User user){
        String result="POST我的名字是"+user.getName();
        return ResultUtils.success(result) ;
    }
}
