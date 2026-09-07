package com.xhj.examination.service.impl;

import com.xhj.examination.entity.User;
import com.xhj.examination.mapper.UserMapper;
import com.xhj.examination.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }


    @Override
    public User selectByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public void add(User user) {
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public User login(User user) {
        return userMapper.findUserByLoginInfo(user);
    }
}
