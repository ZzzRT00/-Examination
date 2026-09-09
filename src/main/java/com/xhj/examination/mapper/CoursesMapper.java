package com.xhj.examination.mapper;

import com.xhj.examination.entity.Courses;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoursesMapper {
    List<Courses> getCoursesByUserId(Long userId);
}
