package com.xhj.examination.entity;

import lombok.Data;
import java.util.List;

@Data
public class SubmitExamVO {
    private Long userId;
    private Long paperId;
    private List<QuestionAnswerVO> answerList;
    private Boolean retake;

    @Data
    public static class QuestionAnswerVO{
        private Long questionId;
        private String userAnswer;
    }
}
