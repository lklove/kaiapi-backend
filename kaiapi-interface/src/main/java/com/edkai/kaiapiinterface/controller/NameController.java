package com.edkai.kaiapiinterface.controller;

import com.edkai.kaiapiinterface.modle.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author liukai
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping
    public String getNameByGet(String name){
        return "GET我的名字是"+name;
    }

    @PostMapping("/user")
    public String getNameByPost(@RequestBody User user){
        return "POST我的名字是"+user.getName();
    }
}
