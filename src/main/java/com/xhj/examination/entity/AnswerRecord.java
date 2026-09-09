package com.xhj.examination.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerRecord {
    private Long id;
    private Long userId;
    private Long paperId;
    private Integer totalScore;
    private Integer userScore;
    private LocalDateTime createTime;
}
