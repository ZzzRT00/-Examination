package com.xhj.examination.service.impl;

import com.xhj.examination.entity.*;
import com.xhj.examination.mapper.PaperQuestionMapper;
import com.xhj.examination.mapper.QuestionMapper;
import com.xhj.examination.service.AnswerRecordService;
import com.xhj.examination.mapper.AnswerRecordMapper;
import com.xhj.examination.service.AnswerRecordService;
import com.xhj.examination.service.PaperQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnswerRecordServiceImpl implements AnswerRecordService {

    @Autowired
    private AnswerRecordMapper answerRecordMapper;
    @Autowired
    private PaperQuestionMapper paperQuestionMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private PaperQuestionService paperQuestionService;

    @Override
    public int submitExam(AnswerRecord answerRecord) {
        return answerRecordMapper.insert(answerRecord);
    }

    @Override
    public List<AnswerRecord> getUserRecord(Long userId) {
        return answerRecordMapper.selectByUserId(userId);
    }

    @Override
    public List<AnswerRecord> getPaperAllRecord(Long paperId) {
        return answerRecordMapper.selectByPaperId(paperId);
    }

    @Override
    public Integer calcScoreAndSave(SubmitExamVO submitExamVO) {
//        Integer paperId = submitExamVO.getPaperId();
//        Integer userId = submitExamVO.getUserId();
//
//        List<PaperQuestionVO> paperQuestionVOList = paperQuestionMapper.selectQuestionWithScoreByPaperId(paperId);
//
//        int userScore = 0;
//        for (SubmitExamVO.QuestionAnswerVO userAnswer : submitExamVO.getAnswerList()) {
//            for (PaperQuestionVO q : paperQuestionVOList) {
//                if(q.getQuestionId().equals(userAnswer.getQuestionId())){
//                    if (Objects.equals(q.getAnswer(), userAnswer.getUserAnswer())) {
//                        userScore += q.getScore();
//                    }
//                    break;
//                }
//            }
//        }
//
//        int totalScore = paperQuestionVOList.stream()
//                .mapToInt(q -> q.getScore() == null ? 0 : q.getScore())
//                .sum();
//
//        AnswerRecord record = new AnswerRecord();
//        record.setUserId(userId);
//        record.setPaperId(paperId);
//        record.setTotalScore(totalScore);
//        record.setUserScore(userScore);
//        answerRecordMapper.insert(record);
//
//        return userScore;
        // ========== 1. 前置参数校验：避免空指针 ==========
        if (submitExamVO == null || submitExamVO.getPaperId() == null || submitExamVO.getUserId() == null) {
            throw new IllegalArgumentException("试卷ID和用户ID不能为空");
        }
        if (submitExamVO.getAnswerList() == null || submitExamVO.getAnswerList().isEmpty()) {
            return 0;
        }

        Long paperId = submitExamVO.getPaperId();
        Long userId = submitExamVO.getUserId();

        List<PaperQuestionVO> paperQuestionVOList = paperQuestionMapper.selectQuestionWithScoreByPaperId(paperId);
        if (paperQuestionVOList.isEmpty()) {
            return 0;
        }

        // ========== 2. 核心优化：双层循环 → Map匹配，性能从O(n*m)降到O(n+m) ==========
        // 把题目列表转成Map，key是题目ID，value是题目对象，一次遍历即可
        Map<Long, PaperQuestionVO> questionMap = paperQuestionVOList.stream()
                .collect(Collectors.toMap(PaperQuestionVO::getQuestionId, Function.identity()));

        int userScore = 0;
        for (SubmitExamVO.QuestionAnswerVO userAnswer : submitExamVO.getAnswerList()) {
            // 直接从Map取题目，不用循环遍历
            PaperQuestionVO question = questionMap.get(userAnswer.getQuestionId());
            // 题目存在 + 答案正确 才加分
            if (question != null && Objects.equals(question.getAnswer(), userAnswer.getUserAnswer())) {
                userScore += question.getScore();
            }
        }

        // ========== 3. 简化总分计算 ==========
        // 直接求和，score为null时默认0，和你原来的逻辑一致
        int totalScore = paperQuestionVOList.stream()
                .mapToInt(q -> q.getScore() == null ? 0 : q.getScore())
                .sum();

        // ========== 4. 补充创建时间 ==========
        AnswerRecord record = new AnswerRecord();
        record.setUserId(userId);
        record.setPaperId(paperId);
        record.setTotalScore(totalScore);
        record.setUserScore(userScore);
        record.setCreateTime(LocalDateTime.now());

        answerRecordMapper.insert(record);

        return userScore;
    }

    @Override
    public List<ScoreVO> getCourseScoreList(Long courseId) {
        return answerRecordMapper.getCourseScoreList(courseId);
    }

    @Override
    public List<ScoreVO> getStudentCourseScore(Long userId, Long courseId) {
        return answerRecordMapper.getStudentCourseScore(userId, courseId);
    }
}
