package com.xhj.examination.service;

import com.xhj.examination.entity.User;

import java.util.List;

public interface UserService {
    List<User> selectAll();

    User selectByName(String name);

    void add(User user);

    void update(User user);
}
