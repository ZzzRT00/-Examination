package com.xhj.examination.service;

import com.xhj.examination.entity.Courses;

import java.util.List;

public interface CoursesService {
    List<Courses> getCoursesByUserId(Long userId);

    List<Courses> getCoursesByTeacherId(Long teacherId);
}
