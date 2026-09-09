package com.xhj.examination.entity;

import lombok.Data;

@Data
public class PaperQuestionVO {
    private Long id;
    private Long paperId;
    private Long questionId;
    private Integer score;

    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
}
