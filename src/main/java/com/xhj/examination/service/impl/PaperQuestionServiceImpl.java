package com.xhj.examination.service.impl;

import com.xhj.examination.entity.PaperQuestion;
import com.xhj.examination.mapper.PaperQuestionMapper;
import com.xhj.examination.service.PaperQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperQuestionServiceImpl implements PaperQuestionService {

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Override
    public int add(PaperQuestion paperQuestion) {
        return paperQuestionMapper.addPaperQuestion(paperQuestion);
    }

    @Override
    public List<PaperQuestion> getByPaperId(Long paperId) {
        return paperQuestionMapper.listByPaperId(paperId);
    }
}
