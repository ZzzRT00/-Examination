package com.xhj.examination.mapper;

import com.xhj.examination.entity.PaperQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaperQuestionMapper {
    int addPaperQuestion(PaperQuestion paperQuestion);

    List<PaperQuestion> listByPaperId(Long paperId);
}
