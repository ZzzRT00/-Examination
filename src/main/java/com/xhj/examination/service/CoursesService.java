package com.xhj.examination.service;

import com.xhj.examination.entity.Courses;

import java.util.List;

public interface CoursesService {
    List<Courses> getTeacherCourseList(Long teacherId);
}
