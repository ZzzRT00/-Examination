package com.xhj.examination.controller;


import com.xhj.examination.entity.Courses;
import com.xhj.examination.service.CoursesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
@Tag(name = "课程相关接口")
public class CoursesController {

    @Autowired
    private CoursesService coursesService;

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "查询教师课程")
    public List<Courses> getTeacherCourse(@PathVariable Long teacherId){
        return coursesService.getTeacherCourseList(teacherId);
    }
}
