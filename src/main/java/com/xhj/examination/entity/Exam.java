package com.xhj.examination.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class Exam {
    private long id;
    private long student_id;
    private long course_id;
    private int score;
    private LocalDateTime time;
}
