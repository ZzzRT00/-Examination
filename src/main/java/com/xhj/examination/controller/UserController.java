package com.xhj.examination.controller;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.xhj.examination.common.Result;
import com.xhj.examination.entity.User;
import com.xhj.examination.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.WebResourceRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name="用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @Operation(summary="查询所有用户")
    public Result<List<User>> list(){
        List<User> userList=userService.selectAll();
        return Result.success(userList);
    }


    @GetMapping("/find")
    @Operation(summary = "查询指定用户")
    public Result<User> selectUserByName(@RequestParam String name) {
        User user = userService.selectByName(name);
        return Result.success(user);
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public Result<Void> add(@RequestBody User user)
    {
        userService.add(user);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改用户信息")
    public Result<Void> update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }

}