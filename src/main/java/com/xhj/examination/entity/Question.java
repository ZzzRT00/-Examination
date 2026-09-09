package com.xhj.examination.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Question {
    private Long id;
    private Long courseId;
    private String title;
    private String a;
    private String b;
    private String c;
    private String d;
    private String answer;
}
