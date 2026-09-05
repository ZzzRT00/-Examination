package com.xhj.examination.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course_name {
    private long id;
    private long user_id;
    private long course_id;
}
