package com.xhj.examination.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ExamRecord {
    private Long id;
    private Long studentId;
    private Long paperId;
    private Long courseId;
    private Integer score;
    private Date examTime;
}