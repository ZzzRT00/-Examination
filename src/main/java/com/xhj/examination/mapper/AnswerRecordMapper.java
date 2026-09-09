package com.xhj.examination.mapper;

import com.xhj.examination.entity.AnswerRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswerRecordMapper {
    int insert(AnswerRecord answerRecord);

    List<AnswerRecord> selectByUserId(Long userId);

    List<AnswerRecord> selectByPaperId(Long paperId);
}
