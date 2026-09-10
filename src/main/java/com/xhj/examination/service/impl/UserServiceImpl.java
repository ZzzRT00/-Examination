package com.xhj.examination.service.impl;

import com.xhj.examination.entity.User;
import com.xhj.examination.mapper.UserMapper;
import com.xhj.examination.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.update(user);
    }

    @Override
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public User login(User user) {
        User dbUser = userMapper.selectByNumber(user.getNumber());
        if (dbUser == null) {
            return null;
        }
        if (passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return dbUser;
        }
        return null;
    }

    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }
}
