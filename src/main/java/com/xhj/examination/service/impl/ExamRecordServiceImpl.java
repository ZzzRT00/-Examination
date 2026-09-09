package com.xhj.examination.service.impl;

import com.xhj.examination.entity.ExamRecord;
import com.xhj.examination.mapper.ExamRecordMapper;
import com.xhj.examination.service.ExamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamRecordServiceImpl implements ExamRecordService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Override
    public void addRecord(ExamRecord examRecord) {
        examRecordMapper.insert(examRecord);
    }

    @Override
    public List<ExamRecord> getStudentRecord(Long studentId) {
        return examRecordMapper.selectByStudentId(studentId);
    }

    @Override
    public ExamRecord getById(Long id) {
        return examRecordMapper.selectById(id);
    }
}
