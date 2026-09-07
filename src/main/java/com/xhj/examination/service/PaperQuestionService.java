package com.xhj.examination.service;

import com.xhj.examination.entity.PaperQuestion;

import java.util.List;

public interface PaperQuestionService {
    int add(PaperQuestion paperQuestion);

    List<PaperQuestion> getByPaperId(Long paperId);
}
