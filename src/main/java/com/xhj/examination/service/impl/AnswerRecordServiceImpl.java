package com.xhj.examination.service.impl;

import com.xhj.examination.entity.*;
import com.xhj.examination.mapper.AnswerRecordMapper;
import com.xhj.examination.mapper.PaperMapper;
import com.xhj.examination.mapper.PaperQuestionMapper;
import com.xhj.examination.service.AnswerRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private PaperMapper paperMapper;

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
        if (submitExamVO == null || submitExamVO.getPaperId() == null || submitExamVO.getUserId() == null) {
            throw new IllegalArgumentException("试卷ID和用户ID不能为空");
        }
        if (submitExamVO.getAnswerList() == null || submitExamVO.getAnswerList().isEmpty()) {
            return 0;
        }

        Long paperId = submitExamVO.getPaperId();
        Long userId = submitExamVO.getUserId();

        Paper paper = paperMapper.selectById(paperId);
        if (paper != null && paper.getCourseId() != null) {
            Long courseId = paper.getCourseId();
            ScoreVO existing = answerRecordMapper.getLatestStudentCourseScore(userId, courseId);
            boolean isRetake = Boolean.TRUE.equals(submitExamVO.getRetake());
            if (existing != null && !isRetake) {
                throw new IllegalStateException("该课程已参加过考试，如需重新考试请点击重考");
            }
        }

        List<PaperQuestionVO> paperQuestionVOList = paperQuestionMapper.selectQuestionWithScoreByPaperId(paperId);
        if (paperQuestionVOList.isEmpty()) {
            return 0;
        }

        Map<Long, PaperQuestionVO> questionMap = paperQuestionVOList.stream()
                .collect(Collectors.toMap(PaperQuestionVO::getQuestionId, Function.identity()));

        int userScore = 0;
        for (SubmitExamVO.QuestionAnswerVO userAnswer : submitExamVO.getAnswerList()) {
            PaperQuestionVO question = questionMap.get(userAnswer.getQuestionId());
            if (question != null && Objects.equals(question.getAnswer(), userAnswer.getUserAnswer())) {
                userScore += question.getScore();
            }
        }

        int totalScore = paperQuestionVOList.stream()
                .mapToInt(q -> q.getScore() == null ? 0 : q.getScore())
                .sum();

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

    @Override
    public ScoreVO getLatestStudentCourseScore(Long userId, Long courseId) {
        return answerRecordMapper.getLatestStudentCourseScore(userId, courseId);
    }
}
