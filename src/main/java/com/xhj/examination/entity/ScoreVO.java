package com.xhj.examination.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScoreVO {
    private Long userId;
    private String userName;
    private String studentNo;
    private Long paperId;
    private String paperName;
    private Long courseId;
    private String courseName;
    private Integer userScore;
    private Integer totalScore;
    private LocalDateTime examTime;
}

