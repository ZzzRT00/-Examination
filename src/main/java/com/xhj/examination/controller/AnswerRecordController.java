package com.xhj.examination.controller;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.AnswerRecord;
import com.xhj.examination.entity.ScoreVO;
import com.xhj.examination.entity.SubmitExamVO;
import com.xhj.examination.service.AnswerRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.List;

@RestController
@RequestMapping("/answerRecord")
@Tag(name = "考试答卷记录相关接口")
public class AnswerRecordController {

    @Autowired
    private AnswerRecordService answerRecordService;

    @PostMapping("/submit")
    @Operation(summary = "提交考试答卷")
    public String submit(@RequestBody AnswerRecord answerRecord){
        int res = answerRecordService.submitExam(answerRecord);
        return res>0?"考试提交成功":"提交失败";
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "查询某个学生全部考试记录")
    public List<AnswerRecord> getUserRecord(@PathVariable Long userId){
        return answerRecordService.getUserRecord(userId);
    }

    @GetMapping("/paper/{paperId}")
    @Operation(summary = "查询某张试卷所有学生答卷")
    public List<AnswerRecord> getPaperRecord(@PathVariable Long paperId){
        return answerRecordService.getPaperAllRecord(paperId);
    }

    @PostMapping("/examSubmit")
    @Operation(summary = "提交判分")
    public Result<Integer> examSubmit(@RequestBody SubmitExamVO submitExamVO){
        Integer score = answerRecordService.calcScoreAndSave(submitExamVO);
        return Result.success(score);
    }

    @GetMapping("/courseScore/{courseId}")
    @Operation(summary = "查询某课程所有学生考试成绩")
    public Result<List<ScoreVO>> getCourseScore(@PathVariable Long courseId){
        List<ScoreVO> list = answerRecordService.getCourseScoreList(courseId);
        return Result.success(list);
    }

    @GetMapping("/studentScore")
    @Operation(summary = "查询某学生某课程历次成绩")
    public Result<List<ScoreVO>> getStudentScore(@RequestParam Long userId, @RequestParam Long courseId){
        List<ScoreVO> list = answerRecordService.getStudentCourseScore(userId, courseId);
        return Result.success(list);
    }
}
