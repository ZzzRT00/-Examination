package com.xhj.examination.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ExamRecord {
    private Long id;
    private Long studentId;
    private Long paperId;
    private Integer score;
    private LocalDateTime examTime;

}