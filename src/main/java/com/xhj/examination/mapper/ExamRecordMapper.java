package com.xhj.examination.mapper;

import com.xhj.examination.entity.ExamRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamRecordMapper {
    void insert(ExamRecord examRecord);

    List<ExamRecord> selectByStudentId(Long studentId);

    ExamRecord selectById(Long id);
}
