package com.xhj.examination.controller;


import com.xhj.examination.common.Result;
import com.xhj.examination.entity.User;
import com.xhj.examination.service.UserService;
import com.xhj.examination.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name="用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String,Object>> login(@RequestBody User user){
        User loginUser = userService.login(user);
        if(loginUser == null){
            return Result.fail(401,"账号密码或身份错误");
        }
        String token = JwtUtil.createToken(loginUser.getId(), loginUser.getIdentity());
        loginUser.setPassword(null);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("userInfo", loginUser);
        resultMap.put("token", token);
        return Result.success(resultMap);
    }

    @GetMapping("/list")
    @Operation(summary="查询所有用户")
    public Result<List<User>> list(){
        List<User> userList=userService.selectAll();
        return Result.success(userList);
    }


    @GetMapping("/find")
    @Operation(summary = "根据姓名查询用户")
    public Result<User> selectUserByName(@RequestParam String name) {
        User user = userService.selectByName(name);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户信息")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public Result<Void> add(@RequestBody User user)
    {
        userService.add(user);
        return Result.success();
    }

    @PutMapping("/student")
    @Operation(summary = "修改用户信息")
    public Result<Void> update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result<Void> delete(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }
}