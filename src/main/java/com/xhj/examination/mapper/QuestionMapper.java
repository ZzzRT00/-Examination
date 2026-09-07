package com.xhj.examination.mapper;

import com.xhj.examination.entity.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<Question> getQuestionByCourseId(Long courseId);

    int addQuestion(Question question);
}
