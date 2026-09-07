package com.xhj.examination.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Courses {
    private long id;
    private String courseName;
    private long teacherId;
}
