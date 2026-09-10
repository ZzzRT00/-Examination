package com.xhj.examination.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Paper {
    private Long id;
    private String paperName;
    private Integer totalScore;
    private Integer duration;
    private Long courseId;
}
