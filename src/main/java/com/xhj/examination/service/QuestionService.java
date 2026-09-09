package com.xhj.examination.service;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionList(Long courseId);

    int add(Question question);

    Result updateQuestion(Question question);

    Result deleteQuestion(Integer id);
}
