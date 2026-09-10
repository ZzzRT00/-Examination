package com.xhj.examination.service.impl;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.Question;
import com.xhj.examination.entity.StudentPaperQuestionVO;
import com.xhj.examination.mapper.PaperQuestionMapper;
import com.xhj.examination.mapper.QuestionMapper;
import com.xhj.examination.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Override
    public List<Question> getQuestionList(Long courseId) {
        return questionMapper.getQuestionByCourseId(courseId);
    }

    @Override
    public int add(Question question) {
        return questionMapper.addQuestion(question);
    }

    @Override
    public Result<String> updateQuestion(Question question) {
        int row = questionMapper.updateQuestion(question);
        return row > 0 ? Result.success("修改成功") : Result.fail(200,"修改失败");
    }

    @Override
    public Result<String> deleteQuestion(Integer id) {
        int row = questionMapper.deleteQuestion(id);
        return row > 0 ? Result.success("删除成功") : Result.fail(200,"删除失败");
    }

    @Override
    public List<StudentPaperQuestionVO> getStudentQuestionList(Long paperId) {
        return paperQuestionMapper.listStudentQuestionsByPaperId(paperId);
    }
}
