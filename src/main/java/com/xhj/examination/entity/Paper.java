package com.xhj.examination.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Paper {
    private Integer id;
    private String paperName;
    private Integer totalScore;
    private Integer duration;
}
