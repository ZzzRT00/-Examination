package com.xhj.examination.service.impl;

import com.xhj.examination.entity.Courses;
import com.xhj.examination.mapper.CoursesMapper;
import com.xhj.examination.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesServiceImpl implements CoursesService {

    @Autowired
    private CoursesMapper coursesMapper;

    @Override
    public List<Courses> getCoursesByUserId(Long userId) {
        return coursesMapper.getCoursesByUserId(userId);
    }

    @Override
    public List<Courses> getCoursesByTeacherId(Long teacherId) {
        return coursesMapper.getCoursesByTeacherId(teacherId);
    }
}
