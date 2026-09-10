package com.xhj.examination.mapper;


import com.xhj.examination.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> selectAll();

    User selectByName(String name);

    User selectByNumber(String number);

    void insert(User user);

    void update(User user);

    void deleteById(Integer id);

    User findUserByLoginInfo(User user);

    User selectById(Long userId);
}
