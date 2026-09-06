package com.xhj.examination.mapper;


import com.xhj.examination.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    //查询全部用户
    List<User> selectAll();

    User selectByName(String name);

    void insert(User user);

    void update(User user);
}
