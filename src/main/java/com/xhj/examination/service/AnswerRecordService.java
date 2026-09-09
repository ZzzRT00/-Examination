package com.xhj.examination.service;

import com.xhj.examination.entity.AnswerRecord;
import com.xhj.examination.entity.ScoreVO;
import com.xhj.examination.entity.SubmitExamVO;

import java.util.List;

public interface AnswerRecordService {
    int submitExam(AnswerRecord answerRecord);
    List<AnswerRecord> getUserRecord(Long userId);
    List<AnswerRecord> getPaperAllRecord(Long paperId);

    Integer calcScoreAndSave(SubmitExamVO submitExamVO);

    List<ScoreVO> getCourseScoreList(Long courseId);

    List<ScoreVO> getStudentCourseScore(Long userId, Long courseId);
}
