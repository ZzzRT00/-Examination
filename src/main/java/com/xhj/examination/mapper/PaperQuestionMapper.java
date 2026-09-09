package com.xhj.examination.mapper;

import com.xhj.examination.entity.PaperQuestion;
import com.xhj.examination.entity.PaperQuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaperQuestionMapper {
    int addPaperQuestion(PaperQuestion paperQuestion);

    List<PaperQuestion> listByPaperId(Long paperId);

    List<PaperQuestionVO> selectByPaperId(Long paperId);
}
