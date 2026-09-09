package com.xhj.examination.service;

import com.xhj.examination.entity.ExamRecord;

import java.util.List;

public interface ExamRecordService {

    void addRecord(ExamRecord examRecord);

    List<ExamRecord> getStudentRecord(Long studentId);

    ExamRecord getById(Long id);
}
