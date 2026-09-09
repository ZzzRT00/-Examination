package com.xhj.examination.service;

import com.xhj.examination.entity.PaperQuestion;
import com.xhj.examination.entity.PaperQuestionVO;

import java.util.List;

public interface PaperQuestionService {
    int add(PaperQuestion paperQuestion);

    List<PaperQuestion> getByPaperId(Long paperId);

    List<PaperQuestionVO> getQuestionByPaperId(Long paperId);
}
