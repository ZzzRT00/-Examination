package com.xhj.examination.entity;

import lombok.Data;

@Data
public class PaperQuestion {
    private Long id;
    private Long paperId;
    private Long questionId;
    private Integer score;
}