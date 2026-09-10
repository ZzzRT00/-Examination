package com.xhj.examination.service;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.Question;
import com.xhj.examination.entity.StudentPaperQuestionVO;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionList(Long courseId);

    int add(Question question);

    Result<String> updateQuestion(Question question);

    Result<String> deleteQuestion(Integer id);

    List<StudentPaperQuestionVO> getStudentQuestionList(Long paperId);
}
