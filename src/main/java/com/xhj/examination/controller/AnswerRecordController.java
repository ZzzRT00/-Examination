package com.xhj.examination.controller;

import com.xhj.examination.entity.AnswerRecord;
import com.xhj.examination.service.AnswerRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
}
