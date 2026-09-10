package com.xhj.examination.entity;

import lombok.Data;

@Data
public class StudentPaperQuestionVO {
    private Long questionId;
    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Integer score;
}
