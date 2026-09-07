package com.xhj.examination.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Question {
    private long id;
    private long course_id;
    private String title;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;
}
