package com.xhj.examination.controller;


import com.xhj.examination.entity.User;
import com.xhj.examination.service.UserService;
import org.apache.catalina.WebResourceRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> list(){
        return userService.selectAll();
    }
}