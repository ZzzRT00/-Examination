package com.xhj.examination.service.impl;

import com.xhj.examination.service.AnswerRecordService;
import com.xhj.examination.entity.AnswerRecord;
import com.xhj.examination.mapper.AnswerRecordMapper;
import com.xhj.examination.service.AnswerRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnswerRecordServiceImpl implements AnswerRecordService {

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    @Override
    public int submitExam(AnswerRecord answerRecord) {
        return answerRecordMapper.insert(answerRecord);
    }

    @Override
    public List<AnswerRecord> getUserRecord(Long userId) {
        return answerRecordMapper.selectByUserId(userId);
    }

    @Override
    public List<AnswerRecord> getPaperAllRecord(Long paperId) {
        return answerRecordMapper.selectByPaperId(paperId);
    }
}
