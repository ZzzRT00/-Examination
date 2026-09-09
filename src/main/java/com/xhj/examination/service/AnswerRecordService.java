package com.xhj.examination.service;

import com.xhj.examination.entity.AnswerRecord;

import java.util.List;

public interface AnswerRecordService {
    int submitExam(AnswerRecord answerRecord);
    List<AnswerRecord> getUserRecord(Long userId);
    List<AnswerRecord> getPaperAllRecord(Long paperId);
}
