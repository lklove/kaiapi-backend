package com.edkai.kaiapiinterface.controller;

import com.edkai.common.BaseResponse;
import com.edkai.common.utils.ResultUtils;
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

}
